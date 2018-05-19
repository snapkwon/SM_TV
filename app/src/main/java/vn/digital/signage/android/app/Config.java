package vn.digital.signage.android.app;

import android.os.Environment;

import vn.digital.signage.android.BuildConfig;
import vn.digital.signage.android.utils.enumeration.LogLevel;

/**
 * The type Config.
 */
public class Config {

    public static final boolean DEBUG_AUTO_ON_OFF_TIMER = false;
    public static final boolean DEBUG_AUTO_PLAY_AT_FIXED_TIME = false;
    public static final boolean DEBUG_PRE_ENTER_LOGIN_CONFIG = BuildConfig.DEBUG;
    public static final boolean DEBUG_MODE = BuildConfig.DEBUG;

    public static final boolean IS_AUTO_ON_OFF_ENABLED = false;
    public static final boolean IS_AUTO_PLAY_WHEN_SCREEN_MINIMIZED = true;
    public static final boolean IS_HASH_CHECK_ENABLED = true;
    public static final boolean IS_START_APP_AFTER_CRASHED_SCHEDULE_ENABLED = true;

    public static final int MAXIMIZE_AFTER_SECONDS_DURATION = 5;

    public static final int TURN_ON_HOUR_OF_DAY = 6;// turn on hour: 6h am
    public static final int TURN_ON_MINUTE = 0;// turn on minute: 0
    public static final int TURN_ON_SECOND = 0;// turn on second: 0

    public static final int TURN_OFF_HOUR_OF_DAY = 22;// turn off hour: 10h pm
    public static final int TURN_OFF_MINUTE = 0;// turn off minute: 0
    public static final int TURN_OFF_SECOND = 0;// turn off second: 0

    public static final int ONE_DAY_IN_HOUR = 24;

    public static final long ONE_SECOND = 1000;

    public static final int ONE_MINUTE = 60;

    public static final long IMAGE_DURATION = 30000L; // for test
    // configure log level
    public static final int CONFIG_LOG_LEVEL = LogLevel.API
            | LogLevel.RECEIVER
            //| LogLevel.DATA
            | LogLevel.UI
            | LogLevel.SERVICE;

    public static boolean hasLogLevel(@LogLevel int logLevel) {
        return (logLevel & Config.CONFIG_LOG_LEVEL) > 0;
    }

    public static final class OverallConfig {
        // shared preference config
        public static final String APP_PREFERENCES = "smg_digital_signage_prefs";

        // declare assert's path
        public static final String URL_APP_DEFAULT = "http://starmedia.vn/digital";

        public static final long FACE_INFO_INTERVAL_IN_MS = 1 * 60 * 1000L; // 10 mins

        public static final long GET_PLAYLIST_INTERVAL_IN_MS = 60 * 1000L; // 60 seconds

        public static final long UPDATE_INFO_NOTIFY_INTERVAL = 6 * 30 * 1000L; // 30 seconds Thoai modified 15/03/2017 to 3 minutes

        public static final long AUTO_UPDATE_INFO_NOTIFY_INTERVAL = 4 * 60 * 60 * 1000; // 4 hour

        public static final long CHECK_AUTO_PLAY_NOTIFY_INTERVAL = 60 * 1000; // 60 seconds

        public static final String LINK_DOWNLOAD = "%s/files/%s";

        public static final String LINK_DOWNLOAD_UPDATE = "%s/sync/checknewversion";

        public static final String FOLDER_VIDEO = "videos";

        public static final String FOLDER_PACKAGE_VIDEO = "vn.digital.signage.video";

        public static final String FOLDER_DEFAULT = Environment.getExternalStorageDirectory().getAbsoluteFile().getPath() + "/Android/data/";

        public static final String FOLDER_PATH = FOLDER_DEFAULT + FOLDER_PACKAGE_VIDEO + "/%s";

        public static final String PATH_FOLDER_VIDEO = FOLDER_PATH + FOLDER_VIDEO;
    }

    public static class NetworkApiConfig {
        public static final long DEFAULT_CONNECTION_TIME_OUT = 2L; // 2 minutes
        public static final int MAX_CONNECTION_POOL = 1;
        public static final int DEFAULT_KEEP_ALIVE_DURATION = 30 * 1000;
    }
}
