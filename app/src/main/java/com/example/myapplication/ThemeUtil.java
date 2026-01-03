package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ThemeUtil {
    public static void applyTheme(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark", false);
        if (isDark) activity.setTheme(R.style.AppTheme_Dark);
        else activity.setTheme(R.style.AppTheme_Light);
    }
}