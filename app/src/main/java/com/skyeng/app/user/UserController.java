package com.skyeng.app.user;

import android.content.Context;
import android.text.TextUtils;

import com.skyeng.app.login.data.json.LoginResponse;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class UserController {

    private String jwt;
    private Context context;

    public UserController(Context context) {
        this.context = context;
    }

    public void onLoggedIn(LoginResponse loginResponse) {
        if (loginResponse != null) {
            jwt = loginResponse.getJwt();
        }
    }

    public boolean isAuthenticated() {
        return !TextUtils.isEmpty(jwt);
    }

    public Observable<Void> logout() {
        return Observable.fromCallable(() -> {
            jwt = null;
            return null;
        });
    }

    public Observable<Void> sync() {
        return Observable.timer(1, TimeUnit.SECONDS)
                .map(result -> null);
    }
}