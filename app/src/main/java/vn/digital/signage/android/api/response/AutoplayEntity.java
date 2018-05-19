package vn.digital.signage.android.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AutoplayEntity implements Serializable {
    @SerializedName("hour")
    private int hour;

    @SerializedName("minute")
    private int minute;

    @SerializedName("second")
    private int second;

    public AutoplayEntity() {
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}


