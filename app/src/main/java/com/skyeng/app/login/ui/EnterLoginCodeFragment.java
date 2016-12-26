package com.skyeng.app.login.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.skyeng.app.R;
import com.skyeng.app.SkyengApp;
import com.skyeng.app.login.LoginController;
import com.skyeng.app.login.data.json.LoginResponse;
import com.skyeng.app.ui.MainActivity;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EnterLoginCodeFragment extends BaseLoginFragment {

    private static final String TAG = EnterLoginCodeFragment.class.getName();

    private static final String ARGUMENT_PHONE_NUMBER = "ARGUMENT_PHONE_NUMBER";
    private static final String ARGUMENT_EMAIL = "ARGUMENT_EMAIL";

    private static final long RESEND_COUNTDOWN_TIME = 59000;

    @Inject
    LoginController loginController;

    private CountDownTimer resendCodeCountDownTimer;

    public static EnterLoginCodeFragment newInstance(String phoneNumber, String email) {
        final Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_EMAIL, email);
        arguments.putString(ARGUMENT_PHONE_NUMBER, phoneNumber);
        final EnterLoginCodeFragment fragment = new EnterLoginCodeFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

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
        button.setEnabled(hasCode());
    }

    private boolean hasCode() {
        return !TextUtils.isEmpty(getCode());
    }

    @Nullable
    private String getPhoneNumber() {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getString(ARGUMENT_PHONE_NUMBER);
        }
        return null;
    }

    @Nullable
    private String getEmail() {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getString(ARGUMENT_EMAIL);
        }
        return null;
    }

    @Override
    protected void onActionTextClicked() {
        if (resendCodeCountDownTimer == null) {
            resendCodeCountDownTimer = new CountDownTimer(RESEND_COUNTDOWN_TIME, 1000) {

                public void onTick(long millisUntilFinished) {
                    actionText.setText(getString(R.string.resend_code_phone_number_countdown_format, millisUntilFinished / 1000));
                }

                public void onFinish() {
                    actionText.setText(R.string.resend_code_phone_number);
                    resendCodeCountDownTimer = null;
                }
            }.start();
            final String email = getEmail();
            if (!TextUtils.isEmpty(email)) {
                loginController
                        .sendCode(email)
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            hideLoadingDialog();
                        }, throwable -> {
                            hideLoadingDialog();
                            Log.e(TAG, "got an error", throwable);
                        });
            }
        }
    }

    @Override
    protected void onButtonClicked() {
        tryToLogin();
    }

    private void tryToLogin() {
        final String code = getCode();
        if (!TextUtils.isEmpty(code)) {
            if (resendCodeCountDownTimer == null) {
                showLoadingDialog(R.string.logging_in_wait);
                loginController.login(code)
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            hideLoadingDialog();
                            if (result.hasError()) {
                                handleLoginError(result.getErrorCode());
                            } else {
                                ((MainActivity) getActivity()).openMainFragment();
                            }
                        }, throwable -> {
                            hideLoadingDialog();
                            handleLoginError(throwable);
                        });
            }
        } else {
            editText.setError(getString(R.string.please_enter_code));
        }
    }

    private void handleLoginError(int errorCode) {
        if (LoginResponse.ERROR_WRONG_CODE == errorCode) {
            showToast(R.string.wrong_code);
        }
    }

    private void handleLoginError(Throwable throwable) {
        if (throwable != null) {
            if (throwable instanceof SocketTimeoutException) {
                showNoInternetError();
                return;
            } else if (throwable instanceof ConnectException) {
                showNoInternetError();
                return;
            }
        }
        showToast(R.string.could_not_login);
    }

    @Nullable
    private String getCode() {
        return editText.getText().toString();
    }

    @Override
    protected int getEditTextInputType() {
        return EditorInfo.TYPE_CLASS_NUMBER;
    }

    @Override
    protected int getTextActionText() {
        return R.string.resend_code_phone_number;
    }

    @Override
    protected int getButtonTextResourceId() {
        return R.string.enter;
    }

    @Override
    protected int getEditTextHintResourceId() {
        return R.string.code;
    }

    @Override
    protected CharSequence getTopText() {
        if (isPhoneNumberEmpty()) {
            return getEmailText();
        } else {
            return getPhoneNumberText();
        }
    }

    private boolean isPhoneNumberEmpty() {
        return TextUtils.isEmpty(getPhoneNumber());
    }

    private CharSequence getPhoneNumberText() {
        final String phoneNumber = getPhoneNumber();
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.code_sent_to_phone, phoneNumber);
        }
        return getString(R.string.code_sent_to_phone);
    }

    private CharSequence getEmailText() {
        final String email = getEmail();
        if (!TextUtils.isEmpty(email)) {
            return getString(R.string.code_sent_to_email, email);
        } else {
            return getString(R.string.code_sent_to_email_no_email_string);
        }
    }

    @Override
    protected int getToolbarTitleResourceId() {
        return R.string.please_enter_code;
    }
}