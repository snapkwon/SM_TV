package com.yepstudio.android.library.autoupdate;

import android.content.Context;

import java.io.File;

/**
 * @author zzljob@gmail.com
 * @version 1.0，2014年4月20日
 * @create 2014年4月20日
 */
public interface InstallExecutor {

    public void install(Context context, File file);

}
