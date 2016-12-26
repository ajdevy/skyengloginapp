package com.skyeng.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyeng.app.R;
import com.skyeng.app.SkyengApp;
import com.skyeng.app.user.UserController;
import com.skyeng.app.util.FragmentUtil;
import com.skyeng.app.util.LogErrorAction;
import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends RxFragment {

    private static final String TAG = MainFragment.class.getName();

    @Inject
    UserController userController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SkyengApp) getActivity().getApplication()).getInjector().inject(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.youve_entered);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkAuthentication();
    }

    private void checkAuthentication() {
        if (!isAuthenticated()) {
            logout();
        }
    }

    private boolean isAuthenticated() {
        return userController.isAuthenticated();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @OnClick(R.id.logout_button)
    void onClickLogout() {
        logout();
    }

    private void logout() {
        userController.logout()
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    FragmentUtil.remove(this);
                    ((MainActivity) getActivity()).openLoginFragment();
                }, new LogErrorAction(TAG));
    }
}