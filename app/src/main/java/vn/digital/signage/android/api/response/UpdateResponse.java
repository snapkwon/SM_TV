package vn.digital.signage.android.api.response;

import com.google.gson.annotations.SerializedName;

import vn.digital.signage.android.api.model.NewVersion;

public class UpdateResponse {

    @SerializedName("newversion")
    private NewVersion version;

    public NewVersion getVersion() {
        return version;
    }

    public void setVersion(NewVersion version) {
        this.version = version;
    }
}
