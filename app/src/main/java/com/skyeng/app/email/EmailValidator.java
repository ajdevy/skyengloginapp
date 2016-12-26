package com.skyeng.app.email;

import android.text.TextUtils;

public class EmailValidator {

    public final static boolean isValid(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}