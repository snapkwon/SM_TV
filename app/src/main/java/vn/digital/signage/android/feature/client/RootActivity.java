package vn.digital.signage.android.feature.client;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.esotericsoftware.minlog.Log;

import org.apache.log4j.Level;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import io.paperdb.Paper;
import vn.digital.signage.android.R;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.base.MainActivity;
import vn.digital.signage.android.feature.client.test.facedetection.TestFaceDetectionActivity;
import vn.digital.signage.android.util.permission.PermissionHelper;
import vn.digital.signage.android.util.permission.PermissionHelperImpl;
import vn.digital.signage.android.util.permission.PermissionListener;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.asynctask.CopyAssetToSd;

import static vn.digital.signage.android.app.Config.ONE_MINUTE;
import static vn.digital.signage.android.app.Config.ONE_SECOND;

public class RootActivity extends FragmentActivity {

    private static final Handler handler = new Handler();

    private static final String[] permissions = new String[]{
//            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    protected PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        Paper.init(this);

        permissionHelper = new PermissionHelperImpl(this, new PermissionListener() {
            @Override
            public void onRequestPermissionsGranted(String[] permissions) {
                requestPermissionGranted(permissions);
            }

            @Override
            public void onRequestPermissionsDenied(String[] permissions) {
                requestPermissionDenied(permissions);
            }
        });

        if (permissionHelper.getListGrantedPermissions(permissions).length < permissions.length) {
            permissionHelper.requestPermission(permissions);
        } else {
            requestPermissionGranted(permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissionsList, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissionsList, grantResults);
    }

    protected void requestPermissionGranted(String[] permissions) {
//        DebugLog.d("RootActivity - requestPermissionGranted");

        // Config Logs
        setLogConfigurator();

        goToNextScreen();
        //goToTestFaceActivity();

        new CopyAssetToSd(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    protected void requestPermissionDenied(String[] permissions) {
        Log.info("RootActivity - requestPermissionDenied");
        this.finish();
    }

    private void goToNextScreen() {
        Utils.startActivityWithClearAllBackStack(
                RootActivity.this,
                MainActivity.intentInstance(RootActivity.this));
    }

    private void goToTestFaceActivity() {
        Utils.startActivityWithClearAllBackStack(
                RootActivity.this,
                TestFaceDetectionActivity.intentInstance(RootActivity.this));
    }

    public static void startRootActivity(final Context context) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.info("RootActivity - initiate root activity");
                Intent i = new Intent(context, RootActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }, ONE_MINUTE * ONE_SECOND);
    }

    public static void startRootActivity(final Context context, int delaySeconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent i = new Intent(context, RootActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        }, delaySeconds * ONE_SECOND);
    }

    private void setLogConfigurator() {
        final LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Config.OverallConfig.FOLDER_DEFAULT
                + File.separator + Config.OverallConfig.FOLDER_PACKAGE_VIDEO + File.separator + "logs"
                + File.separator + "digital-signage.txt");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024L * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }
}
