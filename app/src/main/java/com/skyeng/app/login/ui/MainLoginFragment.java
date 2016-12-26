package com.skyeng.app.login.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.skyeng.app.R;
import com.skyeng.app.SkyengApp;
import com.skyeng.app.email.EmailValidator;
import com.skyeng.app.login.LoginController;
import com.skyeng.app.login.data.json.LoginCodeResponse;
import com.skyeng.app.util.UiAvailabilityUtil;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainLoginFragment extends BaseBottomTextLoginFragment {

    private static final String TAG = MainLoginFragment.class.getName();

    @Inject
    LoginController loginController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SkyengApp) getActivity().getApplication()).getInjector().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        button.setEnabled(hasEmailText());
    }

    private boolean hasEmailText() {
        return !TextUtils.isEmpty(getEmail());
    }

    @Override
    protected void onActionTextClicked() {
        openLoginWithPasswordFragment(getEmail());
    }

    @Override
    protected void onButtonClicked() {
        tryToGetCode();
    }

    private void tryToGetCode() {

        final CharSequence email = getEmail();
        if (!EmailValidator.isValid(getEmail())) {
            showEmailNotValidError();
        } else {
            showLoadingDialog(R.string.getting_code_wait);
            loginController
                    .sendCode(email.toString())
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        hideLoadingDialog();
                        if (result.hasError()) {
                            handleSendCodeError(result.getErrorCode());
                        } else {
                            Log.d(TAG, "code sent " + result);
                            openEnterCodeFragment(result.getPhoneNumber(), result.getEmail());
                        }
                    }, throwable -> {
                        hideLoadingDialog();
                        handleInternetError(throwable);
                        Log.e(TAG, "got an error", throwable);
                    });
        }
    }

    private void handleSendCodeError(int errorCode) {
        if (LoginCodeResponse.ERROR_WRONG_EMAIL == errorCode) {
            showToast(R.string.wrong_email);
        } else {
            showToast(R.string.could_not_send_code);
        }
    }

    private void openEnterCodeFragment(String phoneNumber, String email) {
        openFragment(EnterLoginCodeFragment.newInstance(phoneNumber, email));
    }

    private void openLoginWithPasswordFragment(String email) {
        openFragment(LoginWithPasswordFragment.newInstance(email));
    }

    private void openFragment(Fragment fragment) {
        final FragmentActivity activity = getActivity();
        if (UiAvailabilityUtil.isUiAvailable(activity)) {
            final String tag = fragment.getClass().getName();
            activity.getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, tag)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        }
    }

    private void showEmailNotValidError() {
        editText.setError(getString(R.string.email_not_valid));
    }

    private String getEmail() {
        return editText.getText().toString();
    }

    @Override
    protected int getEditTextInputType() {
        return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    }

    @Override
    protected int getTextActionText() {
        return R.string.plain_entrance_with_password;
    }

    @Override
    protected int getBottomTextResourceId() {
        return R.string.main_login_bottom_text;
    }

    @Override
    protected int getButtonTextResourceId() {
        return R.string.main_login_button_text;
    }

    @Override
    protected int getEditTextHintResourceId() {
        return R.string.email;
    }

    @Override
    protected CharSequence getTopText() {
        return getString(R.string.main_login_top_text);
    }

    @Override
    protected int getToolbarTitleResourceId() {
        return R.string.login;
    }
}