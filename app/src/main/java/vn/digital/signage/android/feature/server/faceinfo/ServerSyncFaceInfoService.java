package vn.digital.signage.android.feature.server.faceinfo;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.esotericsoftware.minlog.Log;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.home.HomeFragment;
import vn.digital.signage.android.feature.server.BaseService;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class ServerSyncFaceInfoService extends BaseService {

    @Inject
    SMRuntime runtime;

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();

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
        // schedule task
        timer.scheduleAtFixedRate(new FaceInfoTimerTask(), 0, Config.OverallConfig.FACE_INFO_INTERVAL_IN_MS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class FaceInfoTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    if (runtime.getRegisterInfo() != null) {
                        if (Config.hasLogLevel(LogLevel.SERVICE))
                            Log.info(" FaceInfoTimerTask - call submit face detection");
                        if (HomeFragment.instance != null && HomeFragment.instance.getHomeView() != null) {
                            HomeFragment.instance.getHomeView().submitFaceInfo();
                        }
                    }
                }
            });
        }
    }
}
