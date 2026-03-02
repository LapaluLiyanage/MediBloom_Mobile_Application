package com.example.medibloomappv2.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.medibloomappv2.R;

public class OnboardingPageFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_ICON = "icon";
    private static final String ARG_COLOR = "color";

    public static OnboardingPageFragment newInstance(String title, String desc, int icon, int color) {
        OnboardingPageFragment f = new OnboardingPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putInt(ARG_ICON, icon);
        args.putInt(ARG_COLOR, color);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_page, container, false);

        Bundle args = getArguments();
        if (args == null) return view;

        View iconCircle = view.findViewById(R.id.icon_circle);
        ImageView ivIcon = view.findViewById(R.id.iv_icon);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvDesc = view.findViewById(R.id.tv_description);

        tvTitle.setText(args.getString(ARG_TITLE));
        tvDesc.setText(args.getString(ARG_DESC));
        ivIcon.setImageResource(args.getInt(ARG_ICON));
        iconCircle.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), args.getInt(ARG_COLOR)));

        // Animate icon circle
        iconCircle.setAlpha(0f);
        iconCircle.setScaleX(0.5f);
        iconCircle.setScaleY(0.5f);
        iconCircle.animate()
                .alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(500).setStartDelay(100).start();

        return view;
    }
}

