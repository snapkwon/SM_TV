package vn.digital.signage.android.feature.device.autoupdate;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.esotericsoftware.minlog.Log;
import com.yepstudio.android.library.autoupdate.AppUpdateService;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class AutoUpdateService extends Service {
    @Inject
    SMRuntime runtime;
    // run on another Thread to avoid crash
    private Handler handler = new Handler();
    // timer handling
    private Timer timer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        App.getInstance().inject(this);

        // cancel if already existed
        if (timer != null) {
            timer.cancel();
        } else {
            // recreate new
            timer = new Timer();
        }

        if (Config.hasLogLevel(LogLevel.SERVICE))
            Log.info("AutoUpdateService - schedule auto update timer task");

        // schedule task
        timer.scheduleAtFixedRate(new AutoUpdateTimerTask(), 0, Config.OverallConfig.AUTO_UPDATE_INFO_NOTIFY_INTERVAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class AutoUpdateTimerTask extends TimerTask {

        @Override
        public void run() {

            // run on another thread
            handler.post(new Runnable() {

                @Override
                public void run() {
                    if (Config.hasLogLevel(LogLevel.SERVICE))
                        Log.info("AutoUpdateService - Starting service auto update app");

                    if (runtime.getApiUrl() != null) {
                        AppUpdateService.checkUpdate(getApplicationContext(), true);
                    }
                }
            });
        }
    }
}
