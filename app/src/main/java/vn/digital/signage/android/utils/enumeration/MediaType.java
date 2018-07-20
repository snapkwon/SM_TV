package vn.digital.signage.android.utils.enumeration;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static vn.digital.signage.android.utils.enumeration.MediaType.CLEAR;
import static vn.digital.signage.android.utils.enumeration.MediaType.FRAME;
import static vn.digital.signage.android.utils.enumeration.MediaType.IMAGE;
import static vn.digital.signage.android.utils.enumeration.MediaType.VIDEO;
import static vn.digital.signage.android.utils.enumeration.MediaType.WEB_VIEW;

@Retention(SOURCE)
@IntDef({CLEAR,
        IMAGE,
        WEB_VIEW,
        VIDEO,
        FRAME})
public @interface MediaType {
    int CLEAR = -1;
    int IMAGE = 0x01;
    int WEB_VIEW = 0x02;
    int VIDEO = 0x04;
    int FRAME = 0x08;
}
