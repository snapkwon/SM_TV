package vn.digital.signage.android.feature.client.registration;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import butterknife.ButterKnife;
import vn.digital.signage.android.R;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.api.response.RegisterResponse;
import vn.digital.signage.android.feature.client.base.BaseActivity;
import vn.digital.signage.android.feature.client.base.BaseFragment;
import vn.digital.signage.android.feature.client.home.HomeFragment;
import vn.digital.signage.android.utils.UiUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class RegisterFragment extends BaseFragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();
    private final Logger log = Logger.getLogger(RegisterFragment.class);

    @Inject
    RegisterView registerView;

    @Inject
    SMRuntime runtime;

    @Inject
    public RegisterFragment() {
        // do nothing
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.register_fragment, container, false);
        UiUtils.setFullScreenWindow(getActivity(), view);

        ButterKnife.inject(registerView, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerView.initViews(getActivity());

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                UiUtils.hideKeyboard(getActivity());
            }
        }, 200L);
    }

    public void onEventMainThread(final RegisterResponse events) {
        if (events != null && events.isSuccess()) {
            if (events.getError().equalsIgnoreCase("error") || events.getDetails() == null) {
                registerView.showErrorName(events.getMsg());
            } else {
                if (Config.hasLogLevel(LogLevel.API))
                    log.info("RegisterFragment on register succeed");
                Toast.makeText(getActivity(), getText(R.string.msg_register_susses), Toast.LENGTH_LONG).show();

                runtime.setRegisterInfo(events.getDetails());

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity) getActivity()).switchFragment(HomeFragment.newInstance(), HomeFragment.TAG, false);
                    }
                }, 500L);
            }
        } else {
            Toast.makeText(getActivity(), getText(R.string.app_connection_error), Toast.LENGTH_LONG).show();
            if (Config.hasLogLevel(LogLevel.API))
                log.info(getResources().getString(R.string.app_connection_error));
        }
    }
}
