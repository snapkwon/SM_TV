package vn.digital.signage.android.feature.server.syncautoplayatdefinedtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

/**
 * The type Media play scheduled receiver.
 */
public class MediaPlayScheduleReceiver extends BroadcastReceiver {
    public static final String TAG = MediaPlayScheduleReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Config.hasLogLevel(LogLevel.RECEIVER))
            Log.info(TAG + " auto_play_media - MediaPlayScheduleReceiver - on Receive");

        if (!Utils.Services.isServiceRunning(context, MediaPlayScheduleService.TAG)) {
            Intent i = new Intent(context, MediaPlayScheduleService.class);
            context.startService(i);
        }
    }
}
