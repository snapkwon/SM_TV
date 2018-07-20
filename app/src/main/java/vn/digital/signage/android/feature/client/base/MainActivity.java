package vn.digital.signage.android.feature.client.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import vn.digital.signage.android.Constants;
import vn.digital.signage.android.R;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.RootActivity;
import vn.digital.signage.android.feature.client.home.HomeFragment;
import vn.digital.signage.android.feature.client.registration.RegisterFragment;
import vn.digital.signage.android.feature.device.startappinspecifictime.StartAppInSpecificTimeReceiver;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.foreground.Foreground;
import vn.digital.signage.android.utils.security.AutoOnOffScreenHelper;

public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private final Logger logger = Logger.getLogger(BaseActivity.class);


    private BaseFragment fragment;

    private Foreground.Listener mBackgroundListener = new Foreground.Listener() {
        @Override
        public void onBecameForeground() {
            if (Config.hasLogLevel(LogLevel.UI))
                logger.info("BaseActivity - onForeground");
        }

        @Override
        public void onBecameBackground() {
            ActivityCompat.finishAffinity(MainActivity.this);
//            MainActivity.this.finish();

            if (Config.hasLogLevel(LogLevel.UI))
                logger.info("BaseActivity - onBackground");

            if (Config.IS_AUTO_PLAY_WHEN_SCREEN_MINIMIZED) {
                RootActivity.startRootActivity(MainActivity.this, Config.MAXIMIZE_AFTER_SECONDS_DURATION);
            }
        }
    };

    public static Intent intentInstance(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Inject
    public MainActivity() {
        // default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        stopBootUpService();

        stopScreenStateChangeService();

        Foreground.get(this).addListener(mBackgroundListener);

        StartAppInSpecificTimeReceiver.scheduleAlarms(this);

        if (runtime.getRegisterInfo() == null) {
            // go to register screen
            fragment = RegisterFragment.newInstance();
            switchFragment(fragment, RegisterFragment.TAG, false);
        } else {
            // go to home screen
            fragment = HomeFragment.newInstance();
            switchFragment(fragment, HomeFragment.TAG, false);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
//            DebugLog.d(" onActivityResult 1" + mFragmentHelper.getActiveFragment(null) + "|" + fragment);
            BaseFragment homeFragment = (BaseFragment) mFragmentHelper.getActiveFragment(null);
            if (homeFragment != null) {
                homeFragment.onActivityResult(requestCode, resultCode, data);
            } else if (fragment != null)
                fragment.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {// to avoid null pointer
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Config.hasLogLevel(LogLevel.UI))
            logger.info("BaseActivity - onStart");

        onStartService();

        if (runtime.getRegisterInfo() != null && (runtime.getMediaFileNameOn() != null)) {

            // set pref time on
            runtime.setMediaTimeOn(System.currentTimeMillis());

            // if pref time on is smaller than pref time off, then we update time off value before update to server
            if (runtime.getMediaTimeOn() > runtime.getMediaTimeOff()) {
                runtime.setMediaTimeOff(System.currentTimeMillis());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.hasLogLevel(LogLevel.UI))
            logger.info("BaseActivity - onResume");

        if (Constants.IS_AUTO_ON_OFF_ENABLED) {
            AutoOnOffScreenHelper.onResumeWakeupBypassLockScreen(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Config.hasLogLevel(LogLevel.UI))
            logger.info("BaseActivity - onStop");

        onStopService();
        EventBus.getDefault().unregister(this);

        if (runtime.getRegisterInfo() != null && runtime.getMediaFileNameOff() != null) {
            if (Config.hasLogLevel(LogLevel.UI))
                logger.info(MainActivity.TAG + " on Stop Activity - call do History");

            runtime.setMediaTimeOff(System.currentTimeMillis());

            postHistoryToServer();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Config.hasLogLevel(LogLevel.UI))
            logger.info("BaseActivity - onDestroy");

        // Destroy Service
        onStopService();
        // Destroy EventBus
        EventBus.getDefault().unregister(this);

        ButterKnife.reset(this);

        unregisterReceiver(screenStateReceiver);

        Foreground.get(this).removeListener(mBackgroundListener);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        final Fragment fragment = mFragmentHelper.getFragmentByTag(HomeFragment.TAG);
        if (fragment != null && (fragment instanceof HomeFragment)) {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
            final HomeFragment home = (HomeFragment) fragment;
            return home.getHomeView().getTaskManager().retainTask();
        }
        return null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= 19 && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}