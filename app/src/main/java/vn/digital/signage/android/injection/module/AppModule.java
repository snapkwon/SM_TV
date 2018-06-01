package vn.digital.signage.android.injection.module;

import android.content.Context;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.WindowManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.FrameActivity;
import vn.digital.signage.android.feature.client.base.MainActivity;
import vn.digital.signage.android.feature.client.home.HomeController;
import vn.digital.signage.android.feature.client.home.HomeFragment;
import vn.digital.signage.android.feature.client.registration.RegisterController;
import vn.digital.signage.android.feature.client.registration.RegisterFragment;
import vn.digital.signage.android.feature.server.faceinfo.ServerSyncFaceInfoService;
import vn.digital.signage.android.feature.server.oneofftimer.AutoOffTimerService;
import vn.digital.signage.android.feature.server.oneofftimer.AutoOnTimerService;
import vn.digital.signage.android.feature.server.syncautoplayatdefinedtime.MediaPlayScheduleService;
import vn.digital.signage.android.feature.server.syncautoplayatdefinedtime.ServerSyncAutoPlayService;
import vn.digital.signage.android.feature.server.synccurrentplaylist.ServerSyncCurrentPlaylistService;
import vn.digital.signage.android.feature.server.updateinfotoserver.UpdateInfoToServerService;
import vn.digital.signage.android.injection.scope.ForApp;
import vn.digital.signage.android.utils.FragmentHelper;

@Module(injects = {
        // Utilities
        EventBus.class,
        // Activities
        MainActivity.class,
        FrameActivity.class,
        // Controller
        HomeController.class,
        RegisterController.class,
        // Fragment
        HomeFragment.class,
        RegisterFragment.class,
        // Helper
        FragmentHelper.class,
        // Services
        ServerSyncCurrentPlaylistService.class,
        ServerSyncAutoPlayService.class,
        UpdateInfoToServerService.class,
        MediaPlayScheduleService.class,
        AutoOnTimerService.class,
        AutoOffTimerService.class,
        ServerSyncFaceInfoService.class
        //AutoUpdateService.class
}, library = true)
public class AppModule {

    private final App mApp;

    public AppModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    @ForApp
    Context provideApplicationContext() {
        return mApp;
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) mApp.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    WindowManager provideWindowManager() {
        return (WindowManager) mApp.getSystemService(Context.WINDOW_SERVICE);
    }

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater() {
        return (LayoutInflater) mApp.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return mApp.getResources();
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    public FragmentHelper provideFragmentHelper() {
        return new FragmentHelper();
    }

    @Provides
    @Singleton
    public SMRuntime provideRuntime() {
        SMRuntime runtime = SMRuntime.getInstance();
        runtime.init(mApp.getApplicationContext());
        return runtime;
    }

    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) mApp.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    public PowerManager providePowerManager() {
        return (PowerManager) mApp.getSystemService(Context.POWER_SERVICE);
    }

    @Provides
    @Singleton
    public PowerManager.WakeLock provideWakeLock() {
        return providePowerManager().newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass()
                .getName());
    }

    @Provides
    @Singleton
    public String provideDeviceId() {
        return Settings.Secure.getString(mApp.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
