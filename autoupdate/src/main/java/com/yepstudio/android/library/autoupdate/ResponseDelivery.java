package com.yepstudio.android.library.autoupdate;

import java.io.IOException;

/**
 * @param <T>
 * @author zzljob@gmail.com
 * @version 1.0, 2014年6月16日
 * @create 2014年6月16日
 */
public interface ResponseDelivery<T> {

    T submitRequest(RequestInfo requestInfo) throws IOException;

    Version parserResponse(T before) throws Exception;

}
