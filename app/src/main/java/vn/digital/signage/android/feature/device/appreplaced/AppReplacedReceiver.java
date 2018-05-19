package vn.digital.signage.android.feature.device.appreplaced;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.device.bootup.DeviceBootUpService;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class AppReplacedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.info("AppReplacedReceiver - on Received Replaced");
        if (Config.hasLogLevel(LogLevel.RECEIVER))
            Log.info("AppReplacedReceiver - on Received Replaced");

        if (!Utils.Services.isServiceRunning(context, DeviceBootUpService.class.getName())) {
            Intent i = new Intent(context, DeviceBootUpService.class);
            context.startService(i);
        }
    }
}
