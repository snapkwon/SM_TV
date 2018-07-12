package vn.digital.signage.android.feature.client.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.apache.log4j.Logger;

import java.util.List;

import im.delight.android.webview.AdvancedWebView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.media.IjkVideoView;
import vn.digital.signage.android.utils.DebugLog;
import vn.digital.signage.android.utils.enumeration.MediaType;

public class FrameView extends FrameLayout {

    private static final String TAG = FrameView.class.getSimpleName();
    private static final String[] EXP_IMAGES_FILES = new String[]{"JPG", "JPEG", "PNG"};
    private static final String[] EXP_WEBVIEW_FILES = new String[]{"HTML", "HTM", "ASPX"};

    private final Logger log = Logger.getLogger(FrameView.class);
    IjkVideoView exoVideoView;
    ImageView imageView;
    WebView webView;

    private Handler mHandler = new Handler();
    private int mediaTypeVisibility;


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
//        setBackgroundColor(Color.RED);
//        mContext = context;
        // setup videoview
        exoVideoView = new IjkVideoView(context);
//        exoVideoView.setVisibility(INVISIBLE);

        // setup webview
        webView = new AdvancedWebView(context);
//        webView.setListener((Activity) context, mOnWebViewListener);

        imageView = new ImageView(context);

//        faceDetectionFragment = FaceDetectionFragment.newInstance();
//        hostFragment(faceDetectionFragment);
//        faceDetection = new FaceDetectionImpl(runtime);
    }

    public void playMedia0(SourceInfo sourceInfo) {
        // Cache file name
        String url = sourceInfo.getSource();
        final String fileName = url.substring(url.lastIndexOf('/') + 1);

        if (sourceInfo.getType() == SourceInfo.SourceType.IMAGE) {
            if (getChildCount() == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addView(imageView, params);
            }
            playImageMedia(url, fileName, 0); // play image file
        } else if (sourceInfo.getType() == SourceInfo.SourceType.URL || sourceInfo.getType() == SourceInfo.SourceType.WEB) {
//            if (NetworkUtils.checkInternetConnection()) {
            if (getChildCount() == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addView(webView, params);
                playWebViewMedia(url, fileName, 0); // play webview url
            }
//            } else {
//                if (getChildCount() == 0) {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//                    addView(exoVideoView, params);
//                }
//                if (!TextUtils.isEmpty(HomeController.LOCAL_VIDEO_PATH))
//                    playVideoMedia(HomeController.LOCAL_VIDEO_PATH, fileName, 0); // play video file
//            }
        } else {
            if (getChildCount() == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                addView(exoVideoView, params);
            }
            if (sourceInfo.getType() == SourceInfo.SourceType.VIDEO_LIST)
                playVideoListMedia(sourceInfo.getArrSources());
            else
                playVideoMedia(url, fileName, 0); // play video file
        }
    }

    private int mCurrentIndex = 0;

    private void playVideoListMedia(final List<String> sources) {
        if (sources != null && !sources.isEmpty()) {
            DebugLog.d("media list " + sources.get(mCurrentIndex));

            if (!TextUtils.isEmpty(sources.get(mCurrentIndex))) {
                exoVideoView.setVideoPath(sources.get(mCurrentIndex));
                mCurrentIndex++;
                mCurrentIndex = mCurrentIndex % sources.size();
            } else {
                mCurrentIndex++;
                mCurrentIndex = mCurrentIndex % sources.size();
                playVideoListMedia(sources);
            }
            exoVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    playVideoListMedia(sources);
                }
            });
            exoVideoView.start();
        }

        updateMediaVisibility(MediaType.VIDEO);
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
        if (!Patterns.WEB_URL.matcher(url).matches())
            url = "file://" + url;

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                DebugLog.d("onReceivedError");
            }
        });
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
                exoVideoView.setOnCompletionListener(null);
                exoVideoView.stopPlayback();
                exoVideoView.release(true);
                exoVideoView.stopBackgroundPlay();
                break;
        }
        removeAllViews();
    }

}
