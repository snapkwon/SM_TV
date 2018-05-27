package vn.digital.signage.android.api.model;

import android.content.res.Resources;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZINNO on 5/24/2018.
 */

public class SourceInfo {

    public enum SourceType {
        @SerializedName("video")
        VIDEO("video"),
        @SerializedName("url")
        URL("url"),
        UNKNOWN("");

        private String type;

        SourceType(String type) {
            this.type = type;
        }
    }

    private int name;
    @SerializedName("z-index")
    private int z_index;
    private int width;
    private int height;
    private int top;
    private int left;
    private String time;
    @SerializedName("soure")
    private String source;
    private String hash;
    private SourceType type;

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getZ_index() {
        return z_index;
    }

    public void setZ_index(int z_index) {
        this.z_index = z_index;
    }

    public int getWidth() {

        return getDpFromUnit(width);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return getDpFromUnit(height);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTop() {
        return getDpFromUnit(top);
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return getDpFromUnit(left);
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public static int getDpFromUnit(int unit) {
        return unit * Resources.getSystem().getDisplayMetrics().widthPixels / 1280;
    }
}
