package vn.digital.signage.android.feature.client.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import butterknife.ButterKnife;
import vn.digital.signage.android.R;
import vn.digital.signage.android.api.response.AssetsListResponse;
import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.api.response.LayoutResponse;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.base.BaseActivity;
import vn.digital.signage.android.feature.client.base.BaseFragment;
import vn.digital.signage.android.feature.server.updateinfotoserver.UpdateInfoToServerEvent;
import vn.digital.signage.android.utils.DebugLog;
import vn.digital.signage.android.utils.UiUtils;
import vn.digital.signage.android.utils.autoplay.AutoPlayHelper;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.enumeration.MediaType;

public class HomeFragment extends BaseFragment {

    public static final String TAG = HomeFragment.class.getSimpleName();
    public static HomeFragment instance;
    public static BaseActivity mActivity;
    private final Logger log = Logger.getLogger(HomeFragment.class);

    @Inject
    HomeScreenView homeView;
    @Inject
    SMRuntime runtime;
    @Inject
    HomeController homeController;

    @Inject
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        UiUtils.setFullScreenWindow(getActivity(), view);

        ButterKnife.inject(homeView, view);
        setRetainInstance(true);
        instance = this;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeView.initViews(this);

        homeController.postVersionCode(String.valueOf(runtime.getRegisterInfo().getId()));
        // start default video
        getHomeView().playDefaultVideo(0);

        // check layout
        getHomeView().getCurrentPlaylist();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                HomeController.isUpdate = true;
//            }
//        }, 60000);
    }

    public HomeScreenView getHomeView() {
        return homeView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        instance = this;
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        DebugLog.d("onDestroy");
        if (homeView != null) {
            homeView.updateMediaVisibility(MediaType.CLEAR);//clear all data
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        instance = null;
    }

    public void onEventMainThread(final LayoutResponse response) {
        if (response != null) {
            if (response.isSuccess()) {
                if (response.getError() != null && !response.getError().equalsIgnoreCase("error")) {
//                    runtime.setLayoutResponse(response);

                    homeView.setCurrentResponse(response);

                    // Check and download video
                    homeView.checkDownloadVideo(response);
                } else {
                    if (Config.hasLogLevel(LogLevel.API))
                        log.info(response.getMsg());
                }
            } else {
                if (Config.hasLogLevel(LogLevel.API))
                    log.info(getResources().getString(R.string.app_connection_error));
            }
        } else {
            if (Config.hasLogLevel(LogLevel.API))
                log.info(getResources().getString(R.string.app_connection_error));
        }
    }

    public void onEventMainThread(final AssetsListResponse response) {
        if (response != null) {
            if (response.isSuccess() && (response.getError() == null
                    || !response.getError().equalsIgnoreCase("error"))) {
                runtime.setAssetsListResponse(response);
            } else {
                if (Config.hasLogLevel(LogLevel.API))
                    log.info(response.getMsg());
            }
        } else {
            if (Config.hasLogLevel(LogLevel.API))
                log.info(getResources().getString(R.string.app_connection_error));
        }
    }

    public void onEventMainThread(final UpdateInfoToServerEvent response) {
        if (getHomeView() != null)
            getHomeView().postInfoToServer();
    }

    public void onEventMainThread(AutoPlayResponse response) {
        if (response != null) {
            if (response.isSuccess() && (response.getError() == null
                    || !response.getError().equalsIgnoreCase("error"))) {

                // set response
                runtime.setAutoPlaySchedule(response);

                // schedule another play receiver
                AutoPlayResponse r = runtime.getAutoPlaySchedule();
                if (r.getAutoplay() != null &&
                        (r.getAutoplay().getHour() >= 0
                                || r.getAutoplay().getMinute() >= 0
                                || r.getAutoplay().getSecond() >= 0)) {

                    if (Config.hasLogLevel(LogLevel.API))
                        log.info(TAG + String.format("auto_play_media - onEventMainThread - %s", (new Gson()).toJson(r.getAutoplay())));
                    AutoPlayHelper.scheduleAutoPlay(getActivity(), runtime);
                }
            } else {
                if (Config.hasLogLevel(LogLevel.API))
                    log.info(response.getMsg());
            }
        } else {
            if (Config.hasLogLevel(LogLevel.API))
                log.info(getResources().getString(R.string.app_connection_error));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        homeView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeView.onActivityResult();
    }
}
