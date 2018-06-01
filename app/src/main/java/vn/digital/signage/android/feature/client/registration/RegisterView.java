package vn.digital.signage.android.feature.client.registration;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import org.apache.log4j.Logger;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import vn.digital.signage.android.Constants;
import vn.digital.signage.android.R;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.base.MainActivity;
import vn.digital.signage.android.utils.NetworkUtils;
import vn.digital.signage.android.utils.ValidationUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class RegisterView {
    private static final String TAG = RegisterView.class.getSimpleName();
    private final Logger log = Logger.getLogger(RegisterView.class);
    @InjectView(R.id.fragment_register_edt_address)
    EditText edtAddress;
    @InjectView(R.id.fragment_register_edt_name)
    EditText edtName;
    @InjectView(R.id.fragment_register_edt_password)
    EditText edtPassword;
    @InjectView(R.id.fragment_register_btn_submit)
    Button btnSubmit;
    @Inject
    RegisterController registerController;
    private Context mContext;

    @Inject
    public RegisterView() {
        // do nothing
    }

    public void initViews(Context context) {
        mContext = context;

        if (Constants.DEBUG_PRE_ENTER_LOGIN_CONFIG) {
            //edtAddress.setText("http://cms3.smg.com.vn:8300");
            edtAddress.setText("http://cms3.smg.com.vn:8200");
            edtName.setText("aaaaaa");
            edtPassword.setText("123456");
        }
    }

    @OnClick(R.id.fragment_register_btn_submit)
    public void onButtonSubmitClick(View v) {
        if (!isValid() || !NetworkUtils.checkInternetConnection()) {
            return;
        }

        final Runtime runtime = Runtime.getRuntime();

        if (Config.hasLogLevel(LogLevel.UI))
            log.info(TAG + " calling register api");
        // Do call server get Wallet Voucher
        registerController.doRegister(edtName.getText().toString().trim(),
                edtPassword.getText().toString().trim(),
                runtime.totalMemory(),
                runtime.freeMemory(),
                edtAddress.getText().toString());
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(edtAddress.getText())) {
            showErrorAddress(mContext.getString(R.string.msg_enter_address));
            return false;
        }

        if (!ValidationUtils.isValidUrl(edtAddress.getText().toString())) {
            showErrorAddress(mContext.getString(R.string.msg_enter_address_invalid));
            return false;
        }

        if (!URLUtil.isNetworkUrl(edtAddress.getText().toString())) {
            showErrorAddress(mContext.getString(R.string.msg_enter_address_exist));
            return false;
        }

        if (TextUtils.isEmpty(edtName.getText())) {
            showErrorName(mContext.getString(R.string.msg_enter_name));
            return false;
        }

        if (TextUtils.isEmpty(edtPassword.getText())) {
            showErrorPass();
            return false;
        }
        return true;
    }

    public void showErrorAddress(String error) {
        if (Config.hasLogLevel(LogLevel.UI))
            log.info(TAG + " " + error);
        ((MainActivity) mContext).showMessagePopup(edtAddress, error);
    }

    public void showErrorName(String error) {
        if (Config.hasLogLevel(LogLevel.UI))
            log.info(TAG + " " + error);
        ((MainActivity) mContext).showMessagePopup(edtName, error);
    }

    public void showErrorPass() {
        if (Config.hasLogLevel(LogLevel.UI))
            log.info(TAG + " " + mContext.getString(R.string.msg_enter_password));
        ((MainActivity) mContext).showMessagePopup(edtPassword, mContext.getString(R.string.msg_enter_password));
    }

    public void showErrorNetwork() {
        if (Config.hasLogLevel(LogLevel.UI))
            log.info(mContext.getString(R.string.app_connection_error));
    }
}
