package vn.digital.signage.android.feature.client.test.facedetection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import vn.digital.signage.android.R;
import vn.digital.signage.android.feature.client.home.face.BaseFaceDetectionFragment;

public class TestFaceDetectionActivity extends AppCompatActivity {
    private static final String TAG = TestFaceDetectionActivity.class.getSimpleName();
    private BaseFaceDetectionFragment fragment;

    public static Intent intentInstance(Context context) {
        return new Intent(context, TestFaceDetectionActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_face_detection);

        if (savedInstanceState == null) {
            fragment = getFragmentToHost();
            this.hostFragment(fragment);
        }

        //if (!Utils.Services.isServiceRunning(this, ServerSyncFaceInfoService.class.getName()))
        //    startService(getIntentService(ServerSyncFaceInfoService.class));
    }

    private BaseFaceDetectionFragment getFragmentToHost() {
        return TestFaceDetectionFragment.newInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        Log.i(TAG,"onRequestPermissionsResult - TestFaceDetectionActivity");
        fragment.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void hostFragment(BaseFaceDetectionFragment fragment) {
        if (fragment != null && this.getFragmentManager().findFragmentByTag(fragment.getTagName()) == null) {
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment.getInstance(), fragment.getTagFromClassName());
            ft.commit();
        }
    }

    private <T extends Service> Intent getIntentService(Class<T> classes) {
        return new Intent(this, classes);
    }
}
