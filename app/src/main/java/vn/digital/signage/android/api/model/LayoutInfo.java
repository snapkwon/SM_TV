package vn.digital.signage.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LayoutInfo {

    public enum LayoutType {
        @SerializedName("1")
        IMAGE(1),
        @SerializedName("2")
        VIDEO(2),
        @SerializedName("3")
        URL(2),
        @SerializedName("5")
        FRAME(5),
        UNKNOWN(-1);

        private int type;

        LayoutType(int type) {
            this.type = type;
        }
    }

    private Long id;

    @SerializedName("group_id")
    private Long groupId;

    private String name;

    private LayoutType type;

    private String source;

    private String assets;

    private String data;

    private DataInfo objData;

    private ArrayList<SourceInfo> objSource;

    private Integer duration;

    @SerializedName("hash")
    private String hash;

    @SerializedName("play_from")
    private String playFrom;

    @SerializedName("play_to")
    private String playTo;

    @SerializedName("dontplay_from")
    private String dontPlayFrom;

    @SerializedName("dontplay_to")
    private String dontPlayTo;

    private Integer position;

    @SerializedName("transition_id")
    private String transitionId;

    @SerializedName("transition_in_class")
    private String transitionInClass;

    @SerializedName("transition_out_class")
    private String transitionOutClass;

    private Integer enabled;

    @SerializedName("last_update")
    private String lastUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LayoutType getType() {
        return type;
    }

    public void setType(LayoutType type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPlayFrom() {
        return playFrom;
    }

    public void setPlayFrom(String playFrom) {
        this.playFrom = playFrom;
    }

    public String getPlayTo() {
        return playTo;
    }

    public void setPlayTo(String playTo) {
        this.playTo = playTo;
    }

    public String getDontPlayFrom() {
        return dontPlayFrom;
    }

    public void setDontPlayFrom(String dontPlayFrom) {
        this.dontPlayFrom = dontPlayFrom;
    }

    public String getDontPlayTo() {
        return dontPlayTo;
    }

    public void setDontPlayTo(String dontPlayTo) {
        this.dontPlayTo = dontPlayTo;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getTransitionId() {
        return transitionId;
    }

    public void setTransitionId(String transitionId) {
        this.transitionId = transitionId;
    }

    public String getTransitionInClass() {
        return transitionInClass;
    }

    public void setTransitionInClass(String transitionInClass) {
        this.transitionInClass = transitionInClass;
    }

    public String getTransitionOutClass() {
        return transitionOutClass;
    }

    public void setTransitionOutClass(String transitionOutClass) {
        this.transitionOutClass = transitionOutClass;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public DataInfo getObjData() {
        return objData;
    }

    public void setObjData(DataInfo objData) {
        this.objData = objData;
    }

    public ArrayList<SourceInfo> getObjSource() {
        return objSource;
    }

    public void setObjSource(ArrayList<SourceInfo> objSource) {
        this.objSource = objSource;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
