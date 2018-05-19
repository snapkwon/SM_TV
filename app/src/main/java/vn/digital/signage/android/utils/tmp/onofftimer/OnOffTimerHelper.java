package vn.digital.signage.android.utils.tmp.onofftimer;

public class OnOffTimerHelper {

    /*public static void onReceiveAutoOnTimerAction(Context context) {
        if (!Utils.Services.isServiceRunning(context, AutoOnTimerService.TAG)) {
            Intent i = new Intent(context, AutoOnTimerService.class);
            context.startService(i);
        }
    }

    public static void onReceiveAutoOffTimerAction(Context context) {
        if (!Utils.Services.isServiceRunning(context, AutoOffTimerService.TAG)) {
            Intent i = new Intent(context, AutoOffTimerService.class);
            context.startService(i);
        }
    }

    public static void scheduleAutoOnScreenTimer(Context context, int hour, int minute, int second) {
        // set fire alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimerScreenOnReceiver.class);

        // set schedule time
        long scheduleAutoPlayTime = DateUtils.scheduleAutoPlayTime(hour, minute, second);

        // broadcast an intent
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                scheduleAutoPlayTime,
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static void scheduleAutoOffScreenTimer(Context context, int hour, int minute, int second) {
        // set fire alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimerScreenOffReceiver.class);

        // set schedule time
        long scheduleAutoPlayTime = DateUtils.scheduleAutoPlayTime(hour, minute, second);

        // broadcast an intent
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                scheduleAutoPlayTime,
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }*/
}
