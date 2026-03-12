package com.example.medibloomappv2.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.ui.auth.LoginActivity;
import com.example.medibloomappv2.ui.main.MainActivity;
import com.example.medibloomappv2.ui.onboarding.OnboardingActivity;
import com.example.medibloomappv2.utils.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.iv_logo);
        TextView appName = findViewById(R.id.tv_app_name);
        TextView tagline = findViewById(R.id.tv_tagline);
        TextView version = findViewById(R.id.tv_version);

        // Animate logo: scale + rotation
        logo.setAlpha(0f);
        logo.setScaleX(0f);
        logo.setScaleY(0f);
        logo.setRotation(-180f);
        logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .rotation(0f)
                .setDuration(800)
                .setInterpolator(AnimationUtils.loadInterpolator(this,
                        android.R.interpolator.anticipate_overshoot))
                .start();

        // Animate app name: slide up + fade in
        appName.setAlpha(0f);
        appName.setTranslationY(40f);
        appName.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(500)
                .start();

        // Animate tagline: slide up + fade in
        tagline.setAlpha(0f);
        tagline.setTranslationY(24f);
        tagline.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(700)
                .start();

        // Animate version: fade in from bottom
        version.setAlpha(0f);
        version.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(1000)
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            PreferenceManager prefs = new PreferenceManager(this);
            Intent nextIntent;

            if (!prefs.isOnboardingDone()) {
                nextIntent = new Intent(this, OnboardingActivity.class);
            } else if (prefs.isLoggedIn()) {
                nextIntent = new Intent(this, MainActivity.class);
            } else {
                nextIntent = new Intent(this, LoginActivity.class);
            }

            startActivity(nextIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2500);
    }
}
