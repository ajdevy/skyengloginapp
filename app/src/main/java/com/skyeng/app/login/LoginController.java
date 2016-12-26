package com.skyeng.app.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skyeng.app.login.data.LoginClient;
import com.skyeng.app.login.data.json.LoginCodeResponse;
import com.skyeng.app.login.data.json.LoginResponse;
import com.skyeng.app.user.UserController;
import com.skyeng.app.util.NetworkHelper;

import rx.Observable;

public class LoginController {

    private final Context context;
    private final UserController userController;
    private final LoginClient loginClient;

    public LoginController(Context context, UserController userController, LoginClient loginClient) {
        this.context = context;
        this.userController = userController;
        this.loginClient = loginClient;
    }

    public Observable<LoginResponse> login(@NonNull String code) {
        return NetworkHelper.checkNetworkAndExecute(context, login(loginClient.login(code)));
    }

    public Observable<LoginResponse> login(@NonNull String username, @NonNull String password) {
        return NetworkHelper.checkNetworkAndExecute(context, login(loginClient.login(username, password)));
    }

    private Observable<LoginResponse> login(@NonNull Observable<LoginResponse> loginRequest) {
        return NetworkHelper.checkNetworkAndExecute(context, loginRequest
                .flatMap(result -> {
                    userController.onLoggedIn(result);
                    return Observable.just(result);
                }));
    }

    public Observable<LoginCodeResponse> sendCode(@NonNull String email) {
        return NetworkHelper.checkNetworkAndExecute(context, loginClient.sendCode(email));
    }

}