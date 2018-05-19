package vn.digital.signage.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The type Face detection info.
 */
public class FaceDetectionInfo implements Serializable {

    /**
     * screen_id : 1112
     * layout_id : 10
     * duration : 30
     * num_people : 5
     */

    @SerializedName("layout_id")
    private Long layoutId;
    @SerializedName("duration")
    private long duration;
    @SerializedName("num_people")
    private int numPeople;

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Gets num people.
     *
     * @return the num people
     */
    public int getNumPeople() {
        return numPeople;
    }

    /**
     * Sets num people.
     *
     * @param numPeople the num people
     */
    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }
}
