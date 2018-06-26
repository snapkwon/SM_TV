package vn.digital.signage.android.data.prefs;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import vn.digital.signage.android.Constants;
import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.model.RegisterInfo;
import vn.digital.signage.android.api.response.AssetsListResponse;
import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.api.response.FileResponse;
import vn.digital.signage.android.api.response.LayoutResponse;
import vn.digital.signage.android.api.response.OnOffTimerResponse;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.security.ObscuredSharedPreferences;

public class SmSharedPreferenceRuntime {
    private String PREF_CURRENT_LAYOUT = Utils.sha256("PREF_CURRENT_LAYOUT");
    private String PREF_AUTO_PLAY_SCHEDULE = Utils.sha256("PREF_AUTO_PLAY_SCHEDULE");
    private String PREF_ON_OFF_TIMER = Utils.sha256("PREF_ON_OFF_TIMER");
    private String PREF_IS_DOWNLOADED = Utils.sha256("PREF_IS_DOWNLOADED");
    private String PREF_MEDIA_DOWNLOADED = Utils.sha256("PREF_MEDIA_DOWNLOADED");

    private ObscuredSharedPreferences mPref;
    private Context mContext;
    private Gson gson;

    public SmSharedPreferenceRuntime(Context context, Gson gson) {
        this.gson = gson;
        init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        mPref = ObscuredSharedPreferences.getPrefs(mContext,
                Config.OverallConfig.APP_PREFERENCES,
                Context.MODE_PRIVATE);
    }

    public void clearAll() {
        ObscuredSharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();
    }

    public RegisterInfo getPrefRegister() {
        RegisterInfo info = null;
        String value = mPref.getString(Constants.PREF_REGISTER_INFO, "");
        if (!value.isEmpty() && !TextUtils.isEmpty(value)) {
            info = gson.fromJson(value, RegisterInfo.class);
        }
        return info;
    }

    public void setPrefRegister(RegisterInfo value) {
        mPref.edit().putString(Constants.PREF_REGISTER_INFO, gson.toJson(value, RegisterInfo.class)).commit();
    }

    public LayoutResponse getPrefLayoutInfo() {
        LayoutResponse info = null;
        String value = mPref.getString(Constants.PREF_LAYOUT_INFO, "");
        if (!value.isEmpty() && !TextUtils.isEmpty(value)) {
            info = gson.fromJson(value, LayoutResponse.class);
        }
        return info;
    }

    public void setPrefLayoutInfo(LayoutResponse value) {
        String json = gson.toJson(value, LayoutResponse.class);
        mPref.edit().putString(Constants.PREF_LAYOUT_INFO, json).commit();
    }

    public AssetsListResponse getPrefAssetsListInfo() {
        AssetsListResponse info = null;
        String value = mPref.getString(Constants.PREF_ASSETS_LIST_INFO, "");
        if (value != null && !TextUtils.isEmpty(value)) {
            info = gson.fromJson(value, AssetsListResponse.class);
        }
        return info;
    }

    public void setPrefAssetsListInfo(AssetsListResponse value) {
        String json = gson.toJson(value, AssetsListResponse.class);
        mPref.edit().putString(Constants.PREF_ASSETS_LIST_INFO, json).commit();
    }

    public FileResponse getPrefDefaultLayoutInfo() {
        FileResponse info = null;
        String value = mPref.getString(Constants.PREF_DEFAULT_VIDEO_INFO, "");
        if (!value.isEmpty()) {
            info = gson.fromJson(value, FileResponse.class);
        }
        return info;
    }

    public void setPrefDefaultLayoutInfo(FileResponse value) {
        String json = gson.toJson(value, FileResponse.class);
        mPref.edit().putString(Constants.PREF_DEFAULT_VIDEO_INFO, json).commit();
    }

    public String getPrefUrl() {
        return mPref.getString(Constants.PREF_URL_INFO, "");
    }

    public void setPrefUrl(String value) {
        mPref.edit().putString(Constants.PREF_URL_INFO, value).commit();
    }

    public String getPrefName() {
        return mPref.getString(Constants.PREF_NAME_INFO, "");
    }

    public void setPrefName(String value) {
        mPref.edit().putString(Constants.PREF_NAME_INFO, value).commit();
    }

    public String getPrefFileON() {
        return mPref.getString(Constants.PREF_FILE_ON, "");
    }

    public void setPrefFileON(String value) {
        mPref.edit().putString(Constants.PREF_FILE_ON, value).commit();
    }

    public long getPrefTimeON() {
        return mPref.getLong(Constants.PREF_TIME_ON, 0);
    }

    public void setPrefTimeON(long value) {
        mPref.edit().putLong(Constants.PREF_TIME_ON, value).commit();
    }

    public long getPrefTimeOFF() {
        return mPref.getLong(Constants.PREF_TIME_OFF, 0);
    }

    public void setPrefTimeOFF(long value) {
        mPref.edit().putLong(Constants.PREF_TIME_OFF, value).commit();
    }

    public String getPrefFileOFF() {
        return mPref.getString(Constants.PREF_FILE_OFF, "");
    }

    public void setPrefFileOFF(String value) {
        mPref.edit().putString(Constants.PREF_FILE_OFF, value).commit();
    }

    public boolean getPrefProgressStart() {
        return mPref.getBoolean(Constants.PREF_START_PROGRESS_INFO, true);
    }

    public void setPrefProgressStart(boolean value) {
        mPref.edit().putBoolean(Constants.PREF_START_PROGRESS_INFO, value).commit();
    }

    public String getPrefFolderVideo() {
        return mPref.getString(Constants.PREF_FOLDER_VIDEO, "media");
    }

    public void setPrefFolderVideo(String value) {
        mPref.edit().putString(Constants.PREF_FOLDER_VIDEO, value).commit();
    }

    public boolean isPostHistory() {
        return mPref.getBoolean(Constants.PREF_POST_HISTORY, false);
    }

    public void setPostHistory(boolean isPost) {
        mPref.edit().putBoolean(Constants.PREF_POST_HISTORY, isPost).commit();
    }

    public String getPrefLinkAutoUpdate() {
        return mPref.getString(Constants.PREF_LINK_AUTO_UPDATE, null);
    }

    public void setPrefLinkAutoUpdate(String linkUpdate) {
        mPref.edit().putString(Constants.PREF_LINK_AUTO_UPDATE, linkUpdate).commit();
    }

    public LayoutInfo getCurrentLayout() {
        String response = mPref.getString(PREF_CURRENT_LAYOUT, "");
        if (!response.isEmpty()) {
            return gson.fromJson(response, LayoutInfo.class);
        } else {
            return null;
        }
    }

    public void setCurrentLayout(LayoutInfo layoutInfo) {
        mPref.edit().putString(PREF_CURRENT_LAYOUT, gson.toJson(layoutInfo)).commit();
    }

    public Boolean getPlaylistDownloaded() {
        return mPref.getBoolean(PREF_IS_DOWNLOADED, false);
    }

    public void setPlaylistDownloaded(boolean isFetched) {
        mPref.edit().putBoolean(PREF_IS_DOWNLOADED, isFetched).commit();
    }

    public Set<String> getMediaDownloaded() {
        return mPref.getStringSet(PREF_IS_DOWNLOADED, new HashSet<String>());
    }

    public void setMediaDownloaded(Set<String> mediaDownloaded) {
        mPref.edit().putStringSet(PREF_IS_DOWNLOADED, mediaDownloaded).commit();
    }

    public AutoPlayResponse getAutoPlaySchedule() {
        String response = mPref.getString(PREF_AUTO_PLAY_SCHEDULE, "");
        if (!response.isEmpty()) {
            return gson.fromJson(response, AutoPlayResponse.class);
        } else {
            return new AutoPlayResponse();
        }
    }

    public void setAutoPlaySchedule(AutoPlayResponse response) {
        mPref.edit().putString(PREF_AUTO_PLAY_SCHEDULE, gson.toJson(response)).apply();
    }

    public OnOffTimerResponse getOnOffTimer() {
        String response = mPref.getString(PREF_ON_OFF_TIMER, "");
        if (!response.isEmpty()) {
            return gson.fromJson(response, OnOffTimerResponse.class);
        } else {
            return new OnOffTimerResponse();
        }
    }

    public void setOnOffTimer(OnOffTimerResponse response) {
        mPref.edit().putString(PREF_ON_OFF_TIMER, gson.toJson(response)).apply();
    }
}
