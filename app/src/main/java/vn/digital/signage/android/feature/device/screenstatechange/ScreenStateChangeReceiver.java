package vn.digital.signage.android.feature.device.screenstatechange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class ScreenStateChangeReceiver extends BroadcastReceiver {
    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;

            Log.info("ScreenStateChangeReceiver - onReceive - current screen OFF");
            if (Config.hasLogLevel(LogLevel.RECEIVER))
                Log.info("ScreenStateChangeReceiver - onReceive - current screen OFF");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
            Log.info("ScreenStateChangeReceiver - onReceive - current screen ON");
            if (Config.hasLogLevel(LogLevel.RECEIVER))
                Log.info("ScreenStateChangeReceiver - onReceive - current screen ON");
        }

        if (!Utils.Services.isServiceRunning(context, ScreenStateChangeService.TAG)) {
            // Send Current screen ON/OFF value to service
            Intent i = new Intent(context, ScreenStateChangeService.class);
            i.putExtra("screen_state", screenOff);
            context.startService(i);
        }
    }
}
