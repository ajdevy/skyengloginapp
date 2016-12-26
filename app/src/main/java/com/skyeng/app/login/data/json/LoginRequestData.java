package com.skyeng.app.login.data.json;

public class LoginRequestData {

    private static final String TAG = LoginRequestData.class.getName();

    private String username;
    private String password;
    private String sentCode;

    public LoginRequestData() {
    }

    public LoginRequestData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequestData(String sentCode) {
        this.sentCode = sentCode;
    }

    public String getSentCode() {
        return sentCode;
    }

    public void setSentCode(String sentCode) {
        this.sentCode = sentCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}