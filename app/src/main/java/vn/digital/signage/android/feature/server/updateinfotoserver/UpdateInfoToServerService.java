package vn.digital.signage.android.feature.server.updateinfotoserver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.esotericsoftware.minlog.Log;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.enumeration.LogLevel;

/**
 * The type Update info to server service.
 */
public class UpdateInfoToServerService extends Service {

    // run on another Thread to avoid crash
    private Handler handler = new Handler();

    // timer handling
    private Timer timer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        App.getInstance().inject(this);

        SingletonService.setInstance(this);

        //binder = new Binder();

        // cancel if already existed
        if (timer != null) {
            timer.cancel();
        } else {
            // recreate new
            timer = new Timer();
        }

        if (Config.hasLogLevel(LogLevel.SERVICE))
            Log.info("UpdateInfoToServerService - schedule UpdateInfoTimerTask.");

        // schedule task
        timer.scheduleAtFixedRate(new UpdateInfoTimerTask(), 0, Config.OverallConfig.UPDATE_INFO_NOTIFY_INTERVAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //return binder;
    }

    public void cancelTimer() {
        timer.cancel();
        ;
    }

    public static class SingletonService {
        private static UpdateInfoToServerService mInstance;

        private SingletonService() {
            // hide public SingletonService
        }

        public static void setInstance(UpdateInfoToServerService instance) {
            mInstance = instance;
        }

        public static UpdateInfoToServerService getIntance() {
            return mInstance;
        }
    }

    public class Binder extends android.os.Binder {
        public UpdateInfoToServerService getService() {
            return UpdateInfoToServerService.this;
        }
    }

    final class UpdateInfoTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (Config.hasLogLevel(LogLevel.SERVICE))
                        Log.info("UpdateInfoToServerService - UpdateInfoToServerEvent");
                    EventBus.getDefault().post(new UpdateInfoToServerEvent());
                }
            });
        }
    }
}
