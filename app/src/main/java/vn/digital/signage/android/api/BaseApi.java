package vn.digital.signage.android.api;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import vn.digital.signage.android.BuildConfig;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.utils.StringConverter;
import vn.digital.signage.android.utils.network.DigitalSignageClient;

public abstract class BaseApi {

    @Inject
    protected EventBus eventBus;
    protected RestAdapter restAdapter;

    public BaseApi() {
        App.getInstance().inject(this);
    }

    public void getRestAdapter(String baseHost) {
        Executor executor = Executors.newCachedThreadPool();
        restAdapter = new RestAdapter.Builder()
                .setClient(new DigitalSignageClient())
                .setEndpoint(baseHost)
                .setExecutors(executor, executor)
                .setConverter(new StringConverter())
                .build();
        if (BuildConfig.DEBUG) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        eventBus = EventBus.getDefault();
    }
}
