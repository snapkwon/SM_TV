package vn.digital.signage.android.feature.server.oneofftimer;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.esotericsoftware.minlog.Log;

import javax.inject.Inject;

import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class AutoOffTimerService extends Service {
    public static final String TAG = AutoOffTimerService.class.getSimpleName();

    @Inject
    SMRuntime runtime;

    private Handler mHandler;

    @Inject
    public AutoOffTimerService() {
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.hasLogLevel(LogLevel.SERVICE))
            Log.info(TAG + "onCreate");

        App.getInstance().inject(this);

       /* AutoOnOffScreenHelper.turnOffScreen();

        // schedule another play receiver
        OnOffTimerResponse r = runtime.getOnOffTimer();
        if (r.getTimeOff() != null &&
                (r.getTimeOff().getHour() >= 0
                        || r.getTimeOff().getMinute() >= 0
                        || r.getTimeOff().getSecond() >= 0)) {
            OnOffTimerHelper.scheduleAutoOffScreenTimer(getApplicationContext(),
                    r.getTimeOff().getHour(),
                    r.getTimeOff().getMinute(),
                    r.getTimeOff().getSecond());
        }

        this.stopSelf();*/
    }
}
