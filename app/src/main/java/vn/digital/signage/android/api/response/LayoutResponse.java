package vn.digital.signage.android.api.response;

import java.io.Serializable;
import java.util.List;

import vn.digital.signage.android.api.model.LayoutInfo;

public class LayoutResponse extends BaseResponse implements Serializable {

    private List<LayoutInfo> layouts;

    public List<LayoutInfo> getLayouts() {
        return layouts;
    }

    public void setLayouts(List<LayoutInfo> layouts) {
        this.layouts = layouts;
    }

}

