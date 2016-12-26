package com.skyeng.app.util;

import android.support.v4.app.FragmentActivity;

public class UiAvailabilityUtil {

    public static boolean isUiAvailable(FragmentActivity activity) {
        return activity != null && !activity.isFinishing();
    }
}
