package vn.digital.signage.android.feature.device.bootup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class DeviceBootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.info("DeviceBootUpReceiver - on Receive BootUp");
        if (Config.hasLogLevel(LogLevel.RECEIVER))
            Log.info("DeviceBootUpReceiver - on Receive BootUp");


        if (!Utils.Services.isServiceRunning(context, DeviceBootUpService.class.getName())) {
            Log.info("DeviceBootUpReceiver - DeviceBootUpService is not run - start service");
            Intent i = new Intent(context, DeviceBootUpService.class);
            context.startService(i);
        }else{
            Log.info("DeviceBootUpReceiver - DeviceBootUpService has already running");
        }
    }
}
