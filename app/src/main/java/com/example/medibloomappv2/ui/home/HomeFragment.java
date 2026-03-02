package com.example.medibloomappv2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.data.repository.MedicineRepository;
import com.example.medibloomappv2.ui.addmedicine.AddMedicineActivity;
import com.example.medibloomappv2.utils.DateUtils;
import com.example.medibloomappv2.utils.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvReminders;
    private ReminderAdapter reminderAdapter;
    private TextView tvGreeting, tvUserName, tvProgressText, tvTaken, tvMissed, tvUpcoming;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private MedicineRepository medicineRepository;
    private PreferenceManager prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = new PreferenceManager(requireContext());
        medicineRepository = new MedicineRepository(requireContext());

        tvGreeting = view.findViewById(R.id.tv_greeting);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvProgressText = view.findViewById(R.id.tv_progress_text);
        tvTaken = view.findViewById(R.id.tv_taken_count);
        tvMissed = view.findViewById(R.id.tv_missed_count);
        tvUpcoming = view.findViewById(R.id.tv_upcoming_count);
        progressBar = view.findViewById(R.id.progress_bar);
        fabAdd = view.findViewById(R.id.fab_add);
        rvReminders = view.findViewById(R.id.rv_reminders);

        tvGreeting.setText(DateUtils.getGreeting());
        tvUserName.setText("Hello, " + prefs.getUserName() + " 👋");

        rvReminders.setLayoutManager(new LinearLayoutManager(requireContext()));
        reminderAdapter = new ReminderAdapter(logId -> {
            medicineRepository.markDoseAsTaken(logId, System.currentTimeMillis());
        });
        rvReminders.setAdapter(reminderAdapter);

        // FAB spring animation
        fabAdd.setScaleX(0f);
        fabAdd.setScaleY(0f);
        SpringAnimation springX = new SpringAnimation(fabAdd, SpringAnimation.SCALE_X, 1f);
        SpringAnimation springY = new SpringAnimation(fabAdd, SpringAnimation.SCALE_Y, 1f);
        springX.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM)
                .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springY.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM)
                .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            springX.start();
            springY.start();
        }, 300);

        fabAdd.setOnClickListener(v -> {
            v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(100).withEndAction(() ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction(() -> {
                        startActivity(new Intent(requireContext(), AddMedicineActivity.class));
                    }).start()
            ).start();
        });

        // Mark overdue as missed
        medicineRepository.markOverdueAsMissed(DateUtils.today());

        // Observe today's doses
        medicineRepository.getLogsForDate(DateUtils.today()).observe(getViewLifecycleOwner(),
                this::updateUI);
    }

    private void updateUI(List<DoseLogEntity> logs) {
        if (logs == null) return;

        int taken = 0, missed = 0, upcoming = 0;
        for (DoseLogEntity log : logs) {
            switch (log.status) {
                case "TAKEN": taken++; break;
                case "MISSED": missed++; break;
                case "UPCOMING": upcoming++; break;
            }
        }

        tvTaken.setText(String.valueOf(taken));
        tvMissed.setText(String.valueOf(missed));
        tvUpcoming.setText(String.valueOf(upcoming));

        int total = logs.size();
        int progress = total > 0 ? (taken * 100) / total : 0;
        progressBar.setProgress(progress);
        tvProgressText.setText(taken + "/" + total + " done");

        reminderAdapter.submitList(logs);
    }
}

