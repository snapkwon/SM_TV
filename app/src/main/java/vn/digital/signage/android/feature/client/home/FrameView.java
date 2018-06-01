package vn.digital.signage.android.feature.client.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.apache.log4j.Logger;

import java.util.Arrays;

import im.delight.android.webview.AdvancedWebView;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.media.IjkVideoView;
import vn.digital.signage.android.utils.NetworkUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.enumeration.MediaType;
import vn.digital.signage.android.utils.hash.HashFileChecker;

public class FrameView extends FrameLayout {

    private static final String TAG = FrameView.class.getSimpleName();
    private static final String[] EXP_IMAGES_FILES = new String[]{"JPG", "JPEG", "PNG"};
    private static final String[] EXP_WEBVIEW_FILES = new String[]{"HTML", "HTM", "ASPX"};

    private final Logger log = Logger.getLogger(FrameView.class);
    IjkVideoView exoVideoView;
    ImageView imageView;
    AdvancedWebView webView;

    private Handler mHandler = new Handler();
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

    public FrameView(Context context) {
        super(context);
        initViewsForChild(context);
    }

    public FrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initViewsForChild(Context context) {
//        mContext = context;
        // setup videoview
        exoVideoView = new IjkVideoView(context);
//        exoVideoView.setVisibility(INVISIBLE);

        // setup webview
        webView = new AdvancedWebView(context);
        webView.setListener((Activity)context, mOnWebViewListener);

        imageView = new ImageView(context);

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

        exoVideoView.setVideoPath(url);
        exoVideoView.start();

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
                exoVideoView.stopPlayback();
                exoVideoView.release(true);
                exoVideoView.stopBackgroundPlay();
                break;
        }
        removeAllViews();
    }

}
