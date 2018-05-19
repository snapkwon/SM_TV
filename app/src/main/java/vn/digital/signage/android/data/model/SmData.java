package vn.digital.signage.android.data.model;

import java.io.Serializable;

import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.model.RegisterInfo;
import vn.digital.signage.android.api.response.AssetsListResponse;
import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.api.response.LayoutResponse;
import vn.digital.signage.android.api.response.OnOffTimerResponse;
import vn.digital.signage.android.model.FaceDetectionInfos;

public class SmData implements Serializable {

    RegisterInfo registerInfo;

    LayoutResponse layoutResponse;

    AssetsListResponse assetsListResponse;

    String prefUrl;

    String prefName;

    String prefFileOn;

    long prefTimeOn;

    String prefFileOff;

    long prefTimeOff;

    boolean prefProgressStart;

    String prefFolderVideo;

    String prefLinkAutoUpdate;

    LayoutInfo layoutInfo;

    AutoPlayResponse autoPlayResponse;

    OnOffTimerResponse onOffTimerResponse;

    FaceDetectionInfos faceDetectionInfos;

    public RegisterInfo getRegisterInfo() {
        return registerInfo;
    }

    public void setRegisterInfo(RegisterInfo registerInfo) {
        this.registerInfo = registerInfo;
    }

    public LayoutResponse getLayoutResponse() {
        return layoutResponse;
    }

    public void setLayoutResponse(LayoutResponse layoutResponse) {
        this.layoutResponse = layoutResponse;
    }

    public AssetsListResponse getAssetsListResponse() {
        return assetsListResponse;
    }

    public void setAssetsListResponse(AssetsListResponse assetsListResponse) {
        this.assetsListResponse = assetsListResponse;
    }

    public String getPrefUrl() {
        return prefUrl;
    }

    public void setPrefUrl(String prefUrl) {
        this.prefUrl = prefUrl;
    }

    public String getPrefName() {
        return prefName;
    }

    public void setPrefName(String prefName) {
        this.prefName = prefName;
    }

    public String getPrefFileOn() {
        return prefFileOn;
    }

    public void setPrefFileOn(String prefFileOn) {
        this.prefFileOn = prefFileOn;
    }

    public long getPrefTimeOn() {
        return prefTimeOn;
    }

    public void setPrefTimeOn(long prefTimeOn) {
        this.prefTimeOn = prefTimeOn;
    }

    public String getPrefFileOff() {
        return prefFileOff;
    }

    public void setPrefFileOff(String prefFileOff) {
        this.prefFileOff = prefFileOff;
    }

    public long getPrefTimeOff() {
        return prefTimeOff;
    }

    public void setPrefTimeOff(long prefTimeOff) {
        this.prefTimeOff = prefTimeOff;
    }

    public boolean isPrefProgressStart() {
        return prefProgressStart;
    }

    public void setPrefProgressStart(boolean prefProgressStart) {
        this.prefProgressStart = prefProgressStart;
    }

    public String getPrefFolderVideo() {
        return prefFolderVideo;
    }

    public void setPrefFolderVideo(String prefFolderVideo) {
        this.prefFolderVideo = prefFolderVideo;
    }

    public String getPrefLinkAutoUpdate() {
        return prefLinkAutoUpdate;
    }

    public void setPrefLinkAutoUpdate(String prefLinkAutoUpdate) {
        this.prefLinkAutoUpdate = prefLinkAutoUpdate;
    }

    public LayoutInfo getLayoutInfo() {
        return layoutInfo;
    }

    public void setLayoutInfo(LayoutInfo layoutInfo) {
        this.layoutInfo = layoutInfo;
    }

    public AutoPlayResponse getAutoPlayResponse() {
        return autoPlayResponse;
    }

    public void setAutoPlayResponse(AutoPlayResponse autoPlayResponse) {
        this.autoPlayResponse = autoPlayResponse;
    }

    public OnOffTimerResponse getOnOffTimerResponse() {
        return onOffTimerResponse;
    }

    public void setOnOffTimerResponse(OnOffTimerResponse onOffTimerResponse) {
        this.onOffTimerResponse = onOffTimerResponse;
    }

    public FaceDetectionInfos getFaceDetectionInfos() {
        return this.faceDetectionInfos;
    }

    public void setFaceDetectionInfos(FaceDetectionInfos faceDetectionInfos) {
        this.faceDetectionInfos = faceDetectionInfos;
    }
}
