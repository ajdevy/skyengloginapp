package com.skyeng.app.sync;

import android.app.IntentService;
import android.content.Intent;

import com.skyeng.app.SkyengApp;
import com.skyeng.app.user.UserController;

import javax.inject.Inject;


public class BackgroundSyncService extends IntentService {

    private static final String TAG = BackgroundSyncService.class.getName();

    @Inject
    UserController userController;

    public BackgroundSyncService() {
        super(TAG);
        ((SkyengApp) getApplication()).getInjector().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        userController.sync();
    }
}
