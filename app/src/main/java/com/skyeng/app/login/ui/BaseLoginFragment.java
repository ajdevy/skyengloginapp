package com.skyeng.app.login.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skyeng.app.R;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseLoginFragment extends RxFragment {

    @Bind(R.id.login_top_text)
    TextView topText;

    @Bind(R.id.login_action_text)
    TextView actionText;

    @Bind(R.id.login_edit_text)
    EditText editText;

    @Bind(R.id.login_button)
    Button button;

    private ProgressDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutResourceId(), container, false);
        ButterKnife.bind(this, view);

        setupViews();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText.requestFocus();
    }

    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_login;
    }

    private void setupViews() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getToolbarTitleResourceId());

        topText.setText(getTopText());

        editText.setHint(getEditTextHintResourceId());
        editText.setInputType(getEditTextInputType());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateInputAndSetButtonEnabled(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        button.setText(getButtonTextResourceId());
        actionText.setText(getTextActionText());
    }

    protected void validateInputAndSetButtonEnabled(CharSequence text) {
        button.setEnabled(!TextUtils.isEmpty(text));
    }

    protected void disableButton() {
        button.setEnabled(false);
    }

    protected boolean handleInternetError(Throwable throwable) {
        if (throwable != null) {
            if (throwable instanceof SocketTimeoutException) {
                showNoInternetError();
                return true;
            } else if (throwable instanceof ConnectException) {
                showNoInternetError();
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.login_button)
    void onClickLogin() {
        onButtonClicked();
    }

    @OnClick(R.id.login_action_text)
    void onClickLoginActionText() {
        onActionTextClicked();
    }

    protected abstract void onActionTextClicked();

    protected abstract void onButtonClicked();

    protected void showToast(@NonNull String text) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.toast_login, null);
        final TextView toastTextView = (TextView) layout.findViewById(R.id.toast_message);
        toastTextView.setText(text);
        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    protected void showToast(@StringRes int stringId) {
        showToast(getString(stringId));
    }

    protected ProgressDialog showLoadingDialog(@StringRes int dialogText) {
        loadingDialog = ProgressDialog.show(getContext(), "", getString(dialogText), true);
        return loadingDialog;
    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    protected void showNoInternetError() {
        showToast(R.string.could_not_connect);
    }

    protected abstract int getEditTextInputType();

    @StringRes
    protected abstract int getTextActionText();

    @StringRes
    protected abstract int getButtonTextResourceId();

    @StringRes
    protected abstract int getEditTextHintResourceId();

    protected abstract CharSequence getTopText();

    @StringRes
    protected abstract int getToolbarTitleResourceId();
}