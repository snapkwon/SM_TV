package vn.digital.signage.android.util.permission;

/**
 * The interface Permission listener.
 */
public interface PermissionListener {

    /**
     * On request permission granted.
     *
     * @param permissions the permissions
     */
    void onRequestPermissionsGranted(String[] permissions);

    /**
     * On request permission denied.
     *
     * @param permissions the permissions
     */
    void onRequestPermissionsDenied(String[] permissions);
}
