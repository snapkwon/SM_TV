package com.yepstudio.android.library.autoupdate.internal;

import android.content.Context;
import android.widget.Toast;

import com.yepstudio.android.library.autoupdate.AppUpdateService;
import com.yepstudio.android.library.autoupdate.R;
import com.yepstudio.android.library.autoupdate.ResponseListener;
import com.yepstudio.android.library.autoupdate.Version;

/**
 * @author zhangzl@gmail.com
 * @version 1.0, 2014年4月17日
 * @create 2014年4月17日
 */
public class SimpleResponseListener implements ResponseListener {

    @Override
    public void onFoundLatestVersion(Context context, Version version, boolean isAutoUpdate) {

    }

    @Override
    public void onCurrentIsLatest(Context context, boolean isAutoUpdate) {
        if (!isAutoUpdate) {
            AppUpdateService.show(context, R.string.aus__is_latest_version_label, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onResponseError(Context context, boolean isAutoUpdate) {
        if (!isAutoUpdate) {
            if (NetworkUtil.hasNetwork(context)) {
                AppUpdateService.show(context, R.string.aus__network_error, Toast.LENGTH_SHORT);
            } else {
                AppUpdateService.show(context, R.string.aus__network_not_activated, Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onParserError(Context context, boolean isAutoUpdate) {
        if (!isAutoUpdate) {
            AppUpdateService.show(context, R.string.aus__error_check_update, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onStartUpdateCheck(Context context, boolean isAutoUpdate, boolean hasRunning) {
        if (!isAutoUpdate) {
            AppUpdateService.show(context, R.string.aus__check_new_version, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onSkipLatestVersion(Context context, Version version, boolean isAutoUpdate) {
        if (!isAutoUpdate) {
            AppUpdateService.show(context, R.string.aus__is_latest_version_label, Toast.LENGTH_SHORT);
        }
    }
}
