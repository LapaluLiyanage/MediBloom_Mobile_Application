package com.example.medibloomappv2.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medibloomappv2.data.local.entity.DoseLogEntity;

import java.util.List;

@Dao
public interface DoseLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLog(DoseLogEntity log);

    @Update
    void updateLog(DoseLogEntity log);

    @Query("SELECT * FROM dose_logs WHERE date_key = :dateKey ORDER BY scheduled_at ASC")
    LiveData<List<DoseLogEntity>> getLogsForDate(String dateKey);

    @Query("SELECT * FROM dose_logs WHERE date_key = :dateKey ORDER BY scheduled_at ASC")
    List<DoseLogEntity> getLogsForDateSync(String dateKey);

    @Query("SELECT * FROM dose_logs WHERE date_key BETWEEN :startDate AND :endDate ORDER BY date_key DESC")
    LiveData<List<DoseLogEntity>> getLogsByDateRange(String startDate, String endDate);

    @Query("SELECT * FROM dose_logs WHERE date_key BETWEEN :startDate AND :endDate ORDER BY date_key DESC")
    List<DoseLogEntity> getLogsByDateRangeSync(String startDate, String endDate);

    @Query("SELECT COUNT(*) FROM dose_logs WHERE date_key BETWEEN :startDate AND :endDate AND status = 'TAKEN'")
    int getTakenCountInRange(String startDate, String endDate);

    @Query("SELECT COUNT(*) FROM dose_logs WHERE date_key BETWEEN :startDate AND :endDate")
    int getTotalCountInRange(String startDate, String endDate);

    @Query("SELECT COUNT(*) FROM dose_logs WHERE date_key BETWEEN :startDate AND :endDate AND status = 'TAKEN'")
    LiveData<Integer> getTakenCountLive(String startDate, String endDate);

    @Query("SELECT COUNT(*) FROM dose_logs WHERE date_key BETWEEN :startDate AND :endDate")
    LiveData<Integer> getTotalCountLive(String startDate, String endDate);

    @Query("SELECT DISTINCT date_key FROM dose_logs WHERE status = 'TAKEN' ORDER BY date_key DESC")
    List<String> getAllTakenDates();

    @Query("SELECT * FROM dose_logs WHERE medicine_id = :medicineId AND date_key = :dateKey LIMIT 1")
    DoseLogEntity getLogForMedicineOnDate(long medicineId, String dateKey);

    @Query("UPDATE dose_logs SET status = 'TAKEN', taken_at = :takenAt WHERE id = :logId")
    void markAsTaken(long logId, long takenAt);

    @Query("UPDATE dose_logs SET status = 'MISSED' WHERE date_key < :today AND status = 'UPCOMING'")
    void markOverdueAsMissed(String today);

    @Query("SELECT * FROM dose_logs ORDER BY scheduled_at DESC LIMIT 50")
    LiveData<List<DoseLogEntity>> getRecentLogs();
}

