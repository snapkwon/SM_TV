package vn.digital.signage.android.feature.device.screenstatechange;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.RootActivity;
import vn.digital.signage.android.utils.enumeration.LogLevel;

import static vn.digital.signage.android.app.Config.ONE_MINUTE;

public class ScreenStateChangeService extends Service {
    public static final String TAG = ScreenStateChangeService.class.getSimpleName();

    private BroadcastReceiver receiver = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Register receiver that handles screen on and screen off logic
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        receiver = new ScreenStateChangeReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        boolean screenOn = false;
        try {
            screenOn = intent.getBooleanExtra("screen_state", false);
        } catch (Exception e) {
            if (Config.hasLogLevel(LogLevel.SERVICE))
                Log.error("ScreenStateChangeService", e);
        }

        if (!screenOn) {
            // auto Start Screen
            RootActivity.startRootActivity(getBaseContext(), ONE_MINUTE);
        }
    }

    @Override
    public void onDestroy() {
        if (Config.hasLogLevel(LogLevel.SERVICE))
            Log.info(TAG + " on destroy");
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
