package com.example.medibloomappv2.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medibloomappv2.data.local.AppDatabase;
import com.example.medibloomappv2.data.local.dao.MoodDao;
import com.example.medibloomappv2.data.local.entity.MoodEntryEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoodRepository {

    private final MoodDao moodDao;
    private final ExecutorService executor;

    public MoodRepository(Context context) {
        moodDao = AppDatabase.getInstance(context).moodDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insertMood(MoodEntryEntity mood, OnMoodInserted callback) {
        executor.execute(() -> {
            long id = moodDao.insertMood(mood);
            if (callback != null) callback.onInserted(id);
        });
    }

    public LiveData<List<MoodEntryEntity>> getMoodHistory(String userId) {
        return moodDao.getMoodHistory(userId);
    }

    public LiveData<List<MoodEntryEntity>> getRecentMoods(String userId) {
        return moodDao.getRecentMoods(userId);
    }

    public void getWeeklyInsights(String userId, String startDate, String endDate,
                                   OnInsightsLoaded callback) {
        executor.execute(() -> {
            int good = moodDao.getMoodTypeCountInRange(userId, "GOOD", startDate, endDate);
            int okay = moodDao.getMoodTypeCountInRange(userId, "OKAY", startDate, endDate);
            int notWell = moodDao.getMoodTypeCountInRange(userId, "NOT_WELL", startDate, endDate);
            if (callback != null) callback.onLoaded(good, okay, notWell);
        });
    }

    public interface OnMoodInserted {
        void onInserted(long id);
    }

    public interface OnInsightsLoaded {
        void onLoaded(int good, int okay, int notWell);
    }
}

