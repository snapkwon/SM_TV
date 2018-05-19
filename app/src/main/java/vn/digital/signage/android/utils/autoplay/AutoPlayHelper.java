package vn.digital.signage.android.utils.autoplay;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Date;

import vn.digital.signage.android.Constants;
import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.server.syncautoplayatdefinedtime.MediaPlayScheduleReceiver;
import vn.digital.signage.android.feature.client.home.HomeFragment;
import vn.digital.signage.android.utils.DateUtils;

public class AutoPlayHelper {
    public static final String TAG = HomeFragment.class.getSimpleName();

    public static void scheduleAutoPlay(Context context, SMRuntime runtime) {
        // set fire alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MediaPlayScheduleReceiver.class);

        // set schedule time
        long scheduleAutoPlayTime = DateUtils.scheduleAutoPlayTime(runtime);
        Log.d(TAG, String.format("auto_play_media - AutoPlayHelper - schedule event at - %s",
                (new Gson()).toJson(runtime.getAutoPlaySchedule())));

        // broadcast an intent
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                scheduleAutoPlayTime,
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        //schedule next event
        AutoPlayResponse r = runtime.getAutoPlaySchedule();
        r.getAutoplay().setHour(r.getAutoplay().getHour() + Constants.ONE_DAY_IN_HOUR);
        runtime.setAutoPlaySchedule(r);
    }

    public static boolean isCurrentHourLargerThanCompareValues(int hour, int minute) {
        Date d = DateUtils.getCurrentDateWithSecondOffset(0);
        if (d.getHours() > hour) {
            return true;
        } else if (d.getHours() == hour && d.getMinutes() > minute) {
            return true;
        }
        return false;
    }
}
