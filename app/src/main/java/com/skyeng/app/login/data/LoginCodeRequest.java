package com.skyeng.app.login.data;

import com.android.volley.Request;
import com.skyeng.app.login.data.json.LoginCodeRequestData;
import com.skyeng.app.login.data.json.LoginCodeResponse;

public class LoginCodeRequest extends ErrorCodeRequest<LoginCodeResponse> {

    private static final String TAG = LoginCodeRequest.class.getName();

    public LoginCodeRequest(String url, LoginCodeRequestData requestData) {
        super(Request.Method.POST, url, requestData, LoginCodeResponse.class);
    }
}