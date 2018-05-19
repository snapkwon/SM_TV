package com.yepstudio.android.library.autoupdate.internal;

import android.content.Context;

import com.yepstudio.android.library.autoupdate.CheckFileDelegate;
import com.yepstudio.android.library.autoupdate.Version;

import java.io.File;

public class NoneCheckFileDelegate implements CheckFileDelegate {

    @Override
    public boolean doCheck(Context context, Version version, File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

}
