package vn.digital.signage.android.api.response;

import java.io.Serializable;
import java.util.List;

import vn.digital.signage.android.api.model.AssetsInfo;

public class AssetsListResponse extends BaseResponse implements Serializable {

    private List<AssetsInfo> assets;

    public List<AssetsInfo> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsInfo> assets) {
        this.assets = assets;
    }

}
