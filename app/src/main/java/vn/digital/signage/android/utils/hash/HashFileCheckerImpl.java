package vn.digital.signage.android.utils.hash;

import org.apache.log4j.Logger;

import vn.digital.signage.android.Constants;
import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.utils.FileUtils;
import vn.digital.signage.android.utils.HashUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class HashFileCheckerImpl implements HashFileChecker {
    private final Logger log = Logger.getLogger(HashFileCheckerImpl.class);

    private SMRuntime mRuntime;

    public HashFileCheckerImpl(SMRuntime runtime) {
        this.mRuntime = runtime;
    }

    @Override
    public boolean checkInvalidAndRemoveFile(String url) {
        boolean result = true;

        if (Constants.IS_HASH_CHECK_ENABLED) {
            LayoutInfo lInfo = getLayoutItem(url);

            if (lInfo != null
                    && lInfo.getHash() != null
                    && !lInfo.getHash().equalsIgnoreCase(HashUtils.fileToMD5(url))) {
                FileUtils.deleteFileInPath(url);
                if (Config.hasLogLevel(LogLevel.DATA))
                    log.info("deleted file :" + url + " - url MD5: " + lInfo.getHash());
                result = false;
            }
        }
        return result;
    }

    private LayoutInfo getLayoutItem(String url) {
        LayoutInfo infos = null;

        if (mRuntime.getLayoutResponse() != null) {
            for (LayoutInfo info : mRuntime.getLayoutResponse().getLayouts()) {
                if (url.contains(info.getAssets())) {
                    infos = info;
                    break;
                }
            }
        }
        return infos;
    }
}
