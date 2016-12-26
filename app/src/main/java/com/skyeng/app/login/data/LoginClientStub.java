package com.skyeng.app.login.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skyeng.app.login.data.json.LoginCodeResponse;
import com.skyeng.app.login.data.json.LoginResponse;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class LoginClientStub implements LoginClient {

    public LoginClientStub(Context context) {
    }

    @Override
    public Observable<LoginResponse> login(@NonNull String username, @NonNull String password) {
        return Observable.timer(1, TimeUnit.SECONDS)
                .flatMap(result -> Observable.just(new LoginResponse("jwt238")));
    }

    @Override
    public Observable<LoginResponse> login(@NonNull String sentCode) {
        return Observable.timer(1, TimeUnit.SECONDS)
                .flatMap(result -> Observable.just(new LoginResponse("jwt238")));
    }

    @Override
    public Observable<LoginCodeResponse> sendCode(@NonNull String email) {
        return Observable.timer(1, TimeUnit.SECONDS)
                .flatMap(result -> Observable.just(new LoginCodeResponse("+3712*****556", "ajdev@yandex.com")));
    }
}