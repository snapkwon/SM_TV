package vn.digital.signage.android.utils.security;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

import vn.digital.signage.android.Constants;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.feature.device.admin.SmDeviceAdminReceiver;
import vn.digital.signage.android.feature.server.oneofftimer.TimerScreenOffReceiver;
import vn.digital.signage.android.feature.server.oneofftimer.TimerScreenOnReceiver;

public class AutoOnOffScreenHelper {
    public static final String TAG = AutoOnOffScreenHelper.class.getSimpleName();

    public static final String TURN_ON_OFF_STATUS = "TURN_ON_OFF_STATUS";
    public static final int TURN_ON = 1;
    public static final int TURN_OFF = 2;

    public static void turnOffScreen() {
        DevicePolicyManager mDPM = (DevicePolicyManager) App.getInstance().getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (mDPM.isAdminActive(new ComponentName(App.getInstance(), SmDeviceAdminReceiver.class))) {
            mDPM.lockNow();
        }
    }

    public static void turnOnScreen() {
        PowerManager.WakeLock screenLock = ((PowerManager) App.getInstance().getSystemService(Activity.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();
    }

    public static void onResumeWakeupBypassLockScreen(Activity activity) {
        Window wind = activity.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        wind.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void scheduleOnScreenAlarm() {
        AlarmManager alarmManager = (AlarmManager) App.getInstance().getSystemService(Context.ALARM_SERVICE);

        // setup turn on alarm scheduler at fixed time: TURN_ON_HOUR_OF_DAY : TURN_ON_MINUTE : TURN_ON_SECOND
        Long fixedTime = getTurnOnScreenInMillis();

        Intent alarmIntent = new Intent(App.getInstance(), TimerScreenOnReceiver.class);
        alarmIntent.putExtra(TURN_ON_OFF_STATUS, TURN_ON);

        //set the alarm for particular time
        /*alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 170000, 60000,
                PendingIntent.getBroadcast(App.getInstance(), 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));*/
        alarmManager.set(AlarmManager.RTC_WAKEUP, fixedTime,
                PendingIntent.getBroadcast(App.getInstance(), 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        Log.i(TAG, "setup turn off screen successfully");
    }

    public static void scheduleOffScreenAlarm() {
        AlarmManager alarmManager = (AlarmManager) App.getInstance().getSystemService(Context.ALARM_SERVICE);

        // setup turn off alarm scheduler at fixed time: TURN_OFF_HOUR_OF_DAY : TURN_OFF_MINUTE : TURN_OFF_SECOND
        Long fixedTime = getTurnOffScreenInMillis();

        Intent alarmIntent = new Intent(App.getInstance(), TimerScreenOffReceiver.class);
        alarmIntent.putExtra(TURN_ON_OFF_STATUS, TURN_OFF);

        //set the alarm for particular time
        /*alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10000, 60000,
                PendingIntent.getBroadcast(App.getInstance(), 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));*/
        alarmManager.set(AlarmManager.RTC_WAKEUP, fixedTime,
                PendingIntent.getBroadcast(App.getInstance(), 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        Log.i(TAG, "setup turn on screen successfully");
    }

    public static void onReceiveAutoOnOffScreenAction(Context context, Intent intent) {
        //Toast.makeText(context, "Alarm Triggered auto on off screen receiver", Toast.LENGTH_LONG).show();
        Log.i(TAG, "auto on off screen receiver is fired");

        Bundle extras = intent.getExtras();

        if (extras != null) {
            int turnOnOffStatus = extras.getInt(AutoOnOffScreenHelper.TURN_ON_OFF_STATUS);
            if (turnOnOffStatus == AutoOnOffScreenHelper.TURN_ON) {

                AutoOnOffScreenHelper.turnOnScreen();

                scheduleOffScreenAlarm();

                Log.i(TAG, "auto on off screen receiver is fired - called turn on screen");
            } else if (turnOnOffStatus == AutoOnOffScreenHelper.TURN_OFF) {

                AutoOnOffScreenHelper.turnOffScreen();

                scheduleOnScreenAlarm();
                Log.i(TAG, "auto on off screen receiver is fired - called turn off screen");
            }
        }
    }

    @NonNull
    private static Long getTurnOnScreenInMillis() {
        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
        //Long time = new GregorianCalendar().getTimeInMillis() + 24 * 60 * 60 * 1000;
        Long time = new GregorianCalendar().getTimeInMillis() + 10 * 1000;

        if (Constants.DEBUG_AUTO_ON_OFF_TIMER) {
            time = new GregorianCalendar().getTimeInMillis() + 10 * 1000;

        } else {
            Calendar timeOff9 = Calendar.getInstance();
            timeOff9.add(Calendar.DATE, 1);
            timeOff9.set(Calendar.HOUR_OF_DAY, Constants.TURN_ON_HOUR_OF_DAY);
            timeOff9.set(Calendar.MINUTE, Constants.TURN_ON_MINUTE);
            timeOff9.set(Calendar.SECOND, Constants.TURN_ON_SECOND);

            time = timeOff9.getTimeInMillis();
        }
        return time;
    }

    @NonNull
    private static Long getTurnOffScreenInMillis() {
        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
        //Long time = new GregorianCalendar().getTimeInMillis() + 24 * 60 * 60 * 1000;
        Long time = new GregorianCalendar().getTimeInMillis() + 10 * 1000;

        if (Constants.DEBUG_AUTO_ON_OFF_TIMER) {
            time = new GregorianCalendar().getTimeInMillis() + 10 * 1000;
        } else {
            Calendar timeOff9 = Calendar.getInstance();
            timeOff9.set(Calendar.HOUR_OF_DAY, Constants.TURN_OFF_HOUR_OF_DAY);
            timeOff9.set(Calendar.MINUTE, Constants.TURN_OFF_MINUTE);
            timeOff9.set(Calendar.SECOND, Constants.TURN_OFF_SECOND);

            time = timeOff9.getTimeInMillis();
        }
        return time;
    }
}
