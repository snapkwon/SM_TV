package com.yepstudio.android.library.autoupdate.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.yepstudio.android.library.autoupdate.AutoUpdateLog;
import com.yepstudio.android.library.autoupdate.AutoUpdateLogFactory;
import com.yepstudio.android.library.autoupdate.Version;
import com.yepstudio.android.library.autoupdate.VersionComparer;

/**
 * @author zhangzl@gmail.com
 * @version 1.0, 2014年4月18日
 * @create 2014年4月18日
 */
public class SimpleVersionCompare implements VersionComparer {

    private static AutoUpdateLog log = AutoUpdateLogFactory.getAutoUpdateLog(SimpleVersionCompare.class);

    @Override
    public boolean compare(Context context, Version version) {
        if (version == null) {
            log.trace("version is null, not new update version");
            return false;
        }
        int currentVersionCode = 0;
        try {
            PackageInfo pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = pkg.versionCode;
        } catch (NameNotFoundException exp) {
            log.error("get versionCode fail, we think not new update version");
            return false;
        }
        if (version.getCode() > currentVersionCode) {
            String text = String.format("currentVersionCode=%s, version=%s, so find new version.", currentVersionCode, version.getCode());
            log.info(text);
            return true;
        }
        log.info("not find new version.");
        return false;
    }

}
