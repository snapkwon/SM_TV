package vn.digital.signage.android.api.response;

import java.io.Serializable;

import vn.digital.signage.android.api.model.RegisterInfo;

public class RegisterDetailResponse extends BaseResponse implements Serializable {

    private RegisterInfo info;

    private String msg;

    private String error;

    public RegisterInfo getInfo() {
        return info;
    }

    public void setInfo(RegisterInfo info) {
        this.info = info;
    }

}
