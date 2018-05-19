package com.yepstudio.android.library.autoupdate.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yepstudio.android.library.autoupdate.AutoUpdateLog;
import com.yepstudio.android.library.autoupdate.AutoUpdateLogFactory;
import com.yepstudio.android.library.autoupdate.CheckFileDelegate;
import com.yepstudio.android.library.autoupdate.Version;

import java.io.File;

/**
 * 检查包名是否是否程序一致
 *
 * @author zhangzl@gmail.com
 * @version 1.0, 2014年4月18日
 * @create 2014年4月18日
 */
public class PackageCheckFileDelegate implements CheckFileDelegate {

    private static AutoUpdateLog log = AutoUpdateLogFactory.getAutoUpdateLog(PackageCheckFileDelegate.class);

    @Override
    public boolean doCheck(Context context, Version version, File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String dexPath = file.getAbsolutePath();
        PackageInfo info = context.getPackageManager().getPackageArchiveInfo(dexPath, PackageManager.GET_SIGNATURES);
        log.debug("app PackageName=" + context.getPackageName() + ", APK packageName = " + info.packageName);
        return info != null && context.getPackageName().equals(info.packageName);
    }

}
