package vn.digital.signage.android.feature.server.oneofftimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.security.AutoOnOffScreenHelper;

public class TimerScreenOffReceiver extends BroadcastReceiver {
    public static final String TAG = TimerScreenOffReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        AutoOnOffScreenHelper.onReceiveAutoOnOffScreenAction(context, intent);

        if (Config.hasLogLevel(LogLevel.RECEIVER))
            Log.info(TAG + " on Receive");

        //OnOffTimerHelper.onReceiveAutoOffTimerAction(context);
    }
}
