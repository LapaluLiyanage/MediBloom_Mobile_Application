package com.example.medibloomappv2.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.medibloomappv2.data.local.entity.ReminderTimeEntity;

import java.util.List;

@Dao
public interface ReminderTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminderTime(ReminderTimeEntity reminderTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminderTimes(List<ReminderTimeEntity> reminderTimes);

    @Delete
    void deleteReminderTime(ReminderTimeEntity reminderTime);

    @Query("SELECT * FROM reminder_times WHERE medicine_id = :medicineId")
    List<ReminderTimeEntity> getReminderTimesForMedicine(long medicineId);

    @Query("DELETE FROM reminder_times WHERE medicine_id = :medicineId")
    void deleteReminderTimesForMedicine(long medicineId);

    @Query("SELECT * FROM reminder_times")
    List<ReminderTimeEntity> getAllReminderTimes();
}

