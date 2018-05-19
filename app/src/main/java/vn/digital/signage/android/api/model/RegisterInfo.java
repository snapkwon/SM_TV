package vn.digital.signage.android.api.model;

import java.io.Serializable;

public class RegisterInfo implements Serializable {

    private Long id;
    private String secret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {

        return "RegisterInfo{" +
                "id='" + id + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
