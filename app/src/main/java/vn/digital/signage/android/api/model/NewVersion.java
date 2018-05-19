package vn.digital.signage.android.api.model;

import com.google.gson.annotations.SerializedName;

public class NewVersion {

    @SerializedName("newversion")
    private int newVersion;

    @SerializedName("isUpdate")
    private boolean isUpdate;

    @SerializedName("url")
    private String url;

    public int getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(int newVersion) {
        this.newVersion = newVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
}
