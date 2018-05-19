package vn.digital.signage.android.api.model;

import java.io.Serializable;

public class ErrorInfo implements Serializable {

    private String msg;

    private String error;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
