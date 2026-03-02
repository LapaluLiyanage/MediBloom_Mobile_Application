package com.example.medibloomappv2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.medibloomappv2.data.local.AppDatabase;
import com.example.medibloomappv2.data.local.entity.MedicineEntity;
import com.example.medibloomappv2.data.local.entity.ReminderTimeEntity;
import com.example.medibloomappv2.utils.NotificationHelper;
import com.example.medibloomappv2.utils.PreferenceManager;

import java.util.List;
import java.util.concurrent.Executors;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            rescheduleAllReminders(context);
        }
    }

    private void rescheduleAllReminders(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            PreferenceManager prefs = new PreferenceManager(context);
            String userId = prefs.getUserId();
            if (userId == null) return;

            AppDatabase db = AppDatabase.getInstance(context);
            List<MedicineEntity> medicines = db.medicineDao().getAllMedicinesSync(userId);

            for (MedicineEntity med : medicines) {
                List<ReminderTimeEntity> times = db.reminderTimeDao()
                        .getReminderTimesForMedicine(med.id);
                for (ReminderTimeEntity t : times) {
                    NotificationHelper.scheduleReminder(
                            context, med.id, med.name, med.dosage, t.timeHour, t.timeMinute);
                }
            }
        });
    }
}

