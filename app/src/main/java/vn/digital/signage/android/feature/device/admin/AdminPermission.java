package vn.digital.signage.android.feature.device.admin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import vn.digital.signage.android.R;

public class AdminPermission {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;

    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    private AdminPermissionListener mListener;

    public AdminPermission(Context context) {
        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(context, SmDeviceAdminReceiver.class);
    }

    public void setAdminPermissionListener(AdminPermissionListener listener) {
        mListener = listener;
    }

    public void onRequestAdminPermission(Context context) {
        if (!mDPM.isAdminActive(mAdminName)) {
            // try to become active – must happen here in this activity, to get result
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    context.getString(R.string.app_name));
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        } else {
            // Already is a device administrator, can do security operations now.
            if (mListener != null)
                mListener.onSuccessAdminPermissionGranted();
        }
    }

    public void onReceiveAdminPermission(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (mListener != null)
                mListener.onSuccessAdminPermissionGranted();
        }
    }
}
