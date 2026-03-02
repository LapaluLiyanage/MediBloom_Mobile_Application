package com.example.medibloomappv2;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.medibloomappv2.utils.NotificationHelper;
import com.example.medibloomappv2.utils.PreferenceManager;

public class MediBloomApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Create notification channel on app start
        NotificationHelper.createNotificationChannel(this);

        // Apply dark mode preference
        PreferenceManager prefs = new PreferenceManager(this);
        AppCompatDelegate.setDefaultNightMode(
                prefs.isDarkMode()
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}

