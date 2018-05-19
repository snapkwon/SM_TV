package vn.digital.signage.android.feature.server.syncautoplayatdefinedtime;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.esotericsoftware.minlog.Log;

import javax.inject.Inject;

import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.RootActivity;
import vn.digital.signage.android.feature.client.home.HomeFragment;
import vn.digital.signage.android.utils.autoplay.AutoPlayHelper;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class MediaPlayScheduleService extends Service {
    public static final String TAG = MediaPlayScheduleService.class.getSimpleName();

    @Inject
    SMRuntime runtime;

    private Handler mHandler;

    @Inject
    public MediaPlayScheduleService() {
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
            Log.info(TAG + " auto_play_media - MediaPlayScheduleService - onCreate");

        App.getInstance().inject(this);

        // handle play video with selected index
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (runtime.getRegisterInfo() != null) {
                    if (HomeFragment.instance != null && HomeFragment.instance.getHomeView() != null) {
                        if (Config.hasLogLevel(LogLevel.SERVICE))
                            Log.info(TAG + " auto_play_media - MediaPlayScheduleService - start play media with index: " + runtime.getAutoPlaySchedule().getPlayAtIndex());
                        // start play media with index - Delay 0ms
                        HomeFragment.instance.getHomeView().playDefaultVideo(runtime.getAutoPlaySchedule().getPlayAtIndex());
                    } else {
                        // start activity
                        RootActivity.startRootActivity(getBaseContext());

                        // start play media with index - Delay 500ms
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (HomeFragment.instance != null && HomeFragment.instance.getHomeView() != null) {
                                    if (Config.hasLogLevel(LogLevel.SERVICE))
                                        Log.info(TAG + " auto_play_media - MediaPlayScheduleService - start play media with index: " + runtime.getAutoPlaySchedule().getPlayAtIndex());
                                    HomeFragment.instance.getHomeView().playDefaultVideo(runtime.getAutoPlaySchedule().getPlayAtIndex());
                                }
                            }
                        }, 500l);
                    }
                }

                MediaPlayScheduleService.this.stopSelf();
            }
        });


        // schedule another play receiver
        AutoPlayResponse r = runtime.getAutoPlaySchedule();
        if (r.getAutoplay() != null &&
                (r.getAutoplay().getHour() >= 0
                        || r.getAutoplay().getMinute() >= 0
                        || r.getAutoplay().getSecond() >= 0)) {
            AutoPlayHelper.scheduleAutoPlay(getApplicationContext(), runtime);
        }
    }
}
