package com.example.medibloomappv2.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.data.repository.MedicineRepository;
import com.example.medibloomappv2.utils.DateUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private GridLayout calendarGrid;
    private TextView tvMonthYear, tvTaken, tvMissed, tvAdherence;
    private MedicineRepository repository;
    private YearMonth currentMonth;
    private final Map<String, String> statusMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new MedicineRepository(requireContext());
        currentMonth = YearMonth.now();

        calendarGrid = view.findViewById(R.id.calendar_grid);
        tvMonthYear = view.findViewById(R.id.tv_month_year);
        tvTaken = view.findViewById(R.id.tv_days_taken);
        tvMissed = view.findViewById(R.id.tv_days_missed);
        tvAdherence = view.findViewById(R.id.tv_adherence);

        view.findViewById(R.id.btn_prev_month).setOnClickListener(v -> {
            currentMonth = currentMonth.minusMonths(1);
            loadMonthData();
        });
        view.findViewById(R.id.btn_next_month).setOnClickListener(v -> {
            currentMonth = currentMonth.plusMonths(1);
            loadMonthData();
        });

        loadMonthData();
    }

    private void loadMonthData() {
        String start = currentMonth.atDay(1).format(DateUtils.FORMAT_DATE);
        String end = currentMonth.atEndOfMonth().format(DateUtils.FORMAT_DATE);

        tvMonthYear.setText(currentMonth.getMonth().getDisplayName(
                java.time.format.TextStyle.FULL, Locale.getDefault()) + " " + currentMonth.getYear());

        repository.getLogsByDateRange(start, end).observe(getViewLifecycleOwner(), logs -> {
            statusMap.clear();
            Map<String, Integer> takenPerDay = new HashMap<>();
            Map<String, Integer> totalPerDay = new HashMap<>();

            if (logs != null) {
                for (DoseLogEntity log : logs) {
                    totalPerDay.merge(log.dateKey, 1, Integer::sum);
                    if ("TAKEN".equals(log.status)) {
                        takenPerDay.merge(log.dateKey, 1, Integer::sum);
                    }
                }
                for (String date : totalPerDay.keySet()) {
                    int taken = takenPerDay.getOrDefault(date, 0);
                    int total = totalPerDay.getOrDefault(date, 0);
                    if (taken == total) statusMap.put(date, "TAKEN");
                    else if (taken == 0) statusMap.put(date, "MISSED");
                    else statusMap.put(date, "PARTIAL");
                }
            }

            renderCalendar();
            updateSummary();
        });
    }

    private void renderCalendar() {
        calendarGrid.removeAllViews();
        calendarGrid.setColumnCount(7);

        // Day headers
        String[] dayNames = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (String d : dayNames) {
            TextView tv = createDayHeaderView(d);
            calendarGrid.addView(tv);
        }

        // Empty cells before first day
        LocalDate firstDay = currentMonth.atDay(1);
        int startDow = firstDay.getDayOfWeek().getValue() % 7; // 0=Sun
        for (int i = 0; i < startDow; i++) {
            calendarGrid.addView(createEmptyView());
        }

        // Day cells
        int daysInMonth = currentMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            String dateKey = currentMonth.atDay(day).format(DateUtils.FORMAT_DATE);
            String status = statusMap.get(dateKey);
            boolean isToday = dateKey.equals(DateUtils.today());
            calendarGrid.addView(createDayView(day, status, isToday));
        }
    }

    private TextView createDayHeaderView(String text) {
        TextView tv = new TextView(requireContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 8);
        tv.setLayoutParams(params);
        tv.setText(text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        tv.setTextSize(12);
        return tv;
    }

    private View createEmptyView() {
        View v = new View(requireContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 44;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        v.setLayoutParams(params);
        return v;
    }

    private TextView createDayView(int day, String status, boolean isToday) {
        TextView tv = new TextView(requireContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);
        tv.setLayoutParams(params);
        tv.setText(String.valueOf(day));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setPadding(0, 12, 0, 12);
        tv.setTextSize(13);

        int bgRes, textColor;
        if (isToday && status == null) {
            bgRes = R.drawable.bg_day_today;
            textColor = R.color.primary_pink;
        } else if ("TAKEN".equals(status)) {
            bgRes = R.drawable.bg_day_taken;
            textColor = R.color.white;
        } else if ("MISSED".equals(status)) {
            bgRes = R.drawable.bg_day_missed;
            textColor = R.color.white;
        } else if ("PARTIAL".equals(status)) {
            bgRes = R.drawable.bg_day_partial;
            textColor = R.color.white;
        } else {
            bgRes = R.drawable.bg_day_empty;
            textColor = R.color.text_primary;
        }
        tv.setBackground(ContextCompat.getDrawable(requireContext(), bgRes));
        tv.setTextColor(ContextCompat.getColor(requireContext(), textColor));
        return tv;
    }

    private void updateSummary() {
        int takenDays = 0, missedDays = 0;
        for (String s : statusMap.values()) {
            if ("TAKEN".equals(s)) takenDays++;
            else if ("MISSED".equals(s)) missedDays++;
        }
        int total = takenDays + missedDays;
        int adherence = total > 0 ? (takenDays * 100) / total : 0;
        tvTaken.setText(String.valueOf(takenDays));
        tvMissed.setText(String.valueOf(missedDays));
        tvAdherence.setText(adherence + "%");
    }
}

