package com.example.medibloomappv2.ui.addmedicine;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.data.local.entity.MedicineEntity;
import com.example.medibloomappv2.data.local.entity.ReminderTimeEntity;
import com.example.medibloomappv2.data.repository.MedicineRepository;
import com.example.medibloomappv2.utils.DateUtils;
import com.example.medibloomappv2.utils.NotificationHelper;
import com.example.medibloomappv2.utils.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddMedicineActivity extends AppCompatActivity {

    private TextInputEditText etName, etDosage, etStock, etStartDate, etEndDate;
    private LinearLayout llReminderTimes;
    private MaterialButton btnSave;
    private ChipGroup chipGroupRepeat;
    private final List<int[]> reminderTimes = new ArrayList<>(); // [hour, minute]
    private MedicineRepository repository;
    private PreferenceManager prefs;
    private String selectedRepeat = "DAILY";
    private String startDate = "", endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        repository = new MedicineRepository(this);
        prefs = new PreferenceManager(this);

        etName = findViewById(R.id.et_medicine_name);
        etDosage = findViewById(R.id.et_dosage);
        etStock = findViewById(R.id.et_stock);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        llReminderTimes = findViewById(R.id.ll_reminder_times);
        btnSave = findViewById(R.id.btn_save);
        chipGroupRepeat = findViewById(R.id.chip_group_repeat);

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Add first default reminder time
        addReminderTimeRow(8, 0);

        // Add time button
        findViewById(R.id.btn_add_time).setOnClickListener(v -> addReminderTimeRow(12, 0));

        // Date pickers
        etStartDate.setOnClickListener(v -> showDatePicker(true));
        etEndDate.setOnClickListener(v -> showDatePicker(false));

        // Repeat chip group
        chipGroupRepeat.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == R.id.chip_daily) selectedRepeat = "DAILY";
            else if (id == R.id.chip_weekly) selectedRepeat = "WEEKLY";
            else if (id == R.id.chip_custom) selectedRepeat = "CUSTOM";
        });

        btnSave.setOnClickListener(v -> saveMedicine());
    }

    private void addReminderTimeRow(int hour, int minute) {
        int[] time = {hour, minute};
        reminderTimes.add(time);

        View row = getLayoutInflater().inflate(R.layout.item_reminder_time_row, llReminderTimes, false);
        TextView tvTime = row.findViewById(R.id.tv_time);
        tvTime.setText(DateUtils.formatTime(hour, minute));

        int index = reminderTimes.size() - 1;
        tvTime.setOnClickListener(v -> {
            TimePickerDialog picker = new TimePickerDialog(this,
                    (view, h, m) -> {
                        reminderTimes.get(index)[0] = h;
                        reminderTimes.get(index)[1] = m;
                        tvTime.setText(DateUtils.formatTime(h, m));
                    }, time[0], time[1], false);
            picker.show();
        });

        View btnRemove = row.findViewById(R.id.btn_remove_time);
        if (reminderTimes.size() == 1) {
            btnRemove.setVisibility(View.INVISIBLE);
        }
        btnRemove.setOnClickListener(v -> {
            llReminderTimes.removeView(row);
            reminderTimes.remove(index);
        });

        row.setAlpha(0f);
        llReminderTimes.addView(row);
        row.animate().alpha(1f).translationY(0).setDuration(300).start();
    }

    private void showDatePicker(boolean isStart) {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(isStart ? "Select Start Date" : "Select End Date")
                .build();
        picker.addOnPositiveButtonClickListener(selection -> {
            String formatted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date(selection));
            String display = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(new Date(selection));
            if (isStart) {
                startDate = formatted;
                etStartDate.setText(display);
            } else {
                endDate = formatted;
                etEndDate.setText(display);
            }
        });
        picker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void saveMedicine() {
        String name = getText(etName);
        String dosage = getText(etDosage);
        String stockStr = getText(etStock);

        if (name.isEmpty()) { etName.setError("Medicine name is required"); return; }
        if (dosage.isEmpty()) { etDosage.setError("Dosage is required"); return; }
        if (startDate.isEmpty()) { Toast.makeText(this, "Please select a start date", Toast.LENGTH_SHORT).show(); return; }

        int stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);
        String userId = prefs.getUserId() != null ? prefs.getUserId() : "local";

        // Assign a color based on name hash
        String[] colors = {"#F8BBD0", "#90CAF9", "#CE93D8", "#A5D6A7", "#FFCC80"};
        String color = colors[Math.abs(name.hashCode()) % colors.length];

        MedicineEntity medicine = new MedicineEntity(
                name, dosage, stock, startDate, endDate, selectedRepeat, color, userId);

        List<ReminderTimeEntity> times = new ArrayList<>();
        for (int[] t : reminderTimes) {
            times.add(new ReminderTimeEntity(0, t[0], t[1]));
        }

        btnSave.setEnabled(false);

        repository.insertMedicineWithTimes(medicine, times, medicineId -> {
            // Create today's dose logs
            for (int[] t : reminderTimes) {
                DoseLogEntity log = new DoseLogEntity(
                        medicineId, name,
                        DateUtils.getScheduledTimestampForToday(t[0], t[1]),
                        "UPCOMING", DateUtils.today());
                repository.insertDoseLog(log);

                // Schedule notification
                NotificationHelper.scheduleReminder(
                        AddMedicineActivity.this, medicineId, name, dosage, t[0], t[1]);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Medicine saved! 🌸", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}

