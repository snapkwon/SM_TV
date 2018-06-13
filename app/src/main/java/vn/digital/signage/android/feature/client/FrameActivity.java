package vn.digital.signage.android.feature.client;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import vn.digital.signage.android.R;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.feature.client.home.FrameView;
import vn.digital.signage.android.utils.DebugLog;
import vn.digital.signage.android.utils.UiUtils;

public class FrameActivity extends FragmentActivity {


    private List<SourceInfo> lists;
    private static final Handler handler = new Handler();

    @InjectView(R.id.id_container)
    RelativeLayout relativeLayout;
    private FrameCountDownTimer mCountDownTimer;
    private long mCurrentImageStartTimeInMillis;
    private List<SourceInfo> sourceInfos;
    private long duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frame);
        ButterKnife.inject(this);
        UiUtils.setFullScreenWindow(this, relativeLayout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sourceInfos = (List<SourceInfo>) bundle.getSerializable("source");
            duration = bundle.getLong("duration");
            playSelectedFrame(sourceInfos);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void playSelectedFrame(List<SourceInfo> lists) {
        for (SourceInfo info : lists) {
            FrameView frameLayout = new FrameView(getApplicationContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(info.getWidth(), info.getHeight());
            layoutParams.leftMargin = info.getLeft();
            layoutParams.topMargin = info.getTop();
            relativeLayout.addView(frameLayout, layoutParams);
            frameLayout.playMedia0(info);
            DebugLog.d(info.getLeft() + "|" + info.getTop() + "|" + info.getWidth() + "|" + info.getHeight());

        }

        long imageDuration = duration;

        // destroy current count down timer
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        mCountDownTimer = new FrameCountDownTimer(imageDuration, 1000, "");
        mCountDownTimer.start();
    }


    private class FrameCountDownTimer extends CountDownTimer {
        protected boolean mIsRunning;

        FrameCountDownTimer(long millisInFuture, long countDownInterval, String fileName) {
            super(millisInFuture, countDownInterval);
        }

        boolean isRunning() {
            return mIsRunning;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mIsRunning = true;
        }

        @Override
        public void onFinish() {
            DebugLog.d("finish frame");
            try {
                mIsRunning = false;
//                runtime.setMediaFileNameOff(fileName);
            } catch (Exception e) {
//                if (Config.hasLogLevel(LogLevel.DATA))
//                    log.error("Error start video by list", e);
            } finally {
                finish();
            }
        }

    }

    @Override
    public void finish() {
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            FrameView view = (FrameView) relativeLayout.getChildAt(i);
            view.onDestroy();
        }
        // destroy current count down timer
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        super.finish();
    }
}
