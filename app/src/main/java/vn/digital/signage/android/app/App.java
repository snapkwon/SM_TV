package vn.digital.signage.android.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import vn.digital.signage.android.injection.module.AppModule;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.foreground.Foreground;

public class App extends Application {
    private static App instance;

    private ObjectGraph mObjectGraph;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Application's object graph for handling dependency injection
        mObjectGraph = ObjectGraph.create(getModules());
        mObjectGraph.injectStatics();

        // install crashlytics
        Fabric.with(this, new Crashlytics());

        // start Foreground listener
        Foreground.init(this);
    }

    protected Object[] getModules() {
        return new Object[]{new AppModule(this)};
    }

    public void inject(Object object) {
        if (mObjectGraph == null) {
            throw new IllegalArgumentException("object graph must be initialized prior to calling inject");
        }
        mObjectGraph.inject(object);
    }

    public void setStrictModePolicy() {
        if (Utils.BuildVersion.hasGingerbread()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }
}
