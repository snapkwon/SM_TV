package com.yepstudio.android.library.autoupdate;

import android.content.Context;

import java.io.File;

/**
 * 检查文件的在合法性
 *
 * @author zhangzl@gmail.com
 * @version 1.0, 2014年4月17日
 * @create 2014年4月17日
 */
public interface CheckFileDelegate {

    /**
     * 检查的过程，不需要考虑时间问题，会做异步调用
     *
     * @param module
     * @param context
     * @param version
     * @param file    文件
     * @return 检查结果
     */
    public boolean doCheck(Context context, Version version, File file);

}
