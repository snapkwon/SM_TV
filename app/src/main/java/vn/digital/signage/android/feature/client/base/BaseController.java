package vn.digital.signage.android.feature.client.base;

import vn.digital.signage.android.api.BaseApi;
import vn.digital.signage.android.api.service.DigitalSignageApi;

public abstract class BaseController extends BaseApi {

    protected DigitalSignageApi api;

    public void initRestAdapter(String serverAddress) {
        getRestAdapter(serverAddress);
        api = restAdapter.create(DigitalSignageApi.class);
    }
}
