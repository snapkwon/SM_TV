package vn.digital.signage.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AutoPlayResponse extends BaseResponse implements Serializable {
    /**
     * hour : 18
     * minute : 45
     * second : 00
     */

    @SerializedName("autoplay")
    private AutoplayEntity mAutoplay;

    private int playAtIndex = 0;

    public AutoplayEntity getAutoplay() {
        return mAutoplay;
    }

    public void setAutoplay(AutoplayEntity autoplay) {
        mAutoplay = autoplay;
    }

    public int getPlayAtIndex() {
        return playAtIndex;
    }

    public void setPlayAtIndex(int playAtIndex) {
        this.playAtIndex = playAtIndex;
    }
}
