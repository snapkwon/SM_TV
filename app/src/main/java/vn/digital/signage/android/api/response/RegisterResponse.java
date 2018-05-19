package vn.digital.signage.android.api.response;

import java.io.Serializable;

import vn.digital.signage.android.api.model.RegisterInfo;

public class RegisterResponse extends BaseResponse implements Serializable {

    private RegisterInfo details;

    public RegisterInfo getDetails() {
        return details;
    }

    public void setDetails(RegisterInfo details) {
        this.details = details;
    }
}
