package vn.digital.signage.android.feature.client.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.InjectView;
import im.delight.android.webview.AdvancedWebView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import vn.digital.signage.android.Constants;
import vn.digital.signage.android.R;
import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.api.response.LayoutResponse;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.FrameActivity;
import vn.digital.signage.android.feature.client.base.MainActivity;
import vn.digital.signage.android.feature.client.home.face.BaseFaceDetectionFragment;
import vn.digital.signage.android.media.IjkVideoView;
import vn.digital.signage.android.utils.DateUtils;
import vn.digital.signage.android.utils.DebugLog;
import vn.digital.signage.android.utils.FileUtils;
import vn.digital.signage.android.utils.NetworkUtils;
import vn.digital.signage.android.utils.asynctask.AsyncTaskManager;
import vn.digital.signage.android.utils.asynctask.MediaDownloadTask;
import vn.digital.signage.android.utils.asynctask.OnTaskCompleteListener;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.enumeration.MediaType;
import vn.digital.signage.android.utils.hash.HashFileChecker;
import vn.digital.signage.android.utils.hash.HashFileCheckerImpl;

public class HomeScreenView {

    private static final String TAG = HomeScreenView.class.getSimpleName();
    private static final String[] SUPPORT_FILES = new String[]{"MP4", "MP3", "JPG", "JPEG", "PNG", "HTML"};
    private static final String[] EXP_IMAGES_FILES = new String[]{"JPG", "JPEG", "PNG"};
    private static final String[] EXP_WEBVIEW_FILES = new String[]{"HTML", "HTM", "ASPX"};

    private final Logger log = Logger.getLogger(HomeScreenView.class);
    public static final String KEY_URL = "KEY_URL";
    @Inject
    HomeController homeController;
    @Inject
    SMRuntime runtime;
    @InjectView(R.id.fragment_home_adv_video_player)
    IjkVideoView exoVideoView;
    @InjectView(R.id.fragment_home_adv_image)
    ImageView imageView;
    @InjectView(R.id.fragment_home_adv_webview)
    AdvancedWebView webView;
    @InjectView(R.id.fragment_home_adv_frameview)
    RelativeLayout frameView;
    @InjectView(R.id.fragment_home_adv_debug)
    TextView txtDebug;

    private List<String> mCurrentMediaList = new ArrayList<>();

//    private FaceDetectionFragment faceDetectionFragment;

    private int mCurrentMediaIndex = 0;
    private String mCurrentUrl = "";
    private LayoutResponse mCurrentResponse;

    public LayoutResponse getCurrentResponse() {
        return mCurrentResponse;
    }

    public void setCurrentResponse(LayoutResponse mCurrentResponse) {
        this.mCurrentResponse = mCurrentResponse;
    }

    public void setmCurrentMediaIndex(int mCurrentMediaIndex) {
        this.mCurrentMediaIndex = mCurrentMediaIndex;
        if (mCurrentMediaIndex == 0)
            DebugLog.d("current media index " + mCurrentMediaIndex);
    }

    private long mCurrentImageStartTimeInMillis = 0;
    private BaseCountDownTimer mCountDownTimer;
    //    private FrameCountDownTimer mFrameTimer;
    private HomeFragment mContext;
    private AsyncTaskManager taskManager;
    private Handler mHandler = new Handler();
    //    private IPlayer mPlayer;
    private HashFileChecker mHashFileChecker;
    //    private IFaceDetection faceDetection;
    private OnTaskCompleteListener mOnTaskCompleteListener = new OnTaskCompleteListener() {
        @Override
        public void onTaskComplete(MediaDownloadTask task) {
            if (task != null) {
                if (task.isCancelled()) {
//                    if (Config.hasLogLevel(LogLevel.DATA))
                    DebugLog.d("The download cancelled ...");
                    // Report about cancel
                    displayMessage("The download process has been ignored.");
                } else {
//                    if (Config.hasLogLevel(LogLevel.DATA))
                    DebugLog.d("The download finished.");

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

        if (mCurrentResponse == null) {
            setCurrentResponse(runtime.getLayoutResponse());
        }
        // Create manager and set this activity as context and listener
        taskManager = new AsyncTaskManager(mContext.getActivity(), mOnTaskCompleteListener);
        // Handle task that can be retained before
        taskManager.handleRetainedTask((mContext.getActivity()).getLastCustomNonConfigurationInstance());
        // setup videoview
        exoVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                runtime.setMediaFileNameOff(iMediaPlayer.getDataSource());
                playDefaultAdvertiseMedia(getMediaFileList());
            }
        });
        exoVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                runtime.setMediaFileNameOff(iMediaPlayer.getDataSource());
                playDefaultAdvertiseMedia(getMediaFileList());
                return false;
            }
        });
//        mPlayer = new ExoPlayerImpl(mContext.getActivity(), exoVideoView, new VideoStateChanged() {
//            @Override
//            public void onError(String fileName, Exception error) {
//                if (Config.hasLogLevel(LogLevel.DATA)) {
//                    log.error(String.format("VideoStateChanged - onError - filename: %s - onError - %s", fileName, error.getMessage()));
//                }
//
//                runtime.setMediaFileNameOff(fileName);
//                playSelectedMediaFile(getMediaFileList());
//            }
//
//            @Override
//            public void onStateChanged(String fileName, int index) {
//                if (Config.hasLogLevel(LogLevel.DATA))
//                    log.info(String.format(Locale.ENGLISH, "VideoStateChanged - onStateChanged - filename: %s - index: %d", fileName, index));
//
//                runtime.setMediaFileNameOff(fileName);
//
//                playSelectedMediaFile(getMediaFileList());
//            }
//        });

        // setup webview
        webView.setListener(mContext.getActivity(), mOnWebViewListener);

        mHashFileChecker = new HashFileCheckerImpl(runtime);

//        faceDetectionFragment = FaceDetectionFragment.newInstance();
//        hostFragment(faceDetectionFragment);
//        faceDetection = new FaceDetectionImpl(runtime);
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
        setmCurrentMediaIndex(index);
        playDefaultAdvertiseMedia(getMediaFileList());
    }

    private boolean isValidLayout(List<String> lists) {
        LayoutResponse response = getCurrentResponse();
        if (lists.size() != response.getLayouts().size())
            return false;

        for (int i = 0; i < response.getLayouts().size(); i++) {
            LayoutInfo layoutInfo = response.getLayouts().get(i);
            if (layoutInfo.getType() == LayoutInfo.LayoutType.VIDEO || layoutInfo.getType() == LayoutInfo.LayoutType.IMAGE) {
                if (!lists.get(i).contains(layoutInfo.getAssets()) || !checkInvalidAndRemoveFile(lists.get(i), layoutInfo.getHash()))
                    return false;
            } else if (layoutInfo.getType() == LayoutInfo.LayoutType.FRAME) {

                final File[] videos = getListOfSdCardFiles();

                if (videos == null)
                    return false;
                String[] links = new String[videos.length];
                for (int j = 0; j < videos.length; j++) {
                    final File f = videos[j];
                    links[j] = f.getAbsolutePath();
                }

                if (!isFrameHasData(links, layoutInfo))
                    return false;
            }
        }

        runtime.setLayoutResponse(getCurrentResponse());
        return true;
    }

    public boolean checkInvalidAndRemoveFile(String url, String hash) {
//        boolean result = true;
        return (runtime.getMediaDownloaded().contains(FileUtils.getFileNameFromPath(url)));
        //DebugLog.d("check hash :" + url + " - url MD5: " + hash);
//        if (Constants.IS_HASH_CHECK_ENABLED) {
//
//            if (!TextUtils.isEmpty(hash)
//                    && !hash.equalsIgnoreCase(HashUtils.fileToMD5(url))) {
////                FileUtils.deleteFileInPath(url);
//                DebugLog.d("check hash2 :" + url + " - url MD5: " + HashUtils.fileToMD5(url));
//                if (Co    nfig.hasLogLevel(LogLevel.DATA))
//                    DebugLog.d("deleted file :" + url + " - url MD5: " + hash);
//                result = false;
//            }
//        }
//        return result;
    }

    private void playDefaultAdvertiseMedia(final List<String> lists) {
        DebugLog.d("test download media " + new Gson().toJson(lists));

        if (lists == null || lists.isEmpty()) {
            // play default video
            if (!TextUtils.isEmpty(homeController.getVideoAdvLocal())) {
                final List<String> l = new ArrayList<>();
                l.add(homeController.getVideoAdvLocal());
                mCurrentMediaList = l;
                playSelectedMediaFile(l);
            }
        } else {
            if (!isValidLayout(lists)) {
                if (mCurrentMediaList != null && !mCurrentMediaList.isEmpty())
                    playSelectedMediaFile(mCurrentMediaList);
                else playMedia0(0);

                startMultiDownload(getCurrentResponse().getLayouts());
            } else {
                // play video list
                DebugLog.d("valid layout");
                mCurrentMediaList = lists;
                playSelectedMediaFile(lists);
            }
        }
    }

    private void playMedia0(int currentUrlIndex) {
        final String url = homeController.getVideoAdvLocal();
        if (!TextUtils.isEmpty(url)) {
            // Cache file name
            final String fileName = FileUtils.getFileNameFromPath(url);
            final String exp = url.substring(url.lastIndexOf('.') + 1);

            if (Arrays.asList(EXP_IMAGES_FILES).contains(exp.toUpperCase()))
                playImageMedia(url, fileName, currentUrlIndex); // play image file
            else if (Arrays.asList(EXP_WEBVIEW_FILES).contains(exp.toUpperCase()))
                playWebViewMedia(url, fileName, currentUrlIndex); // play webview url
            else
                playVideoMedia(url, fileName, currentUrlIndex); // play video file
        }
    }

    private void playSelectedFrame(List<SourceInfo> lists) {
        for (SourceInfo info : lists) {
//            DebugLog.d(info.getSource() + "|");
            String url = "";
//            if (info.getType() == SourceInfo.SourceType.VIDEO) {
//                url = getMediaFile(info.getSource());
//            } else if (info.getType() == SourceInfo.SourceType.IMAGE) {
//                url = getMediaFile(info.getSource());
//            } else if (info.getType() == SourceInfo.SourceType.WEB) {
//                url = getMediaFile(info.getSource());
//            } else
            if (info.getType() == SourceInfo.SourceType.VIDEO_LIST) {
                for (int i = 0; i < info.getArrSources().size(); i++) {
                    String source = info.getArrSources().get(i);
                    source = getMediaFile(source);
                    info.getArrSources().set(i, source);
                }
            }
//            else if (info.getType() == SourceInfo.SourceType.WEB)
//                url = getMediaFile(info.getSource());
            else if (info.getType() != SourceInfo.SourceType.URL)
                url = getMediaFile(info.getSource());
            else
                url = info.getSource();
            info.setSource(url);
        }
        Intent intent = new Intent(HomeFragment.mActivity, FrameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("source", (Serializable) lists);
        intent.putExtra("duration", getImageDurationFromIndex(mCurrentMediaIndex));
        updateMediaVisibility(MediaType.FRAME);
        HomeFragment.mActivity.startActivityForResult(intent, 100);
    }


    private void playSelectedMediaFile(List<String> lists) {
        if (lists == null || lists.isEmpty())
            return;

        List<String> mMediaList = new ArrayList<>();

        taskManager.showDialog(false); // hide all message

        if (!mMediaList.isEmpty())
            mMediaList.clear();

        mMediaList.addAll(lists);
        try {
//            DebugLog.d(new Gson().toJson(mMediaList) + "|" + mCurrentUrl);
//            if (mMediaList.size() > 1 && mCurrentUrl.equals(mMediaList.get(mCurrentMediaIndex)))
//                return;// avoid play multiple times

//            DebugLog.d("media list " + new Gson().toJson(mMediaList));

            DebugLog.d("on play index " + mCurrentMediaIndex + " |" + mMediaList.get(mCurrentMediaIndex));
        } catch (Exception e) {
            e.printStackTrace();
            setmCurrentMediaIndex((mCurrentMediaIndex + 1) % mMediaList.size());
            return;
        }

        final LayoutResponse response = runtime.getLayoutResponse();

        if (!mMediaList.isEmpty() && mMediaList.size() > mCurrentMediaIndex) {

            if (runtime.getLayoutResponse() != null
                    && runtime.getLayoutResponse().getLayouts() != null
                    && !runtime.getLayoutResponse().getLayouts().isEmpty())
                runtime.setCurrentLayout(runtime.getLayoutResponse().getLayouts().get(mCurrentMediaIndex));

            if (response != null && !response.getLayouts().isEmpty()) {
                final List<LayoutInfo> listLayouts = response.getLayouts();
                if ("FRAME".equals(mMediaList.get(mCurrentMediaIndex))) {
                    mCurrentUrl = mMediaList.get(mCurrentMediaIndex);
                    playSelectedFrame(listLayouts.get(mCurrentMediaIndex).getObjSource());

                    setmCurrentMediaIndex((mCurrentMediaIndex + 1) % mMediaList.size());
                    return;
                }
            }
//            DebugLog.d("on play media " + mCurrentMediaIndex + " |" + mMediaList.get(mCurrentMediaIndex));
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


            boolean isValidHashFile = mHashFileChecker.checkInvalidAndRemoveFile(url);

            if (isValidHashFile) {
                mCurrentUrl = mMediaList.get(mCurrentMediaIndex);
                // start capture face detection
//                faceDetection.onStartFaceDetection(runtime.getCurrentLayout());

                if (Arrays.asList(EXP_IMAGES_FILES).contains(exp.toUpperCase())) {
                    playImageMedia(url, fileName, currentUrlIndex); // play image file
                } else if (Arrays.asList(EXP_WEBVIEW_FILES).contains(exp.toUpperCase())) {
                    playWebViewMedia(url, fileName, currentUrlIndex); // play webview url
                } else {
                    playVideoMedia(url, fileName, currentUrlIndex); // play video file
                }


                setmCurrentMediaIndex((mCurrentMediaIndex + 1) % mMediaList.size());
            } else {
                playDefaultVideo(mCurrentMediaIndex);
                // verify download video
                DebugLog.d("some error cases");
                getCurrentPlaylist();
            }
        } else {
            setmCurrentMediaIndex((mCurrentMediaIndex + 1) % mMediaList.size());
            playMedia0(mCurrentMediaIndex);
        }
    }

    private void playVideoMedia(String url, final String fileName, int urlIndex) {
        exoVideoView.stopPlayback();
        exoVideoView.release(true);
        exoVideoView.stopBackgroundPlay();

        runtime.setMediaFileNameOn(fileName);

        exoVideoView.setVideoPath(url);
        exoVideoView.start();
//        mPlayer.playWithExoPlayer(url, fileName, urlIndex);

        updateMediaVisibility(MediaType.VIDEO);

        // destroy current count down timer
        if (mCountDownTimer != null && mCountDownTimer.isRunning()) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
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
//            DebugLog.d("media redundant  " + new Gson().toJson(lists));
            final List<String> files = getListName();
            final List<String> deleteFiles = new ArrayList<>();
            final List<String> listSrc = converterObjectToString(lists);
            for (String f : files) {
                if (!listSrc.contains(f)) {
                    deleteFiles.add(f);
                }
            }

            DebugLog.d("media delete  " + new Gson().toJson(deleteFiles));
            //  call delete on SDCard
            if (!deleteFiles.isEmpty()) {
                for (String src : deleteFiles) {
                    runtime.removeMediaDownloaded(FileUtils.getFileNameFromPath(src));
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
            taskManager.setupTask(new MediaDownloadTask(lists, runtime));
            taskManager.showDialog(runtime.getPlaylistProgressStart());
        }
    }

    private String getMediaFile(String source) {
        final File[] videos = getListOfSdCardFiles();

        if (videos == null) {
            return "";
        }
        for (int i = 0; i < videos.length; i++) {
            final File f = videos[i];
            if (f.getAbsolutePath().contains(source))
                return f.getAbsolutePath();
        }
        return "";
    }

    private List<String> getMediaFileList() {

        final File[] videos = getListOfSdCardFiles();

        if (videos == null) {
            return new ArrayList<>();
        }
//        DebugLog.d("Frame" + videos.length);

        final String[] links = new String[videos.length];
        for (int i = 0; i < videos.length; i++) {
            final File f = videos[i];
            links[i] = f.getAbsolutePath();
        }
        final List<String> listLinks = new ArrayList<>();
        final LayoutResponse response = getCurrentResponse();
        if (response != null && !response.getLayouts().isEmpty()) {
            final List<LayoutInfo> listLayouts = response.getLayouts();
            for (LayoutInfo l : listLayouts) {
                if (l.getType() == LayoutInfo.LayoutType.FRAME) {
//                    if (isFrameHasData(links, l))
                    listLinks.add("FRAME");
//                    DebugLog.d("Frame");
                } else
                    for (String s : links) {
//                        DebugLog.d("Frame" + s + "|" + l.getAssets());
                        if (s.contains(l.getAssets())) {
                            listLinks.add(s);
                            break;
                        }
                    }
            }
        }
//        DebugLog.d("media file 11 " + new Gson().toJson(listLinks));
        return listLinks;
    }

    private boolean isFrameHasData(String[] links, LayoutInfo layoutInfo) {
        for (SourceInfo info : layoutInfo.getObjSource()) {
            if (info.getType() == SourceInfo.SourceType.VIDEO_LIST) {
                for (int i = 0; i < info.getArrSources().size(); i++) {
                    String source = info.getArrSources().get(i);
                    String hash = info.getArrHashes().get(i);
                    String url = contains(links, source);
                    if (TextUtils.isEmpty(url) || !checkInvalidAndRemoveFile(url, hash))
                        return false;
                }
            } else if (info.getType() != SourceInfo.SourceType.URL) {
                String url = contains(links, info.getSource());
                if (TextUtils.isEmpty(url) || !checkInvalidAndRemoveFile(url, info.getHash()))
                    return false;
            }
        }
        return true;
    }

    private String contains(String[] links, String source) {
        for (String s : links) {
            if (s.contains(source)) {
                return s;
            }
        }
        return "";
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
//        try {
//            DebugLog.d("media file in folder " + new Gson().toJson(file.listFiles()));
//        } catch (Exception e) {
//        }
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
            if (info.getType() == LayoutInfo.LayoutType.FRAME) {
                for (SourceInfo sourceInfo : info.getObjSource()) {
                    if (sourceInfo.getType() == SourceInfo.SourceType.VIDEO_LIST) {
                        for (String source : sourceInfo.getArrSources()) {
                            String exp = source.substring(source.lastIndexOf('.') + 1);
                            if (Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                                files.add(source);
                            }
                        }
                    } else {
                        final String exp = sourceInfo.getSource().substring(sourceInfo.getSource().lastIndexOf('.') + 1);
                        if (Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                            files.add(sourceInfo.getSource());
                        }
                    }
                }
            } else {
                final String exp = info.getAssets().substring(info.getAssets().lastIndexOf('.') + 1);
                if (Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                    files.add(info.getAssets());
                }
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

    public void updateMediaVisibility(final @MediaType int mediaTypeVisibility) {
        switch (mediaTypeVisibility) {
            case MediaType.IMAGE:
                imageView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.INVISIBLE);
                hideFrameView();
                webView.clearCache(true);
                exoVideoView.setVisibility(View.INVISIBLE);

                exoVideoView.stopPlayback();
                exoVideoView.release(true);
                exoVideoView.stopBackgroundPlay();
                break;
            case MediaType.WEB_VIEW:
                imageView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.VISIBLE);
                hideFrameView();
                exoVideoView.setVisibility(View.INVISIBLE);

                exoVideoView.stopPlayback();
                exoVideoView.release(true);
                exoVideoView.stopBackgroundPlay();
                break;
            case MediaType.FRAME:
                imageView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.INVISIBLE);
                webView.clearCache(true);
                exoVideoView.setVisibility(View.INVISIBLE);

                exoVideoView.stopPlayback();
                exoVideoView.release(true);
                exoVideoView.stopBackgroundPlay();
                frameView.setVisibility(View.VISIBLE);
                break;
            case MediaType.VIDEO:
                imageView.setVisibility(View.INVISIBLE);
                hideFrameView();
                webView.setVisibility(View.INVISIBLE);
                webView.clearCache(true);
                exoVideoView.setVisibility(View.VISIBLE);
                break;
            default:

                imageView.setVisibility(View.INVISIBLE);
                hideFrameView();
                webView.setVisibility(View.INVISIBLE);
                webView.clearCache(true);
                exoVideoView.stopPlayback();
                exoVideoView.release(true);
                exoVideoView.stopBackgroundPlay();

        }
    }

    private void hideFrameView() {
        for (int i = 0; i < frameView.getChildCount(); i++) {
            FrameView view = (FrameView) frameView.getChildAt(i);
            view.onDestroy();
        }
        frameView.removeAllViews();
        frameView.setVisibility(View.INVISIBLE);
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

                    String fileName = "";
                    if (info.getType() == LayoutInfo.LayoutType.FRAME) {
                        for (SourceInfo sourceInfo : info.getObjSource()) {
                            if (sourceInfo.getType() == SourceInfo.SourceType.VIDEO_LIST) {
                                boolean exist = false;
                                for (String source : sourceInfo.getArrSources()) {
                                    String exp = source.substring(source.lastIndexOf('.') + 1);
                                    if (!filesOnSdCard.contains(source)
                                            && Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                                        listDownload.add(info);
                                        exist = true;
                                        break;
                                    }
                                }

                                if (exist)
                                    break;
                            } else {
                                fileName = sourceInfo.getSource();
                                final String exp = fileName.substring(fileName.lastIndexOf('.') + 1);
                                if (!filesOnSdCard.contains(fileName)
                                        && Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                                    listDownload.add(info);
                                    break;
                                }
                            }
                        }
                    } else {
                        fileName = info.getAssets();
                        final String exp = fileName.substring(fileName.lastIndexOf('.') + 1);
                        if (!filesOnSdCard.contains(fileName)
                                && Arrays.asList(SUPPORT_FILES).contains(exp.toUpperCase())) {
                            listDownload.add(info);
                        }
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

    public void onActivityResult() {
        playDefaultAdvertiseMedia(getMediaFileList());
    }

    private class ImageCountDownTimer extends BaseCountDownTimer {

        private final String fileName;

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

    }

    private abstract class BaseCountDownTimer extends CountDownTimer {

        protected boolean mIsRunning;

        public BaseCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
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
//        faceDetectionFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
