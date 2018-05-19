package vn.digital.signage.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OnOffTimerResponse extends BaseResponse implements Serializable {

    @SerializedName("time_on")
    private AutoplayEntity timeOn;

    @SerializedName("time_off")
    private AutoplayEntity timeOff;

    public AutoplayEntity getTimeOn() {
        return timeOn;
    }

    public void setTimeOn(AutoplayEntity timeOn) {
        this.timeOn = timeOn;
    }

    public AutoplayEntity getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(AutoplayEntity timeOff) {
        this.timeOff = timeOff;
    }
}
