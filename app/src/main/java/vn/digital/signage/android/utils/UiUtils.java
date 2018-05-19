package vn.digital.signage.android.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * The type Ui utils.
 */
public class UiUtils {

    /**
     * hide keyboard
     *
     * @param context the context
     * @param v       the v
     */
    public static void hideKeyboard(Context context, View v) {
        if (v == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * hide keyboard
     *
     * @param activity the activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            ((InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getWindow()
                            .getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get screen size of device
     *
     * @param context the context
     * @return screenSize[0] is widthPixels ;  screenSize[1] is heightPixels
     */
    public static int[] getScreenSize(@NonNull Context context) {
        int[] screenSize = new int[2];
        screenSize[0] = context.getResources().getDisplayMetrics().widthPixels;
        screenSize[1] = context.getResources().getDisplayMetrics().heightPixels;
        return screenSize;
    }

    /**
     * set full screen view for both android 19 and android 21
     * for android 21 and later please put android:fitsSystemWindows="true"
     * in first child View inside activity layout
     *
     * @param activity the activity
     */
    public static void setFullScreenView(Activity activity) {
        // hide status bar (worked for sdk < 19 & sdk >= 19)
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide button navigation
        if (Build.VERSION.SDK_INT < 19) {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.GONE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            //for higher api versions.
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * Sets full screen window.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setFullScreenWindow(Activity activity, View view) {
        int uiOptions = view.getSystemUiVisibility();
        uiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        view.setSystemUiVisibility(uiOptions);

        final View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
    }

    /*public void configFullscreenWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }*/
}
