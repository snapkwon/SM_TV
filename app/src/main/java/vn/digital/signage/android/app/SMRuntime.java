package vn.digital.signage.android.app;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.model.RegisterInfo;
import vn.digital.signage.android.api.response.AssetsListResponse;
import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.api.response.LayoutResponse;
import vn.digital.signage.android.api.response.OnOffTimerResponse;
import vn.digital.signage.android.data.DataExecutor;
import vn.digital.signage.android.data.model.SmData;
import vn.digital.signage.android.data.prefs.SmSharedPreferenceRuntime;
import vn.digital.signage.android.model.FaceDetectionInfos;

@Singleton
public class SMRuntime {
    private static final Logger log = Logger.getLogger(SMRuntime.class);
    private static SMRuntime instance;

    private SmSharedPreferenceRuntime mSpRuntime;
    private Gson gson;
    private SmData smData;

    @Inject
    public SMRuntime() {
        instance = this;
        gson = new Gson();
    }

    public static SMRuntime getInstance() {
        if (instance == null)
            instance = new SMRuntime();
        return instance;
    }

    public void init(Context context) {
        mSpRuntime = new SmSharedPreferenceRuntime(context, gson);

        smData = DataExecutor.loadData();

        if (smData == null) {
            smData = new SmData();
            syncData(smData);
        }

        //if bak data is null, create new instance of bak data
        if (smData != null && smData.getRegisterInfo() != null && DataExecutor.loadBakData() == null) {
            DataExecutor.saveBakData(smData);
        }


        log.info(String.format("loadData - after init runtime  - success - %s", gson.toJson(smData)));
    }

    private void syncData(SmData data) {
        data.setRegisterInfo(mSpRuntime.getPrefRegister());
        data.setLayoutResponse(mSpRuntime.getPrefLayoutInfo());
        data.setAssetsListResponse(mSpRuntime.getPrefAssetsListInfo());
        data.setPrefUrl(mSpRuntime.getPrefUrl());
        data.setPrefName(mSpRuntime.getPrefName());
        data.setPrefFileOn(mSpRuntime.getPrefFileON());
        data.setPrefTimeOn(mSpRuntime.getPrefTimeON());
        data.setPrefFileOff(mSpRuntime.getPrefFileOFF());
        data.setPrefTimeOff(mSpRuntime.getPrefTimeOFF());
        data.setPrefProgressStart(mSpRuntime.getPrefProgressStart());
        data.setPrefFolderVideo(mSpRuntime.getPrefFolderVideo());
        data.setPrefLinkAutoUpdate(mSpRuntime.getPrefLinkAutoUpdate());
        data.setLayoutInfo(mSpRuntime.getCurrentLayout());
        data.setAutoPlayResponse(mSpRuntime.getAutoPlaySchedule());
        data.setOnOffTimerResponse(mSpRuntime.getOnOffTimer());

        DataExecutor.saveData(data);
    }

    public RegisterInfo getRegisterInfo() {
        return smData.getRegisterInfo();
    }

    public void setRegisterInfo(RegisterInfo value) {
        smData.setRegisterInfo(value);
        DataExecutor.saveData(smData);

        if (value != null) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setRegisterInfo(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public LayoutResponse getLayoutResponse() {
        return smData.getLayoutResponse();
    }

    public void setLayoutResponse(LayoutResponse value) {
        smData.setLayoutResponse(value);

        DataExecutor.saveData(smData);

        if (value != null) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setLayoutResponse(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public AssetsListResponse getAssetsListResponse() {
        return smData.getAssetsListResponse();
    }

    public void setAssetsListResponse(AssetsListResponse value) {
        smData.setAssetsListResponse(value);

        DataExecutor.saveData(smData);

        if (value != null) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setAssetsListResponse(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public String getApiUrl() {
        return smData.getPrefUrl();
    }

    public void setApiUrl(String value) {
        smData.setPrefUrl(value);

        DataExecutor.saveData(smData);

        if (!TextUtils.isEmpty(value)) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefUrl(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public String getAccountUserName() {
        return smData.getPrefName();
    }

    public void setAccountUserName(String value) {
        smData.setPrefName(value);

        DataExecutor.saveData(smData);

        if (!TextUtils.isEmpty(value)) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefName(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public String getMediaFileNameOn() {
        return smData.getPrefFileOn();
    }

    public void setMediaFileNameOn(String value) {
        smData.setPrefFileOn(value);

        DataExecutor.saveData(smData);

        if (!TextUtils.isEmpty(value)) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefFileOn(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public long getMediaTimeOn() {
        return smData.getPrefTimeOn();
    }

    public void setMediaTimeOn(long value) {
        smData.setPrefTimeOn(value);

        DataExecutor.saveData(smData);

        if (value > 0) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefTimeOn(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public long getMediaTimeOff() {
        return smData.getPrefTimeOff();
    }

    public void setMediaTimeOff(long value) {
        smData.setPrefTimeOff(value);

        DataExecutor.saveData(smData);

        if (value > 0) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefTimeOn(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public String getMediaFileNameOff() {
        return smData.getPrefFileOff();
    }

    public void setMediaFileNameOff(String value) {
        smData.setPrefFileOff(value);

        DataExecutor.saveData(smData);

        if (!TextUtils.isEmpty(value)) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefFileOff(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public boolean getPlaylistProgressStart() {
        return smData.isPrefProgressStart();
    }

    public void setPlaylistProgressStart(boolean value) {
        smData.setPrefProgressStart(value);

        DataExecutor.saveData(smData);

        // save bak data
        SmData bakData = DataExecutor.loadBakDataIntance();
        bakData.setPrefProgressStart(value);
        DataExecutor.saveBakData(bakData);
    }

    public String getFolderVideoPath() {
        //return smData.getPrefFolderVideo();
        return "media";
    }

    public void setFolderVideoPath(String value) {
        smData.setPrefFolderVideo(value);

        DataExecutor.saveData(smData);


        if (!TextUtils.isEmpty(value)) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefFolderVideo(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public String getLinkAutoUpdate() {
        return smData.getPrefLinkAutoUpdate();
    }

    public void setLinkAutoUpdate(String value) {
        smData.setPrefLinkAutoUpdate(value);

        DataExecutor.saveData(smData);

        if (!TextUtils.isEmpty(value)) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setPrefLinkAutoUpdate(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public LayoutInfo getCurrentLayout() {
        return smData.getLayoutInfo();
    }

    public void setCurrentLayout(LayoutInfo value) {
        smData.setLayoutInfo(value);

        DataExecutor.saveData(smData);

        if (value != null) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setLayoutInfo(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public AutoPlayResponse getAutoPlaySchedule() {
        return smData.getAutoPlayResponse();
    }

    public void setAutoPlaySchedule(AutoPlayResponse value) {
        smData.setAutoPlayResponse(value);

        DataExecutor.saveData(smData);

        if (value != null) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setAutoPlayResponse(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public OnOffTimerResponse getOnOffTimer() {
        return smData.getOnOffTimerResponse();
    }

    public void setOnOffTimer(OnOffTimerResponse value) {
        smData.setOnOffTimerResponse(value);

        DataExecutor.saveData(smData);

        if (value != null) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setOnOffTimerResponse(value);
            DataExecutor.saveBakData(bakData);
        }
    }

    public FaceDetectionInfos getFaceDetectionInfos() {
        return smData.getFaceDetectionInfos();
    }

    public void setFaceDetectionInfos(FaceDetectionInfos value) {
        smData.setFaceDetectionInfos(value);

        DataExecutor.saveData(smData);

        if (value != null) {
            // save bak data
            SmData bakData = DataExecutor.loadBakDataIntance();
            bakData.setFaceDetectionInfos(value);
            DataExecutor.saveBakData(bakData);
        }
    }
}