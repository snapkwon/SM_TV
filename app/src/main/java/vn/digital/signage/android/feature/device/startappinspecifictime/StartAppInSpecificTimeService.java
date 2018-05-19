package vn.digital.signage.android.feature.device.startappinspecifictime;

import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.esotericsoftware.minlog.Log;

import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.base.MainActivity;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class StartAppInSpecificTimeService extends WakefulIntentService {

    public StartAppInSpecificTimeService() {
        super("StartAppInSpecificTimeService");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        if (Config.hasLogLevel(LogLevel.SERVICE))
            Log.info("StartAppInSpecificTimeService - calling doWakefulWork");

        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            if (Config.hasLogLevel(LogLevel.SERVICE))
                Log.info(String.format("StartAppInSpecificTimeService - error %s", e.getMessage()));
        }

        if (Config.IS_START_APP_AFTER_CRASHED_SCHEDULE_ENABLED && !Utils.isAppRunning(App.getInstance())) {
            if (Config.hasLogLevel(LogLevel.SERVICE))
                Log.info("StartAppInSpecificTimeService - startMainActivity");
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent mIntent = new Intent(this, MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
    }
}
