package vn.digital.signage.android.util.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Permission helper.
 */
public class PermissionHelperImpl implements PermissionHelper {

    private static final int MULTIPLE_PERMISSIONS = 10;

    private final FragmentActivity mActivity;
    private PermissionListener mListener;

    /**
     * Instantiates a new Permission helper.
     *
     * @param a        the a
     * @param listener the listener
     */
    public PermissionHelperImpl(FragmentActivity a, PermissionListener listener) {
        mActivity = a;
        mListener = listener;
    }

    @Override
    public void requestPermission(String[] permissions) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(mActivity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public String[] getListGrantedPermissions(String[] permissions) {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            int result = ContextCompat.checkSelfPermission(mActivity, p);
            if (result == PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        return listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]);
    }

    @Override
    public void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mListener.onRequestPermissionsGranted(permissions);// permissions granted.
                } else {
                    mListener.onRequestPermissionsDenied(permissions); // permissions list of don't granted permission
                }
                break;
            default:
                break;
        }
    }
}