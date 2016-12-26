package com.skyeng.app.login.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.skyeng.app.login.data.json.LoginCodeRequestData;
import com.skyeng.app.login.data.json.LoginCodeResponse;
import com.skyeng.app.login.data.json.LoginRequestData;
import com.skyeng.app.login.data.json.LoginResponse;

import rx.Observable;

public class LoginClientImpl implements LoginClient {

    private static final String TAG = LoginClientImpl.class.getSimpleName();

    private static final String LOGIN_URL = "http://test.skyeng.com/login";
    private static final String LOGIN_CODE_URL = "http://test.skyeng.com/login/code";

    private final RequestQueue requestQueue;

    public LoginClientImpl(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public Observable<LoginResponse> login(@NonNull String username, @NonNull String password) {
        return login(new LoginRequestData(username, password));
    }

    @Override
    public Observable<LoginResponse> login(@NonNull String sentCode) {
        return login(new LoginRequestData(sentCode));
    }

    private Observable<LoginResponse> login(@NonNull LoginRequestData loginRequestData) {
        final LoginRequest request = new LoginRequest(LOGIN_URL, loginRequestData);
        return request.execute(requestQueue);
    }

    @Override
    public Observable<LoginCodeResponse> sendCode(@NonNull String email) {
        final LoginCodeRequest request = new LoginCodeRequest(LOGIN_CODE_URL, new LoginCodeRequestData(email));
        return request.execute(requestQueue);
    }
}