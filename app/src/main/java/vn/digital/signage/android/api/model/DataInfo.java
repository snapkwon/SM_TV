package vn.digital.signage.android.api.model;

import com.google.gson.annotations.SerializedName;

public class DataInfo {

    @SerializedName("pause_jukebox")
    private int pauseJukebox;

    @SerializedName("file_name")
    private String fileName;

    @SerializedName("loop_video")
    private int loopVideo;

    @SerializedName("volume")
    private int volume;

    public int getPauseJukebox() {
        return pauseJukebox;
    }

    public void setPauseJukebox(int pauseJukebox) {
        this.pauseJukebox = pauseJukebox;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLoopVideo() {
        return loopVideo;
    }

    public void setLoopVideo(int loopVideo) {
        this.loopVideo = loopVideo;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
