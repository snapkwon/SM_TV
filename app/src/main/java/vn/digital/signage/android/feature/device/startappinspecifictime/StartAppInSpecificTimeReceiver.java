package vn.digital.signage.android.feature.device.startappinspecifictime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.base.MainActivity;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class StartAppInSpecificTimeReceiver extends BroadcastReceiver {
    private static final int PERIOD = 60000; // 60 seconds

    @Override
    public void onReceive(Context ctxt, Intent i) {
        Log.info("StartAppInSpecificTimeReceiver - onReceived");

        if (Config.hasLogLevel(LogLevel.RECEIVER))
            Log.info("StartAppInSpecificTimeReceiver - onReceived");

        if (i.getAction() == null) {
            WakefulIntentService.sendWakefulWork(ctxt, StartAppInSpecificTimeService.class);
        }

        Log.info("Schedule Start App in specific time alarms");
        if (Config.hasLogLevel(LogLevel.RECEIVER))
            Log.info("Schedule Start App in specific time alarms");
        scheduleAlarms(ctxt);
    }

    public static void scheduleAlarms(Context ctxt) {
        AlarmManager mgr =
                (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, StartAppInSpecificTimeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctxt, 0, i, 0);
        Intent i2 = new Intent(ctxt, MainActivity.class);
        PendingIntent pi2 = PendingIntent.getActivity(ctxt, 0, i2, 0);

        if (Build.VERSION.SDK_INT >= 21) {
            AlarmManager.AlarmClockInfo ac =
                    new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + PERIOD,
                            pi2);

            mgr.setAlarmClock(ac, pi);
        } else {
            mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + PERIOD, pi);
        }
    }
}