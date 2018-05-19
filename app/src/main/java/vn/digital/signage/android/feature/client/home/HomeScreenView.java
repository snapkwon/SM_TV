package vn.digital.signage.android.feature.client.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.InjectView;
import im.delight.android.webview.AdvancedWebView;
import vn.digital.signage.android.Constants;
import vn.digital.signage.android.R;
import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.response.LayoutResponse;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.base.MainActivity;
import vn.digital.signage.android.feature.client.home.face.BaseFaceDetectionFragment;
import vn.digital.signage.android.feature.client.home.face.FaceDetectionFragment;
import vn.digital.signage.android.feature.client.home.face.logic.FaceDetectionImpl;
import vn.digital.signage.android.feature.client.home.face.logic.IFaceDetection;
import vn.digital.signage.android.utils.DateUtils;
import vn.digital.signage.android.utils.FileUtils;
import vn.digital.signage.android.utils.NetworkUtils;
import vn.digital.signage.android.utils.asynctask.AsyncTaskManager;
import vn.digital.signage.android.utils.asynctask.MediaDownloadTask;
import vn.digital.signage.android.utils.asynctask.OnTaskCompleteListener;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.enumeration.MediaType;
import vn.digital.signage.android.utils.hash.HashFileChecker;
import vn.digital.signage.android.utils.hash.HashFileCheckerImpl;
import vn.digital.signage.android.utils.player.ExoPlayerImpl;
import vn.digital.signage.android.utils.player.IPlayer;
import vn.digital.signage.android.utils.player.VideoStateChanged;

public class HomeScreenView {

    private static final String TAG = HomeScreenView.class.getSimpleName();
    private static final String[] SUPPORT_FILES = new String[]{"MP4", "MP3", "JPG", "JPEG", "PNG"};
    private static final String[] EXP_IMAGES_FILES = new String[]{"JPG", "JPEG", "PNG"};
    private static final String[] EXP_WEBVIEW_FILES = new String[]{"HTML", "HTM"};

    private final Logger log = Logger.getLogger(HomeScreenView.class);
    @Inject
    HomeController homeController;
    @Inject
    SMRuntime runtime;
    @InjectView(R.id.fragment_home_adv_video_player)
    SimpleExoPlayerView exoVideoView;
    @InjectView(R.id.fragment_home_adv_image)
    ImageView imageView;
    @InjectView(R.id.fragment_home_adv_webview)
    AdvancedWebView webView;
    @InjectView(R.id.fragment_home_adv_debug)
    TextView txtDebug;

    private FaceDetectionFragment faceDetectionFragment;

    private int mCurrentMediaIndex = 0;
    private long mCurrentImageStartTimeInMillis = 0;
    private ImageCountDownTimer mCountDownTimer;
    private HomeFragment mContext;
    private AsyncTaskManager taskManager;
    private Handler mHandler = new Handler();
    private IPlayer mPlayer;
    private HashFileChecker mHashFileChecker;
    private IFaceDetection faceDetection;
    private OnTaskCompleteListener mOnTaskCompleteListener = new OnTaskCompleteListener() {
        @Override
        public void onTaskComplete(MediaDownloadTask task) {
            if (task != null) {
                if (task.isCancelled()) {
                    if (Config.hasLogLevel(LogLevel.DATA))
                        log.info("The download cancelled ...");
                    // Report about cancel
                    displayMessage("The download process has been ignored.");
                } else {
                    if (Config.hasLogLevel(LogLevel.DATA))
                        log.info("The download finished.");

                    if (mContext != null && mContext.getActivity() != null) {

                        // Report about result
                        displayMessage(mContext.getString(R.string.user_message_download_complete));
                        // play default video
                        playDefaultVideo(0);
                        // Start service, then download finished
                        ((MainActivity) mContext.getActivity()).onStartService();
                    }
                }
            }
        }
    };

    private AdvancedWebView.Listener mOnWebViewListener = new AdvancedWebView.Listener() {
        @Override
        public void onPageStarted(String url, Bitmap favicon) {
            if (Config.hasLogLevel(LogLevel.UI)) {
                log.info("mOnWebViewListener - onPageStarted");
            }
        }

        @Override
        public void onPageFinished(String url) {
            if (Config.hasLogLevel(LogLevel.UI)) {
                log.info("mOnWebViewListener - onPageFinished");
            }
        }

        @Override
        public void onPageError(int errorCode, String description, String failingUrl) {
            if (Config.hasLogLevel(LogLevel.UI)) {
                log.info("mOnWebViewListener - onPageError");
            }
        }

        @Override
        public void onDownloadRequested(String url,
                                        String suggestedFilename,
                                        String mimeType,
                                        long contentLength,
                                        String contentDisposition,
                                        String userAgent) {
            if (Config.hasLogLevel(LogLevel.UI)) {
                log.info("mOnWebViewListener - onDownloadRequested");
            }
        }

        @Override
        public void onExternalPageRequest(String url) {
            if (Config.hasLogLevel(LogLevel.UI)) {
                log.info("mOnWebViewListener - onExternalPageRequest");
            }
        }
    };

    @Inject
    public HomeScreenView() {
        // do nothing
    }

    private void displayMessage(String message) {
        if (message != null && runtime.getPlaylistProgressStart()) {
            Toast.makeText(mContext.getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void initViews(HomeFragment context) {
        mContext = context;
        // Create manager and set this activity as context and listener
        taskManager = new AsyncTaskManager(mContext.getActivity(), mOnTaskCompleteListener);
        // Handle task that can be retained before
        taskManager.handleRetainedTask((mContext.getActivity()).getLastCustomNonConfigurationInstance());
        // setup videoview
        mPlayer = new ExoPlayerImpl(mContext.getActivity(), exoVideoView, new VideoStateChanged() {
            @Override
            public void onError(String fileName, Exception error) {
                if (Config.hasLogLevel(LogLevel.DATA)) {
                    log.error(String.format("VideoStateChanged - onError - filename: %s - onError - %s", fileName, error.getMessage()));
                }

                runtime.setMediaFileNameOff(fileName);
                playSelectedMediaFile(getMediaFileList());
            }

            @Override
            public void onStateChanged(String fileName, int index) {
                if (Config.hasLogLevel(LogLevel.DATA))
                    log.info(String.format(Locale.ENGLISH, "VideoStateChanged - onStateChanged - filename: %s - index: %d", fileName, index));

                runtime.setMediaFileNameOff(fileName);

                playSelectedMediaFile(getMediaFileList());
            }
        });

        // setup webview
        webView.setListener(mContext.getActivity(), mOnWebViewListener);

        mHashFileChecker = new HashFileCheckerImpl(runtime);

        faceDetectionFragment = FaceDetectionFragment.newInstance();
        hostFragment(faceDetectionFragment);
        faceDetection = new FaceDetectionImpl(runtime);
    }

    public AsyncTaskManager getTaskManager() {
        return taskManager;
    }

    /**
     * Check updated layout.
     */
    public void getCurrentPlaylist() {
        if (NetworkUtils.checkInternetConnection()) {
            homeController.getCurrentPlaylist(runtime.getAccountUserName());
        }
    }

    public void submitFaceInfo() {
        if (NetworkUtils.checkInternetConnection()) {
            ((MainActivity) mContext.getActivity()).submitFaceInfo();
        }
    }

    public void playDefaultVideo(int index) {
        // reset current media index
        mCurrentMediaIndex = index;
        final List<String> links = getMediaFileList();
        playDefaultAdvertiseMedia(links);
    }

    private void playDefaultAdvertiseMedia(final List<String> lists) {
        if (lists == null || lists.isEmpty()) {
            // play default video
            final List<String> l = new ArrayList<>();
            l.add(homeController.getVideoAdvLocal());
            playSelectedMediaFile(l);
        } else {
            // play video list
            playSelectedMediaFile(lists);
        }
    }

    private void playMedia0(int currentUrlIndex) {
        final String url = homeController.getVideoAdvLocal();
        // Cache file name
        final String fileName = url.substring(url.lastIndexOf('/') + 1);
        final String exp = url.substring(url.lastIndexOf('.') + 1);

        if (Arrays.asList(EXP_IMAGES_FILES).contains(exp.toUpperCase()))
            playImageMedia(url, fileName, currentUrlIndex); // play image file
        else if (Arrays.asList(EXP_WEBVIEW_FILES).contains(exp.toUpperCase()))
            playWebViewMedia(url, fileName, currentUrlIndex); // play webview url
        else
            playVideoMedia(url, fileName, currentUrlIndex); // play video file
    }

    private void playSelectedMediaFile(List<String> lists) {
        List<String> mMediaList = new ArrayList<>();

        taskManager.showDialog(false); // hide all message

        if (!mMediaList.isEmpty())
            mMediaList.clear();

        mMediaList.addAll(lists);

        if (!mMediaList.isEmpty() && mMediaList.size() > mCurrentMediaIndex) {
            final String url = mMediaList.get(mCurrentMediaIndex);
            final int currentUrlIndex = mCurrentMediaIndex;

            // update ui
            if (Config.DEBUG_MODE) {
                txtDebug.setText(String.format(Locale.ENGLISH,
                        "Playing media file at index: %d - %s",
                        currentUrlIndex,
                        new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.ENGLISH)
                                .format(new Date(System.currentTimeMillis()))));
            }

            // Cache file name
            final String fileName = url.substring(url.lastIndexOf('/') + 1);
            final String exp = url.substring(url.lastIndexOf('.') + 1);

            // reset image duration
            mCurrentImageStartTimeInMillis = 0;

            if (runtime.getLayoutResponse() != null
                    && runtime.getLayoutResponse().getLayouts() != null
                    && !runtime.getLayoutResponse().getLayouts().isEmpty())
                runtime.setCurrentLayout(runtime.getLayoutResponse().getLayouts().get(mCurrentMediaIndex));

            boolean isValidHashFile = mHashFileChecker.checkInvalidAndRemoveFile(url);

            if (isValidHashFile) {
                // start capture face detection
                faceDetection.onStartFaceDetection(runtime.getCurrentLayout());

                if (Arrays.asList(EXP_IMAGES_FILES).contains(exp.toUpperCase())) {
                    playImageMedia(url, fileName, currentUrlIndex); // play image file
                } else if (Arrays.asList(EXP_WEBVIEW_FILES).contains(exp.toUpperCase())) {
                    playWebViewMedia(url, fileName, currentUrlIndex); // play webview url
                } else {
                    playVideoMedia(url, fileName, currentUrlIndex); // play video file
                }

                // increase current media index
                mCurrentMediaIndex++;
                // if current media index is equals to list size, then reset it to 0
                mCurrentMediaIndex = mCurrentMediaIndex % mMediaList.size();
            } else {
                playDefaultVideo(mCurrentMediaIndex);
                // verify download video
                getCurrentPlaylist();
            }
        } else {
            playMedia0(mCurrentMediaIndex);
        }
    }

    private void playVideoMedia(String url, final String fileName, int urlIndex) {
        runtime.setMediaFileNameOn(fileName);

        mPlayer.playWithExoPlayer(url, fileName, urlIndex);

        updateMediaVisibility(MediaType.VIDEO);
    }

    private void playImageMedia(String url, String fileName, int urlIndex) {
        final Bitmap bmp = BitmapFactory.decodeFile(url);
        imageView.setImageBitmap(bmp);

        runtime.setMediaFileNameOn(fileName);

        updateMediaVisibility(MediaType.IMAGE);

        long imageDuration = getImageDurationFromIndex(urlIndex);
        // setup start time of the image
        mCurrentImageStartTimeInMillis = System.currentTimeMillis();

        // destroy current count down timer
        if (mCountDownTimer != null && mCountDownTimer.isRunning()) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new ImageCountDownTimer(imageDuration, 1000, fileName);
        mCountDownTimer.start();
    }

    private void playWebViewMedia(String url, String fileName, int urlIndex) {
        webView.loadUrl(url);

        runtime.setMediaFileNameOn(fileName);

        updateMediaVisibility(MediaType.WEB_VIEW);

        long imageDuration = getImageDurationFromIndex(urlIndex);
        // setup start time of the image
        mCurrentImageStartTimeInMillis = System.currentTimeMillis();

        // destroy current count down timer
        if (mCountDownTimer != null && mCountDownTimer.isRunning()) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new ImageCountDownTimer(imageDuration, 1000, fileName);
        mCountDownTimer.start();
    }

    private void deleteRedundantFiles(List<LayoutInfo> lists) {
        try {
            final List<String> files = getListName();
            final List<String> deleteFiles = new ArrayList<>();
            final List<String> listSrc = converterObjectToString(lists);
            for (String f : files) {
                if (!listSrc.contains(f)) {
                    deleteFiles.add(f);
                }
            }

            //  call delete on SDCard
            if (!deleteFiles.isEmpty()) {
                for (String src : deleteFiles) {
                    FileUtils.deleteFileInPath(src, runtime.getFolderVideoPath());
                    if (Config.hasLogLevel(LogLevel.DATA))
                        log.debug("Starting delete file :" + src);
                }

                playDefaultVideo(0);
                // Start service, then download finished
                ((MainActivity) mContext.getActivity()).onStartService();
            }
        } catch (Exception e) {
            if (Config.hasLogLevel(LogLevel.DATA))
                log.error("deletedFileNotUse", e);
        }
    }

    private void startMultiDownload(final List<LayoutInfo> lists) {
        if (!taskManager.isWorking()) {
            if (Config.hasLogLevel(LogLevel.DATA))
                log.info("preparing download files...");
            // Stop service when server change group or delete and add new video
            ((MainActivity) mContext.getActivity()).onStopService();
            // Start download
            taskManager.setupTask(new MediaDownloadTask(lists, runtime.getApiUrl(), runtime.getFolderVideoPath()));
            taskManager.showDialog(runtime.getPlaylistProgressStart());
        }
    }

    private List<String> getMediaFileList() {

        final File[] videos = getListOfSdCardFiles();

        if (videos == null) {
            return new ArrayList<>();
        }
        final String[] links = new String[videos.length];
        for (int i = 0; i < videos.length; i++) {
            final File f = videos[i];
            links[i] = f.getAbsolutePath();
        }
        final List<String> listLinks = new ArrayList<>();
        final LayoutResponse response = runtime.getLayoutResponse();
        if (response != null && !response.getLayouts().isEmpty()) {
            final List<LayoutInfo> listLayouts = response.getLayouts();
            for (LayoutInfo l : listLayouts) {
                for (String s : links) {
                    if (s.contains(l.getAssets())) {
                        listLinks.add(s);
                    }
                }
            }
        }
        return listLinks;
    }

    private List<String> getListName() {

        final File[] videos = getListOfSdCardFiles();

        if (videos == null) {
            return new ArrayList<>();
        }
        final String[] links = new String[videos.length];
        for (int i = 0; i < videos.length; i++) {
            File f = videos[i];
            links[i] = f.getName();
        }
        return Arrays.asList(links);
    }

    private File[] getListOfSdCardFiles() {
        final File file = new File(String.format(Config.OverallConfig.FOLDER_PATH, runtime.getFolderVideoPath()));

        return file.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                final String exp = fileName.substring(fileName.lastIndexOf('.') + 1);
                return Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase());
            }
        });
    }

    private List<String> converterObjectToString(List<LayoutInfo> lists) {
        final List<String> files = new ArrayList<>();

        for (LayoutInfo info : lists) {
            final String exp = info.getAssets().substring(info.getAssets().lastIndexOf('.') + 1);
            if (Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                files.add(info.getAssets());
            }
        }
        return files;
    }

    private List<LayoutInfo> filterDownloadFile(List<LayoutInfo> lists) {
        final List<LayoutInfo> listDownload = new ArrayList<>();

        for (LayoutInfo info : lists) {
            final String fileName = info.getAssets();
            final String exp = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                listDownload.add(info);
            }
        }
        return listDownload;
    }

    private long getImageDurationFromIndex(final int index) {
        long duration;
        if (runtime.getLayoutResponse() == null
                || runtime.getLayoutResponse().getLayouts() == null
                || runtime.getLayoutResponse().getLayouts().isEmpty())
            return Constants.IMAGE_DURATION;
        else
            duration = runtime.getLayoutResponse().getLayouts().get(index).getDuration() * Constants.ONE_SECOND;
        return duration;
    }

    private void updateMediaVisibility(final @MediaType int mediaTypeVisibility) {
        switch (mediaTypeVisibility) {
            case MediaType.IMAGE:
                imageView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.INVISIBLE);
                webView.clearCache(true);
                exoVideoView.setVisibility(View.INVISIBLE);
                break;
            case MediaType.WEB_VIEW:
                imageView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.VISIBLE);
                exoVideoView.setVisibility(View.INVISIBLE);
                break;
            case MediaType.VIDEO:
            default:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setVisibility(View.INVISIBLE);
                        webView.setVisibility(View.INVISIBLE);
                        webView.clearCache(true);
                        exoVideoView.setVisibility(View.VISIBLE);
                    }
                }, 300L);
                break;
        }
    }

    public void checkAutoPlay() {
        if (runtime.getRegisterInfo() != null) {
            homeController.getAutoPlay(runtime.getRegisterInfo().getId());
        }
    }

    void postInfoToServer() {
        if (runtime.getRegisterInfo() != null && runtime.getAccountUserName() != null) {

            // get current position of playing file
            String positionOfCurrentPlayingFile = getPositionOfCurrentPlayingMediaFile();

            // calculate current position time for the playing file in current screen
            long currentTimePosition;
            if (mCurrentImageStartTimeInMillis > 0) {
                // current playing image position
                currentTimePosition = (System.currentTimeMillis() - mCurrentImageStartTimeInMillis) / Constants.ONE_SECOND;
            } else {
                // current playing video position
                currentTimePosition = 0;
            }

            String info = String.format(Locale.ENGLISH,
                    "%s - position: %s - username: %s - media name: %s - file: %s - playing at duration: %d",
                    DateUtils.currentFormattedDate(),
                    positionOfCurrentPlayingFile,
                    runtime.getAccountUserName(),
                    runtime.getCurrentLayout() != null ? runtime.getCurrentLayout().getName() : "",
                    runtime.getMediaFileNameOn(),
                    currentTimePosition);

            Log.i(TAG, info);

            if (NetworkUtils.checkInternetConnection()) {
                homeController.doCheck(positionOfCurrentPlayingFile,
                        runtime.getAccountUserName(),
                        runtime.getMediaFileNameOn(),
                        String.valueOf(currentTimePosition),
                        String.valueOf(Runtime.getRuntime().freeMemory()),
                        String.valueOf(Runtime.getRuntime().totalMemory()),
                        NetworkUtils.getLocalIpAddress());

                // set time off is current time
                runtime.setMediaTimeOff(System.currentTimeMillis());

                ((MainActivity) mContext.getActivity()).postHistoryToServer();
            }
        }
    }

    private String getPositionOfCurrentPlayingMediaFile() {
        String position = "";
        if (runtime.getCurrentLayout() != null) {
            position = String.valueOf(runtime.getCurrentLayout().getPosition());
        }
        return position;
    }

    void checkDownloadVideo(final LayoutResponse newResponse) {
        try {
            // get list file from SDCard
            final List<String> filesOnSdCard = getListName();

            // Created temp list
            final List<LayoutInfo> listDownload = new ArrayList<>();

            // if there is no downloaded file, we're starting to download the new file
            if (filesOnSdCard == null || filesOnSdCard.isEmpty()) {
                listDownload.addAll(newResponse.getLayouts());
                final List<LayoutInfo> layouts = filterDownloadFile(newResponse.getLayouts());
                runtime.setPlaylistProgressStart(true);

                if (layouts != null && !layouts.isEmpty()) {
                    if (Config.hasLogLevel(LogLevel.DATA)) {
                        log.info(String.format(Locale.ENGLISH,
                                "Start download media: total %d files.", listDownload.size()));
                        log.debug("Starting download ...");
                    }

                    // Start download file
                    startMultiDownload(layouts);
                }

            } else {
                runtime.setPlaylistProgressStart(false);

                // check cache and response from server
                for (LayoutInfo info : newResponse.getLayouts()) {
                    final String fileName = info.getAssets();
                    final String exp = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if (!filesOnSdCard.contains(fileName)
                            && Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                        listDownload.add(info);
                    }
                }

                if (!listDownload.isEmpty()) {
                    if (Config.hasLogLevel(LogLevel.DATA)) {
                        log.info(String.format(Locale.ENGLISH, "Start download media: total %d files.", listDownload.size()));
                        log.debug("Starting download ...");
                    }

                    // Start download file
                    startMultiDownload(listDownload);
                } else {
                    // play video file

                    // Delete file then user change file
                    deleteRedundantFiles(newResponse.getLayouts());
                }
            }
        } catch (Exception e) {
            if (Config.hasLogLevel(LogLevel.DATA))
                log.error(TAG, e);
        }
    }

    private void updateCurrentLayoutToRuntime(String fileName) {
        List<LayoutInfo> ls = runtime.getLayoutResponse().getLayouts();

        for (int i = 0; i < ls.size(); i++) {
            if (i >= mCurrentMediaIndex) {
                LayoutInfo lInfo = ls.get(i);
                if (lInfo.getAssets().equalsIgnoreCase(fileName)) {
                    runtime.setCurrentLayout(lInfo);
                }
            }
        }
    }

    private class ImageCountDownTimer extends CountDownTimer {

        private final String fileName;
        private boolean mIsRunning;

        ImageCountDownTimer(long millisInFuture, long countDownInterval, String fileName) {
            super(millisInFuture, countDownInterval);
            this.fileName = fileName;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mIsRunning = true;
        }

        @Override
        public void onFinish() {
            try {
                mIsRunning = false;
                runtime.setMediaFileNameOff(fileName);
            } catch (Exception e) {
                if (Config.hasLogLevel(LogLevel.DATA))
                    log.error("Error start video by list", e);
            } finally {
                playDefaultAdvertiseMedia(getMediaFileList());
            }
        }

        boolean isRunning() {
            return mIsRunning;
        }
    }

    protected void hostFragment(BaseFaceDetectionFragment fragment) {
        if (fragment != null && mContext.getActivity().getFragmentManager().findFragmentByTag(fragment.getTagName()) == null) {
            FragmentTransaction ft = mContext.getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment.getInstance(), fragment.getTagFromClassName());
            ft.commit();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        faceDetectionFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
