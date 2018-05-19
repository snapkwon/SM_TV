package vn.digital.signage.android.api.request;

import com.google.gson.annotations.SerializedName;

public class AutoPlayRequest {
    @SerializedName("id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
