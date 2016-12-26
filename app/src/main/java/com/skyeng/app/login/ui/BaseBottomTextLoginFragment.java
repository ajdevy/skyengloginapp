package com.skyeng.app.login.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skyeng.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseBottomTextLoginFragment extends BaseLoginFragment {

    @Bind(R.id.login_bottom_text)
    TextView loginBottomText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        setupViews();

        return view;
    }

    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_login_with_bottom_text;
    }

    private void setupViews() {
        loginBottomText.setText(getBottomTextResourceId());
    }

    @StringRes
    protected abstract int getBottomTextResourceId();
}