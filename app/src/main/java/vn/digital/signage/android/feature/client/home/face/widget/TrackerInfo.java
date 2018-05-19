package vn.digital.signage.android.feature.client.home.face.widget;

import java.util.concurrent.TimeUnit;

public class TrackerInfo {
    private long startTime;
    private long endTime;
    private int id;

    public TrackerInfo() {
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getViewInSeconds() {
        long timeOffset = endTime - startTime;

        if (timeOffset > 0) {
            return TimeUnit.MILLISECONDS.toSeconds(timeOffset);
        } else {
            return 0;
        }
    }
}
