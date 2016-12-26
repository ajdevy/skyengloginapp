package com.skyeng.app.login.data.json;

public class LoginCodeRequestData {

    private static final String TAG = LoginCodeRequestData.class.getName();

    private String email;

    public LoginCodeRequestData() {
    }

    public LoginCodeRequestData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}