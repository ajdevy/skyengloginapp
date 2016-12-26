package com.skyeng.app.login.data;

import android.support.annotation.NonNull;

import com.skyeng.app.login.data.json.LoginCodeResponse;
import com.skyeng.app.login.data.json.LoginResponse;

import rx.Observable;

public interface LoginClient {

    Observable<LoginResponse> login(@NonNull String username, @NonNull String password);

    Observable<LoginResponse> login(@NonNull String sentCode);

    Observable<LoginCodeResponse> sendCode(@NonNull String email);
}