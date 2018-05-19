package vn.digital.signage.android.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.digital.signage.android.feature.client.base.MainActivity;

/**
 * The type Utils.
 */
public class Utils {

    /**
     * The type Build version.
     */
    public static class BuildVersion {

        /**
         * Has gingerbread boolean.
         *
         * @return the boolean
         */
        public static boolean hasGingerbread() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
        }

        /**
         * Has honeycomb boolean.
         *
         * @return the boolean
         */
        public static boolean hasHoneycomb() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        }

        /**
         * Has fro yo boolean.
         *
         * @return the boolean
         */
        public static boolean hasFroYo() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
        }

        /**
         * Has kit kat boolean.
         *
         * @return the boolean
         */
        public static boolean hasKitKat() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        }

        /**
         * Has honeycomb mr 1 boolean.
         *
         * @return the boolean
         */
        public static boolean hasHoneycombMR1() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
        }

        /**
         * Has honeycomb mr 2 boolean.
         *
         * @return the boolean
         */
        public static boolean hasHoneycombMR2() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
        }

        /**
         * Has jelly bean boolean.
         *
         * @return the boolean
         */
        public static boolean hasJellyBean() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        }

    }

    /**
     * The type Ui conversion.
     */
    public static class UIConversion {

        /**
         * Gets pixel from dips.
         *
         * @param context the context
         * @param pixels  the pixels
         * @return the pixel from dips
         */
        public static int getPixelFromDips(Context context, float pixels) {
            // Get the screen's density scale
            final float scale = context.getResources().getDisplayMetrics().density;
            // Convert the dps to pixels, based on density scale
            return (int) (pixels * scale + 0.5f);
        }

        /**
         * Gets full screen view.
         *
         * @param context the context
         * @param view    the view
         * @return the full screen view
         */
        public static RelativeLayout.LayoutParams getFullScreenView(Context context, View view) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    view.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            return params;
        }
    }

    /**
     * The type Formatter.
     */
    public static class Formatter {
        /**
         * Simplify string.
         *
         * @param inputString the input string
         * @return the string
         */
        public static String simplify(String inputString) {
            return inputString.replace("\r\n", "").replace("\n", "").replace("\t", "");
        }

        /**
         * Days between long.
         *
         * @param startStr the start str
         * @param endStr   the end str
         * @return the long
         */
        public static long daysBetween(String startStr, String endStr) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            Date oldDate = null;
            try {
                oldDate = dateFormat.parse(endStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date currentDate = new Date();
            long diffMillis = oldDate.getTime() - currentDate.getTime();
            return diffMillis / (24 * 60 * 60 * 1000);
        }
    }

    /**
     * The type Services.
     */
    public static class Services {
        /**
         * Is service running boolean.
         *
         * @param context   the context
         * @param className the class name
         * @return the boolean
         */
        public static boolean isServiceRunning(Context context, String className) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (className.equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * Start activity with clear all back stack.
     *
     * @param activity the activity
     * @param intent   the intent
     */
    public static void startActivityWithClearAllBackStack(Activity activity, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        ActivityCompat.finishAffinity(activity);
    }

    /**
     * Sha 256 string.
     *
     * @param base the base
     * @return the string
     */
    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Is app running boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services =
                activityManager.getRunningTasks(Integer.MAX_VALUE);
        if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
            return true;
        } else
            return false;
    }
       /* ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                    return true;
                }
            }
        }
        return false;
        }*/
}
