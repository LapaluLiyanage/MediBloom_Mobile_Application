package com.example.medibloomappv2.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.data.repository.MedicineRepository;
import com.example.medibloomappv2.utils.DateUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private MedicineRepository repository;
    private TextView tvAdherenceRate, tvStreak, tvMonthTaken, tvMonthMissed;
    private ProgressBar progressAdherence;
    private PieChart pieChart;
    private RecyclerView rvDailyLog;
    private DailyLogAdapter logAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new MedicineRepository(requireContext());

        tvAdherenceRate = view.findViewById(R.id.tv_adherence_rate);
        tvStreak = view.findViewById(R.id.tv_streak);
        tvMonthTaken = view.findViewById(R.id.tv_month_taken);
        tvMonthMissed = view.findViewById(R.id.tv_month_missed);
        progressAdherence = view.findViewById(R.id.progress_adherence);
        pieChart = view.findViewById(R.id.pie_chart);
        rvDailyLog = view.findViewById(R.id.rv_daily_log);

        rvDailyLog.setLayoutManager(new LinearLayoutManager(requireContext()));
        logAdapter = new DailyLogAdapter();
        rvDailyLog.setAdapter(logAdapter);

        setupPieChart();
        loadData();
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(55f);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false);
    }

    private void loadData() {
        String weekStart = DateUtils.weekStartDate();
        String today = DateUtils.today();
        String monthStart = DateUtils.monthStartDate();

        // Weekly adherence
        repository.getAdherenceStats(weekStart, today, (taken, total) -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> {
                int pct = total > 0 ? (taken * 100) / total : 0;
                tvAdherenceRate.setText(pct + "%");
                progressAdherence.setProgress(pct);
                updatePieChart(taken, total - taken);
            });
        });

        // Streak
        repository.getCurrentStreak(streak -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> tvStreak.setText(streak + " Days"));
        });

        // Monthly stats
        repository.getAdherenceStats(monthStart, today, (taken, total) -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> {
                tvMonthTaken.setText(String.valueOf(taken));
                tvMonthMissed.setText(String.valueOf(total - taken));
            });
        });

        // Recent logs
        repository.getLogsByDateRange(weekStart, today).observe(getViewLifecycleOwner(),
                this::groupAndDisplayLogs);
    }

    private void updatePieChart(int taken, int missed) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(taken > 0 ? taken : 0.001f, "Taken"));
        entries.add(new PieEntry(missed > 0 ? missed : 0.001f, "Missed"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(0xFF4CAF50, 0xFFEF5350);
        dataSet.setSliceSpace(3f);
        dataSet.setDrawValues(false);

        pieChart.setData(new PieData(dataSet));
        pieChart.animateY(800);
        pieChart.invalidate();
    }

    private void groupAndDisplayLogs(List<DoseLogEntity> logs) {
        logAdapter.submitList(logs);
    }
}

