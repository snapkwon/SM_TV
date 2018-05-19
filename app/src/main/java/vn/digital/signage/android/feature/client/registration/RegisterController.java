package vn.digital.signage.android.feature.client.registration;

import com.google.gson.Gson;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedString;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.api.response.RegisterResponse;
import vn.digital.signage.android.api.service.DigitalSignageApi;
import vn.digital.signage.android.feature.client.base.BaseController;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class RegisterController extends BaseController {
    private final Logger log = Logger.getLogger(RegisterController.class);

    @Inject
    SMRuntime runtime;

    @Inject
    public RegisterController() {
    }

    public void doRegister(final String name, String secret, Long totalMemory, Long freeMemory, final String url) {
        initRestAdapter(url);

        final MultipartTypedOutput multipart = new MultipartTypedOutput();
        multipart.addPart(DigitalSignageApi.NAME, new TypedString(name));
        multipart.addPart(DigitalSignageApi.SECRET, new TypedString(secret));
        multipart.addPart(DigitalSignageApi.TOTAL_MEMORY, new TypedString(String.valueOf(totalMemory)));
        multipart.addPart(DigitalSignageApi.FREE_MEMORY, new TypedString(String.valueOf(freeMemory)));

        if (Config.hasLogLevel(LogLevel.API))
            log.debug(String.format("RegisterController - call register with name: %s", name));
        api.doRegister(multipart, name, secret, String.valueOf(totalMemory),
                String.valueOf(freeMemory), new Callback<String>() {

                    @Override
                    public void success(String json, Response response) {

                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(String.format("RegisterController - response doRegister - success: %s ", json));

                        final RegisterResponse result = new Gson().fromJson(json, RegisterResponse.class);
                        result.setSuccess(true);
                        result.setError("Success");
                        runtime.setApiUrl(url);
                        runtime.setAccountUserName(name);
                        eventBus.post(result);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.error(String.format("HomeController - response getCurrentPlaylist - error: - %s", retrofitError.getMessage()));


                        final RegisterResponse result = new RegisterResponse();
                        result.setSuccess(false);
                        result.setError("Error");
                        result.setMsg(retrofitError.getLocalizedMessage());
                        eventBus.post(result);
                    }
                });
    }
}
