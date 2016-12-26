package com.skyeng.app.inject;

import android.app.Application;

import com.skyeng.app.login.LoginController;
import com.skyeng.app.login.data.LoginClient;
import com.skyeng.app.login.data.LoginClientStub;
import com.skyeng.app.user.UserController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppInjectionModule {

    private Application mApplication;

    public AppInjectionModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    LoginClient provideLoginClient(Application app) {
        return new LoginClientStub(app);
    }

    @Provides
    @Singleton
    UserController provideUserController(Application app) {
        return new UserController(app);
    }

    @Provides
    @Singleton
    LoginController provideLoginController(Application app, LoginClient loginClient,
                                           UserController userController) {
        return new LoginController(app, userController, loginClient);
    }
}