package com.example.medibloomappv2.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.medibloomappv2.data.local.entity.MoodEntryEntity;

import java.util.List;

@Dao
public interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMood(MoodEntryEntity mood);

    @Query("SELECT * FROM mood_entries WHERE user_id = :userId ORDER BY timestamp DESC")
    LiveData<List<MoodEntryEntity>> getMoodHistory(String userId);

    @Query("SELECT * FROM mood_entries WHERE user_id = :userId ORDER BY timestamp DESC")
    List<MoodEntryEntity> getMoodHistorySync(String userId);

    @Query("SELECT * FROM mood_entries WHERE user_id = :userId AND date_key BETWEEN :startDate AND :endDate ORDER BY date_key DESC")
    List<MoodEntryEntity> getMoodInRange(String userId, String startDate, String endDate);

    @Query("SELECT COUNT(*) FROM mood_entries WHERE user_id = :userId AND mood_type = :moodType AND date_key BETWEEN :startDate AND :endDate")
    int getMoodTypeCountInRange(String userId, String moodType, String startDate, String endDate);

    @Query("SELECT * FROM mood_entries WHERE user_id = :userId AND date_key = :dateKey LIMIT 1")
    MoodEntryEntity getMoodForDate(String userId, String dateKey);

    @Query("SELECT * FROM mood_entries WHERE user_id = :userId ORDER BY timestamp DESC LIMIT 5")
    LiveData<List<MoodEntryEntity>> getRecentMoods(String userId);
}

