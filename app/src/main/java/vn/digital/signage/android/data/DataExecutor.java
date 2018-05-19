package vn.digital.signage.android.data;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.apache.log4j.Logger;

import io.paperdb.Paper;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.data.model.SmData;
import vn.digital.signage.android.utils.FileUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class DataExecutor {
    public static final String PREF_FILE_NAME = String.format(Config.OverallConfig.FOLDER_PATH, "data.json");
    public static final String PREF_FILE_NAME_BAK = String.format(Config.OverallConfig.FOLDER_PATH, "data_bak.json");
    private static final Logger log = Logger.getLogger(DataExecutor.class);
    private static final String DATA_KEY = "DATA_KEY";
    private static final String DATA_KEY_BAK = "DATA_KEY_BAK";
    private static EncryptHelper encryptHelper = EncryptHelper.getInstance();

    public static SmData loadData() {
        SmData info = Paper.book().read(DATA_KEY);

        if (info == null) {
            if (Config.hasLogLevel(LogLevel.DATA))
                log.debug(String.format("loadData - preparing load data"));

            try {
                if (Config.hasLogLevel(LogLevel.DATA))
                    log.debug(String.format("loadData - preparing load data - load file: %s", PREF_FILE_NAME));
                String data = FileUtils.readFileFromExternalStorage(App.getInstance(), PREF_FILE_NAME);

                if (!TextUtils.isEmpty(data)) {
                    if (Config.hasLogLevel(LogLevel.DATA))
                        log.debug(String.format("loadData - data is not empty - data: %s", data));
                    info = (new Gson()).fromJson(encryptHelper.decrypt(data), SmData.class);
                }

                // if info does not contains user info, so load it from bak data
                if (info == null) {
                    info = loadBakData();
                    if (Config.hasLogLevel(LogLevel.DATA))
                        log.debug(String.format("loadData - data is empty - load from bak data - %s", info));
                } else if (info.getRegisterInfo() == null) {
                    if (Config.hasLogLevel(LogLevel.DATA))
                        log.debug(String.format("loadData - data is not empty but register info is null"));
                    SmData bakInfo = loadBakData();
                    if (bakInfo != null) {
                        info = syncData(info, bakInfo);
                        if (Config.hasLogLevel(LogLevel.DATA))
                            log.debug(String.format("loadData - data is not empty but register info is null - sync bak data - %s", (new Gson()).toJson(info)));
                    }
                }
            } catch (Exception e) {
                if (Config.hasLogLevel(LogLevel.DATA))
                    log.error(String.format("loadData - readFileFromExternalStorage - error - %s", e.getMessage()));
            }

            if (Config.hasLogLevel(LogLevel.DATA))
                log.debug(String.format("loadData - readFileFromExternalStorage - data - %s", (new Gson()).toJson(info)));


            if (info != null && info.getRegisterInfo() != null) {
                saveData(info);
            }
        } else {
            if (Config.hasLogLevel(LogLevel.DATA))
                log.debug(String.format("loadData - readFromDb - data - %s", (new Gson()).toJson(info)));
        }

        return info;
    }

    private static SmData syncData(SmData data, SmData bakData) {
        data.setRegisterInfo(bakData.getRegisterInfo());
        data.setLayoutResponse(bakData.getLayoutResponse());
        data.setAssetsListResponse(bakData.getAssetsListResponse());
        data.setPrefUrl(bakData.getPrefUrl());
        data.setPrefName(bakData.getPrefName());
        data.setPrefFileOn(bakData.getPrefFileOn());
        data.setPrefTimeOn(bakData.getPrefTimeOn());
        data.setPrefFileOff(bakData.getPrefFileOff());
        data.setPrefTimeOff(bakData.getPrefTimeOff());
        data.setPrefProgressStart(bakData.isPrefProgressStart());
        data.setPrefFolderVideo(bakData.getPrefFolderVideo());
        data.setPrefLinkAutoUpdate(bakData.getPrefLinkAutoUpdate());
        data.setLayoutInfo(bakData.getLayoutInfo());
        data.setAutoPlayResponse(bakData.getAutoPlayResponse());
        data.setOnOffTimerResponse(bakData.getOnOffTimerResponse());
        return data;
    }

    public static SmData loadBakData() {
        return Paper.book().read(DATA_KEY_BAK);
    }

    public static SmData loadBakDataIntance() {
        SmData info = loadBakData();
        if (info == null)
            info = new SmData();
        return info;
    }

    public static void saveData(SmData smData) {
        if (Config.hasLogLevel(LogLevel.DATA))
            log.debug(String.format("saveData - write to database - data - %s", (new Gson()).toJson(smData)));
        Paper.book().write(DATA_KEY, smData);
        if (Config.hasLogLevel(LogLevel.DATA))
            log.debug(String.format("saveData - write to database - data - success"));
    }

    public static void saveBakData(SmData smData) {
        if (Config.hasLogLevel(LogLevel.DATA))
            log.debug(String.format("saveBakData - write to database - data - %s", (new Gson()).toJson(smData)));
        Paper.book().write(DATA_KEY_BAK, smData);
        if (Config.hasLogLevel(LogLevel.DATA))
            log.debug(String.format("saveBakData - write to database - data - success"));
    }
}
