package vn.digital.signage.android.utils.enumeration;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static vn.digital.signage.android.utils.enumeration.LogLevel.API;
import static vn.digital.signage.android.utils.enumeration.LogLevel.DATA;
import static vn.digital.signage.android.utils.enumeration.LogLevel.RECEIVER;
import static vn.digital.signage.android.utils.enumeration.LogLevel.SERVICE;
import static vn.digital.signage.android.utils.enumeration.LogLevel.UI;

@Retention(SOURCE)
@IntDef({SERVICE,
        RECEIVER,
        UI,
        API,
        DATA})
public @interface LogLevel {
    int SERVICE = 0x01;
    int RECEIVER = 0x02;
    int UI = 0x04;
    int API = 0x08;
    int DATA = 0x10;
}
