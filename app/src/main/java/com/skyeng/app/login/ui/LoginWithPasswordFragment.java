package com.skyeng.app.login.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.skyeng.app.R;
import com.skyeng.app.SkyengApp;
import com.skyeng.app.email.EmailValidator;
import com.skyeng.app.login.LoginController;
import com.skyeng.app.login.data.json.LoginResponse;
import com.skyeng.app.ui.MainActivity;
import com.skyeng.app.util.UiAvailabilityUtil;

import javax.inject.Inject;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginWithPasswordFragment extends BaseBottomTextLoginFragment {

    private static final String TAG = LoginWithPasswordFragment.class.getName();

    private static final String ARGUMENT_EMAIL = "ARGUMENT_EMAIL";

    @Inject
    LoginController loginController;

    @Bind(R.id.login_password_edit_text)
    EditText passwordEditText;

    public static LoginWithPasswordFragment newInstance(String email) {
        final Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_EMAIL, email);
        final LoginWithPasswordFragment fragment = new LoginWithPasswordFragment();
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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_login_with_password;
    }

    private void setupViews() {
        final String email = getEmailArgument();
        if (!TextUtils.isEmpty(email)) {
            editText.setText(email);
        }

        passwordEditText.setOnEditorActionListener((textView, editorInfo, keyEvent) -> {
            Log.d(TAG, "passwordEditText editor action, event " + keyEvent);
            if (EditorInfo.IME_ACTION_GO == editorInfo) {
                tryToLogin();
                return true;
            }
            return false;
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence password, int i, int i1, int i2) {
                validatePasswordInputAndSetButtonEnabled(password);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        button.setEnabled(hasEmailAndPasswordText());
    }

    protected void validatePasswordInputAndSetButtonEnabled(CharSequence password) {
        button.setEnabled(!TextUtils.isEmpty(password) && hasEmailText());
    }

    @Override
    protected void validateInputAndSetButtonEnabled(CharSequence text) {
        if (hasPasswordText()) {
            super.validateInputAndSetButtonEnabled(text);
        } else {
            disableButton();
        }
    }

    private boolean hasPasswordText() {
        return !TextUtils.isEmpty(getPassword());
    }

    private boolean hasEmailAndPasswordText() {
        return hasEmailText() && hasPasswordText();
    }

    private boolean hasEmailText() {
        return !TextUtils.isEmpty(getEmail());
    }

    @Nullable
    private String getEmailArgument() {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getString(ARGUMENT_EMAIL);
        }
        return null;
    }

    public String getEmail() {
        return editText.getText().toString();
    }

    @Override
    protected void onActionTextClicked() {
        //go to previous Login with code screen
        getActivity().onBackPressed();
    }

    @Override
    protected void onButtonClicked() {
        tryToLogin();
    }

    private void tryToLogin() {
        final String email = getEmail();
        final String password = getPassword();
        if (TextUtils.isEmpty(password)) {
            Log.d(TAG, "tryToLogin password is empty");
            passwordEditText.setError(getString(R.string.enter_password));
        } else if (!EmailValidator.isValid(email)) {
            Log.d(TAG, "tryToLogin email invalid  =" + email);
            editText.setError(getString(R.string.email_not_valid));
        } else {
            showLoadingDialog(R.string.logging_in_wait);
            loginController.login(email, password)
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        hideLoadingDialog();
                        if (result.hasError()) {
                            handleLoginError(result.getErrorCode());
                        } else {
                            openMainFragment();
                        }
                    }, throwable -> {
                        hideLoadingDialog();
                        handleLoginError(throwable);
                    });
        }
    }

    private void handleLoginError(int errorCode) {
        if (LoginResponse.ERROR_WRONG_CODE == errorCode) {
            showToast(R.string.wrong_code);
        } else if (LoginResponse.ERROR_WRONG_EMAIL_OR_PASSWORD == errorCode) {
            showToast(R.string.wrong_email_or_password);
        } else {
            showToast(R.string.could_not_login);
        }
    }

    private void handleLoginError(Throwable throwable) {
        if (!handleInternetError(throwable)) {
            showToast(R.string.could_not_login);
        }
    }

    @Nullable
    private String getPassword() {
        return passwordEditText.getText().toString();
    }

    private void openMainFragment() {
        final MainActivity activity = (MainActivity) getActivity();
        if (UiAvailabilityUtil.isUiAvailable(activity)) {
            activity.openMainFragment();
        }
    }

    @Override
    protected int getEditTextInputType() {
        return EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    }

    @Override
    protected int getTextActionText() {
        return R.string.login_without_password;
    }

    @Override
    protected int getButtonTextResourceId() {
        return R.string.enter;
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

    @Override
    protected int getBottomTextResourceId() {
        return R.string.login_password_bottom_text;
    }
}