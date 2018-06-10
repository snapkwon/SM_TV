package vn.digital.signage.android.feature.server;

import android.app.Service;

import java.util.Timer;

/**
 * Created by Admin on 6/10/18.
 */

public abstract class BaseService extends Service{

    // timer handling
    protected Timer timer = null;

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
