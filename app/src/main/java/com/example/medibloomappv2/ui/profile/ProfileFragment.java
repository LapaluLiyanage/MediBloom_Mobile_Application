package com.example.medibloomappv2.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.repository.MedicineRepository;
import com.example.medibloomappv2.ui.auth.LoginActivity;
import com.example.medibloomappv2.utils.PreferenceManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileFragment extends Fragment {

    private PreferenceManager prefs;
    private MedicineRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = new PreferenceManager(requireContext());
        repository = new MedicineRepository(requireContext());

        // User info
        TextView tvName = view.findViewById(R.id.tv_user_name);
        TextView tvEmail = view.findViewById(R.id.tv_user_email);
        TextView tvStreak = view.findViewById(R.id.tv_streak_stat);
        TextView tvAdherence = view.findViewById(R.id.tv_adherence_stat);
        TextView tvMedCount = view.findViewById(R.id.tv_med_count_stat);

        tvName.setText(prefs.getUserName());
        tvEmail.setText(prefs.getUserEmail());

        // Load stats
        repository.getCurrentStreak(streak ->
                requireActivity().runOnUiThread(() -> tvStreak.setText(streak + " Days")));

        String userId = prefs.getUserId() != null ? prefs.getUserId() : "local";
        repository.getMedicineCount(userId).observe(getViewLifecycleOwner(),
                count -> tvMedCount.setText(count != null ? String.valueOf(count) : "0"));

        repository.getAdherenceStats(com.example.medibloomappv2.utils.DateUtils.weekStartDate(),
                com.example.medibloomappv2.utils.DateUtils.today(), (taken, total) -> {
                    if (!isAdded()) return;
                    int pct = total > 0 ? (taken * 100) / total : 0;
                    requireActivity().runOnUiThread(() -> tvAdherence.setText(pct + "%"));
                });

        // Switches
        SwitchMaterial switchNotifications = view.findViewById(R.id.switch_notifications);
        SwitchMaterial switchSound = view.findViewById(R.id.switch_sound);
        SwitchMaterial switchDarkMode = view.findViewById(R.id.switch_dark_mode);

        switchNotifications.setChecked(prefs.isNotificationsEnabled());
        switchSound.setChecked(prefs.isReminderSoundEnabled());
        switchDarkMode.setChecked(prefs.isDarkMode());

        switchNotifications.setOnCheckedChangeListener((btn, checked) ->
                prefs.setNotificationsEnabled(checked));

        switchSound.setOnCheckedChangeListener((btn, checked) ->
                prefs.setReminderSound(checked));

        switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            prefs.setDarkMode(checked);
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        // Logout
        view.findViewById(R.id.btn_logout).setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (d, w) -> {
                    if (!isAdded() || getActivity() == null) return;
                    // Synchronously clear user session (commit, not apply)
                    prefs.clearUserData();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    requireActivity().startActivity(intent);
                    // Finish the host activity to remove it from the back-stack
                    requireActivity().finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}




