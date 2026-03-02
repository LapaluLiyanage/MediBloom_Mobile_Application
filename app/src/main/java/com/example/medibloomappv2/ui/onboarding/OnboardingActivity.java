package com.example.medibloomappv2.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.ui.auth.LoginActivity;
import com.example.medibloomappv2.utils.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView tvSkip;

    private final String[] titles = {
            "Your health deserves gentle care 💗",
            "Never miss a dose again 🌸",
            "Small steps. Big health. 🌼"
    };
    private final String[] descriptions = {
            "Take control of your medication routine with ease and comfort",
            "Smart reminders that adapt to your schedule and lifestyle",
            "Track your progress and build healthy habits every day"
    };
    private final int[] icons = {
            R.drawable.ic_heart,
            R.drawable.ic_pill,
            R.drawable.ic_sparkles
    };
    private final int[] colors = {
            R.color.onboarding_pink,
            R.color.onboarding_purple,
            R.color.onboarding_blue
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.view_pager);
        btnNext = findViewById(R.id.btn_next);
        tvSkip = findViewById(R.id.tv_skip);
        TabLayout tabLayout = findViewById(R.id.tab_indicator);

        OnboardingAdapter adapter = new OnboardingAdapter(this, titles, descriptions, icons, colors);
        viewPager.setAdapter(adapter);

        // Dot indicators via TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateButtonText(position);
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < titles.length - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                goToLogin();
            }
        });

        tvSkip.setOnClickListener(v -> goToLogin());
    }

    private void updateButtonText(int position) {
        if (position == titles.length - 1) {
            btnNext.setText("Get Started");
        } else {
            btnNext.setText("Next");
        }
    }

    private void goToLogin() {
        new PreferenceManager(this).setOnboardingDone(true);
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}

