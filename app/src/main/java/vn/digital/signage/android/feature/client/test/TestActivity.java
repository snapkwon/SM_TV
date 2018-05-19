package vn.digital.signage.android.feature.client.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.apache.log4j.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import im.delight.android.webview.AdvancedWebView;
import vn.digital.signage.android.R;
import vn.digital.signage.android.feature.device.startappinspecifictime.StartAppInSpecificTimeReceiver;
import vn.digital.signage.android.utils.UiUtils;

public class TestActivity extends FragmentActivity {
    private final Logger log = Logger.getLogger(TestActivity.class);

    @InjectView(R.id.webview)
    AdvancedWebView webview;

    public static Intent intentInstance(Context context) {
        return new Intent(context, TestActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtils.setFullScreenView(this);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);

        //testStartAppInSpecificTimeService();
        testWebView();
    }

    private void testWebView() {
        webview.setListener(this, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(String url) {

            }

            @Override
            public void onPageError(int errorCode, String description, String failingUrl) {

            }

            @Override
            public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

            }

            @Override
            public void onExternalPageRequest(String url) {

            }
        });
        webview.loadUrl("http://www.google.com.vn");
    }

    private void testStartAppInSpecificTimeService() {
        StartAppInSpecificTimeReceiver.scheduleAlarms(this);
    }
}
