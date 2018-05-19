package vn.digital.signage.android.util.permission;

import android.support.annotation.NonNull;

/**
 * The interface Permission helper.
 */
public interface PermissionHelper {
    /**
     * Request permission.
     *
     * @param permissions the permissions
     */
    void requestPermission(String[] permissions);

    /**
     * Is permission granted boolean.
     *
     * @param permissions the permissions
     * @return the boolean
     */
    String[] getListGrantedPermissions(String[] permissions);

    /**
     * Handle request permissions result.
     *
     * @param requestCode  the request code
     * @param permissions  the permissions
     * @param grantResults the grant results
     */
    void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
