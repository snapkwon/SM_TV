package vn.digital.signage.android.feature.device.bootup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.esotericsoftware.minlog.Log;


import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.RootActivity;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class DeviceBootUpService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Config.hasLogLevel(LogLevel.SERVICE))
            Log.info("DeviceBootUpReceiver - on DeviceBootUpService");

        Log.info("DeviceBootUpReceiver - on DeviceBootUpService - starting main activity");
        RootActivity.startRootActivity(getBaseContext());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
