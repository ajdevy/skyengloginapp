package com.skyeng.app.login.data;

import com.android.volley.Request;
import com.skyeng.app.login.data.json.LoginRequestData;
import com.skyeng.app.login.data.json.LoginResponse;

public class LoginRequest extends ErrorCodeRequest<LoginResponse> {

    private static final String TAG = LoginRequest.class.getName();

    public LoginRequest(String url, LoginRequestData requestData) {
        super(Request.Method.POST, url, requestData, LoginResponse.class);
    }
}