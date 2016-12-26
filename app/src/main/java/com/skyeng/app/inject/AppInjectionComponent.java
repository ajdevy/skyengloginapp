package com.skyeng.app.inject;

import com.skyeng.app.login.ui.EnterLoginCodeFragment;
import com.skyeng.app.login.ui.LoginWithPasswordFragment;
import com.skyeng.app.login.ui.MainLoginFragment;
import com.skyeng.app.sync.BackgroundSyncService;
import com.skyeng.app.ui.MainActivity;
import com.skyeng.app.ui.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppInjectionModule.class})
public interface AppInjectionComponent {

    void inject(MainActivity mainActivity);

    void inject(MainFragment mainFragment);

    void inject(MainLoginFragment loginFragment);

    void inject(EnterLoginCodeFragment fragment);

    void inject(LoginWithPasswordFragment loginWithPasswordFragment);

    void inject(BackgroundSyncService backgroundSyncService);
}