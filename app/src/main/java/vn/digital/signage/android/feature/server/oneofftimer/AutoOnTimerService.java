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
import vn.digital.signage.android.utils.security.AutoOnOffScreenHelper;

public class AutoOnTimerService extends Service {
    public static final String TAG = AutoOnTimerService.class.getSimpleName();

    @Inject
    SMRuntime runtime;

    private Handler mHandler;

    @Inject
    public AutoOnTimerService() {
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
            Log.info(TAG + " onCreate");

        App.getInstance().inject(this);

        AutoOnOffScreenHelper.turnOnScreen();

        // schedule another play receiver
        /*OnOffTimerResponse r = runtime.getOnOffTimer();
        if (r.getTimeOn() != null &&
                (r.getTimeOn().getHour() >= 0
                        || r.getTimeOn().getMinute() >= 0
                        || r.getTimeOn().getSecond() >= 0)) {
            OnOffTimerHelper.scheduleAutoOnScreenTimer(getApplicationContext(),
                    r.getTimeOn().getHour(),
                    r.getTimeOn().getMinute(),
                    r.getTimeOn().getSecond());
        }

        this.stopSelf();*/
    }
}
