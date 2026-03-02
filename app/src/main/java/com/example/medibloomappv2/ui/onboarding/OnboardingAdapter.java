package com.example.medibloomappv2.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OnboardingAdapter extends FragmentStateAdapter {

    private final String[] titles;
    private final String[] descriptions;
    private final int[] icons;
    private final int[] colors;

    public OnboardingAdapter(FragmentActivity activity, String[] titles,
                             String[] descriptions, int[] icons, int[] colors) {
        super(activity);
        this.titles = titles;
        this.descriptions = descriptions;
        this.icons = icons;
        this.colors = colors;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return OnboardingPageFragment.newInstance(
                titles[position], descriptions[position], icons[position], colors[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}

