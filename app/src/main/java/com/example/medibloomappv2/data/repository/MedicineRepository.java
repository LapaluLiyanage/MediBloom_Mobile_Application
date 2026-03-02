package com.example.medibloomappv2.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medibloomappv2.data.local.AppDatabase;
import com.example.medibloomappv2.data.local.dao.DoseLogDao;
import com.example.medibloomappv2.data.local.dao.MedicineDao;
import com.example.medibloomappv2.data.local.dao.ReminderTimeDao;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.data.local.entity.MedicineEntity;
import com.example.medibloomappv2.data.local.entity.ReminderTimeEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicineRepository {

    private final MedicineDao medicineDao;
    private final ReminderTimeDao reminderTimeDao;
    private final DoseLogDao doseLogDao;
    private final ExecutorService executor;

    public MedicineRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        medicineDao = db.medicineDao();
        reminderTimeDao = db.reminderTimeDao();
        doseLogDao = db.doseLogDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insertMedicineWithTimes(MedicineEntity medicine, List<ReminderTimeEntity> times,
                                        OnMedicineInserted callback) {
        executor.execute(() -> {
            long id = medicineDao.insertMedicine(medicine);
            for (ReminderTimeEntity t : times) {
                t.medicineId = id;
            }
            reminderTimeDao.insertReminderTimes(times);
            if (callback != null) callback.onInserted(id);
        });
    }

    public void deleteMedicine(long id) {
        executor.execute(() -> medicineDao.softDeleteMedicine(id));
    }

    public LiveData<List<MedicineEntity>> getAllMedicines(String userId) {
        return medicineDao.getAllMedicines(userId);
    }

    public void getAllMedicinesAsync(String userId, OnMedicinesLoaded callback) {
        executor.execute(() -> {
            List<MedicineEntity> list = medicineDao.getAllMedicinesSync(userId);
            if (callback != null) callback.onLoaded(list);
        });
    }

    public LiveData<Integer> getMedicineCount(String userId) {
        return medicineDao.getMedicineCount(userId);
    }

    public List<ReminderTimeEntity> getReminderTimesForMedicine(long medicineId) {
        return reminderTimeDao.getReminderTimesForMedicine(medicineId);
    }

    public LiveData<List<DoseLogEntity>> getLogsForDate(String dateKey) {
        return doseLogDao.getLogsForDate(dateKey);
    }

    public LiveData<List<DoseLogEntity>> getLogsByDateRange(String start, String end) {
        return doseLogDao.getLogsByDateRange(start, end);
    }

    public void insertDoseLog(DoseLogEntity log) {
        executor.execute(() -> doseLogDao.insertLog(log));
    }

    public void markDoseAsTaken(long logId, long takenAt) {
        executor.execute(() -> doseLogDao.markAsTaken(logId, takenAt));
    }

    public void markOverdueAsMissed(String today) {
        executor.execute(() -> doseLogDao.markOverdueAsMissed(today));
    }

    public void getAdherenceStats(String startDate, String endDate, OnStatsLoaded callback) {
        executor.execute(() -> {
            int taken = doseLogDao.getTakenCountInRange(startDate, endDate);
            int total = doseLogDao.getTotalCountInRange(startDate, endDate);
            if (callback != null) callback.onLoaded(taken, total);
        });
    }

    public void getCurrentStreak(OnStreakLoaded callback) {
        executor.execute(() -> {
            List<String> takenDates = doseLogDao.getAllTakenDates();
            int streak = calculateStreak(takenDates);
            if (callback != null) callback.onLoaded(streak);
        });
    }

    private int calculateStreak(List<String> sortedDatesDesc) {
        if (sortedDatesDesc == null || sortedDatesDesc.isEmpty()) return 0;
        int streak = 0;
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 0; i < sortedDatesDesc.size(); i++) {
            java.time.LocalDate date = java.time.LocalDate.parse(sortedDatesDesc.get(i));
            java.time.LocalDate expected = today.minusDays(i);
            if (date.equals(expected)) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    public interface OnMedicineInserted {
        void onInserted(long id);
    }

    public interface OnMedicinesLoaded {
        void onLoaded(List<MedicineEntity> medicines);
    }

    public interface OnStatsLoaded {
        void onLoaded(int taken, int total);
    }

    public interface OnStreakLoaded {
        void onLoaded(int streak);
    }
}

