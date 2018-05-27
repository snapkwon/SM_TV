package vn.digital.signage.android.feature.client.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Locale;

import im.delight.android.webview.AdvancedWebView;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.base.BaseFragment;
import vn.digital.signage.android.utils.NetworkUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.enumeration.MediaType;
import vn.digital.signage.android.utils.hash.HashFileChecker;
import vn.digital.signage.android.utils.player.ExoPlayerImpl;
import vn.digital.signage.android.utils.player.IPlayer;
import vn.digital.signage.android.utils.player.VideoStateChanged;

public class FrameView extends RelativeLayout {

    private static final String TAG = FrameView.class.getSimpleName();
    private static final String[] EXP_IMAGES_FILES = new String[]{"JPG", "JPEG", "PNG"};
    private static final String[] EXP_WEBVIEW_FILES = new String[]{"HTML", "HTM", "ASPX"};

    private final Logger log = Logger.getLogger(FrameView.class);
    SimpleExoPlayerView exoVideoView;
    ImageView imageView;
    AdvancedWebView webView;

    private BaseFragment mContext;
    private Handler mHandler = new Handler();
    private IPlayer mPlayer;
    private HashFileChecker mHashFileChecker;
    private int mediaTypeVisibility;

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

    public FrameView(Context context, BaseFragment baseFragment) {
        super(context);
        initViewsForChild(baseFragment);
    }

    public FrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initViewsForChild(BaseFragment context) {
        mContext = context;
        // setup videoview
        exoVideoView = new SimpleExoPlayerView(context.getActivity());
        exoVideoView.setVisibility(INVISIBLE);
        mPlayer = new ExoPlayerImpl(mContext.getActivity(), exoVideoView, new VideoStateChanged() {
            @Override
            public void onError(String fileName, Exception error) {
                if (Config.hasLogLevel(LogLevel.DATA)) {
                    log.error(String.format("VideoStateChanged - onError - filename: %s - onError - %s", fileName, error.getMessage()));
                }

//                runtime.setMediaFileNameOff(fileName);
//                playSelectedMediaFile(getMediaFileList());
            }

            @Override
            public void onStateChanged(String fileName, int index) {
                if (Config.hasLogLevel(LogLevel.DATA))
                    log.info(String.format(Locale.ENGLISH, "VideoStateChanged - onStateChanged - filename: %s - index: %d", fileName, index));

//                runtime.setMediaFileNameOff(fileName);

//                playSelectedMediaFile(getMediaFileList());
            }
        });

        // setup webview
        webView = new AdvancedWebView(mContext.getActivity());
        webView.setListener(mContext.getActivity(), mOnWebViewListener);

        imageView = new ImageView(mContext.getActivity());

//        faceDetectionFragment = FaceDetectionFragment.newInstance();
//        hostFragment(faceDetectionFragment);
//        faceDetection = new FaceDetectionImpl(runtime);
    }

    public void playMedia0(SourceInfo sourceInfo) {
        // Cache file name
        String url = sourceInfo.getSource();
        final String fileName = url.substring(url.lastIndexOf('/') + 1);
        final String exp = url.substring(url.lastIndexOf('.') + 1);

        if (Arrays.asList(EXP_IMAGES_FILES).contains(exp.toUpperCase())) {
            if (getChildCount() == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addView(imageView, params);
            }
            playImageMedia(url, fileName, 0); // play image file
        } else if (Arrays.asList(EXP_WEBVIEW_FILES).contains(exp.toUpperCase())) {
            if(NetworkUtils.checkInternetConnection()) {
                if (getChildCount() == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    addView(webView, params);
                    playWebViewMedia(url, fileName, 0); // play webview url
                }
            }else {
                if (getChildCount() == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    addView(exoVideoView, params);
                }
                playVideoMedia(HomeController.DEFAULT_VIDEO_PATH, fileName, 0); // play video file
            }
        } else {
            if (getChildCount() == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addView(exoVideoView, params);
            }
            playVideoMedia(url, fileName, 0); // play video file
        }
    }

    private void playVideoMedia(String url, final String fileName, int urlIndex) {

        mPlayer.playWithExoPlayer(url, fileName, urlIndex);

        updateMediaVisibility(MediaType.VIDEO);
    }

    private void playImageMedia(String url, String fileName, int urlIndex) {
        final Bitmap bmp = BitmapFactory.decodeFile(url);
        imageView.setImageBitmap(bmp);

        updateMediaVisibility(MediaType.IMAGE);

    }

    private void playWebViewMedia(String url, String fileName, int urlIndex) {
        webView.loadUrl(url);

        updateMediaVisibility(MediaType.WEB_VIEW);
    }

    private void updateMediaVisibility(final @MediaType int mediaTypeVisibility) {
        this.mediaTypeVisibility = mediaTypeVisibility;
        switch (mediaTypeVisibility) {
            case MediaType.IMAGE:
                imageView.setVisibility(VISIBLE);
                break;
            case MediaType.WEB_VIEW:
                break;
            case MediaType.VIDEO:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exoVideoView.setVisibility(VISIBLE);
                    }
                }, 300);
            default:

                break;
        }
    }

    public void onDestroy() {
        switch (mediaTypeVisibility) {
            case MediaType.IMAGE:
                break;
            case MediaType.WEB_VIEW:
                webView.clearCache(true);
                break;
            case MediaType.VIDEO:
            default:
                mPlayer.stopExoPlayer();
                break;
        }
        removeAllViews();
    }

}
