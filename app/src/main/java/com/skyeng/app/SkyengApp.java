package com.skyeng.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyeng.app.inject.AppInjectionComponent;
import com.skyeng.app.inject.AppInjectionModule;
import com.skyeng.app.inject.DaggerAppInjectionComponent;
import com.skyeng.app.sync.BackgroundSyncService;

import java.util.Calendar;

public class SkyengApp extends Application {

    private AppInjectionComponent injectionComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        createInjectionComponent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupImageLoader();
        scheduleSyncService();
    }

    private void scheduleSyncService() {
        // Set the alarm to start at approximately 2:00 p.m.
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(this, BackgroundSyncService.class);
        final PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    private void setupImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public AppInjectionComponent getInjector() {
        return injectionComponent;
    }

    public void createInjectionComponent() {
        injectionComponent = DaggerAppInjectionComponent.builder()
                .appInjectionModule(new AppInjectionModule(this))
                .build();
    }
}