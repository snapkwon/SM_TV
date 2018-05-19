package com.yepstudio.android.library.autoupdate;

/**
 * 用户操作的抽象接口
 *
 * @author zzljob@gmail.com
 * @version 1.1, 2014年6月16日
 * @create 2014年4月17日
 */
public interface UserOptionsListener {

    /**
     * 当用户选择更新时调用
     */
    public void doUpdate(boolean laterOnWifi);

    /**
     * 当用户选择忽略时调用
     */
    public void doIgnore();
}
