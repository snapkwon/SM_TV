package vn.digital.signage.android.feature.client.base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import vn.digital.signage.android.R;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.home.HomeController;
import vn.digital.signage.android.feature.device.bootup.DeviceBootUpService;
import vn.digital.signage.android.feature.device.screenstatechange.ScreenStateChangeReceiver;
import vn.digital.signage.android.feature.device.screenstatechange.ScreenStateChangeService;
import vn.digital.signage.android.feature.server.faceinfo.ServerSyncFaceInfoService;
import vn.digital.signage.android.feature.server.syncautoplayatdefinedtime.ServerSyncAutoPlayService;
import vn.digital.signage.android.feature.server.synccurrentplaylist.ServerSyncCurrentPlaylistService;
import vn.digital.signage.android.feature.server.updateinfotoserver.UpdateInfoToServerService;
import vn.digital.signage.android.utils.DateUtils;
import vn.digital.signage.android.utils.FragmentHelper;
import vn.digital.signage.android.utils.UiUtils;
import vn.digital.signage.android.utils.Utils;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.popup.PopupTooltipHelper;

public abstract class BaseActivity extends FragmentActivity {
    private final Logger log = Logger.getLogger(BaseActivity.class);

    @Inject
    protected HomeController homeController;
    @Inject
    protected SMRuntime runtime;
    @Inject
    protected FragmentHelper mFragmentHelper;

    protected BroadcastReceiver screenStateReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Config.hasLogLevel(LogLevel.UI))
            log.info("BaseActivity - onCreate");

        UiUtils.setFullScreenView(this);

        App.getInstance().inject(this);
        EventBus.getDefault().register(this);

        mFragmentHelper.setFragmentManager(getSupportFragmentManager());

        registerScreenStateReceiver();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
    }

    public void postHistoryToServer() {
        String groupId = "";
        String screenId = "";
        String position = "";
        if (runtime.getCurrentLayout() != null) {
            groupId = runtime.getCurrentLayout().getGroupId().toString();
            screenId = runtime.getCurrentLayout().getId().toString();
            position = String.valueOf(runtime.getCurrentLayout().getPosition());
        }

        if (runtime.getMediaTimeOn() == 0)
            runtime.setMediaTimeOn(System.currentTimeMillis());

        if (Config.hasLogLevel(LogLevel.UI)) {
            log.info(MainActivity.TAG +
                    String.format(" postHistoryToServer - groupId %s - screenId %s - position %s",
                            groupId,
                            screenId,
                            position));

            log.info(MainActivity.TAG +
                    String.format(" postHistoryToServerTime - groupId %s - time on %s - time off %s",
                            groupId,
                            DateUtils.fromDate(runtime.getMediaTimeOn()),
                            DateUtils.fromDate(runtime.getMediaTimeOff())));
        }

        homeController.doHistory(groupId,
                screenId,
                position,
                runtime.getAccountUserName(),
                DateUtils.fromDate(runtime.getMediaTimeOff()),
                runtime.getMediaFileNameOff(),
                DateUtils.fromDate(runtime.getMediaTimeOn()),
                runtime.getMediaFileNameOn());
    }

    public void submitFaceInfo() {
        String screenId = "";
        if (runtime.getCurrentLayout() != null) {
            screenId = runtime.getCurrentLayout().getId().toString();
        }

        homeController.submitFaceDetectionInformation(screenId,
                DateUtils.fromDate(System.currentTimeMillis()));
    }

    public void onEvent(Object obj) {
    }

    public void showMessagePopup(View view, String message) {
        final PopupTooltipHelper popupTooltipHelper = new PopupTooltipHelper(this, message);
        popupTooltipHelper.show(view);
    }

    public void switchFragment(Fragment f, String fragmentTag, boolean isBackStack) {
        mFragmentHelper.attachFragment(R.id.id_container, f, fragmentTag, isBackStack);
    }

    public void switchChildFragment(Fragment f, String fragmentTag, int id) {
        mFragmentHelper.attachFragment(id, f, fragmentTag, false);
    }

    public void removeFragment( int id) {
        mFragmentHelper.removeCurrentFragment(mFragmentHelper.getFragmentManager().findFragmentById(id));
    }

    public void registerScreenStateReceiver() {
        // initialize receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        screenStateReceiver = new ScreenStateChangeReceiver();
        registerReceiver(screenStateReceiver, filter);
    }

    public void onStartService() {
        if (Config.hasLogLevel(LogLevel.UI))
            log.info("BaseActivity - onStartService");

        if (!Utils.Services.isServiceRunning(this, ServerSyncCurrentPlaylistService.class.getName()))
            startService(getIntentService(ServerSyncCurrentPlaylistService.class));
        if (!Utils.Services.isServiceRunning(this, ServerSyncAutoPlayService.class.getName()))
            startService(getIntentService(ServerSyncAutoPlayService.class));
        if (!Utils.Services.isServiceRunning(this, UpdateInfoToServerService.class.getName()))
            startService(getIntentService(UpdateInfoToServerService.class));
//        if (!Utils.Services.isServiceRunning(this, ServerSyncFaceInfoService.class.getName()))
//            startService(getIntentService(ServerSyncFaceInfoService.class));
    }

    public void onStopService() {
        if (Config.hasLogLevel(LogLevel.UI))
            log.info("BaseActivity - onStopService");

        if (Utils.Services.isServiceRunning(this, ServerSyncCurrentPlaylistService.class.getName()))
            stopService(getIntentService(ServerSyncCurrentPlaylistService.class));
        if (Utils.Services.isServiceRunning(this, ServerSyncAutoPlayService.class.getName()))
            stopService(getIntentService(ServerSyncAutoPlayService.class));
        if (Utils.Services.isServiceRunning(this, UpdateInfoToServerService.class.getName())) {
            stopService(getIntentService(UpdateInfoToServerService.class));
            if (UpdateInfoToServerService.SingletonService.getIntance() != null)
                UpdateInfoToServerService.SingletonService.getIntance().cancelTimer();
        }
        if (Utils.Services.isServiceRunning(this, ServerSyncFaceInfoService.class.getName()))
            stopService(getIntentService(ServerSyncFaceInfoService.class));
    }

    public void stopBootUpService() {
        if (Utils.Services.isServiceRunning(this, DeviceBootUpService.class.getName()))
            stopService(getIntentService(DeviceBootUpService.class));
    }

    public void stopScreenStateChangeService() {
        if (Utils.Services.isServiceRunning(this, ScreenStateChangeService.class.getName()))
            stopService(getIntentService(ScreenStateChangeService.class));
    }

    private <T extends Service> Intent getIntentService(Class<T> classes) {
        return new Intent(this, classes);
    }

}
