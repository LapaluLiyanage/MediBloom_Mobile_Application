package com.example.medibloomappv2.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.medibloomappv2.data.local.dao.DoseLogDao;
import com.example.medibloomappv2.data.local.dao.MedicineDao;
import com.example.medibloomappv2.data.local.dao.MoodDao;
import com.example.medibloomappv2.data.local.dao.ReminderTimeDao;
import com.example.medibloomappv2.data.local.dao.UserDao;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.data.local.entity.MedicineEntity;
import com.example.medibloomappv2.data.local.entity.MoodEntryEntity;
import com.example.medibloomappv2.data.local.entity.ReminderTimeEntity;
import com.example.medibloomappv2.data.local.entity.UserEntity;

@Database(entities = {
        UserEntity.class,
        MedicineEntity.class,
        ReminderTimeEntity.class,
        DoseLogEntity.class,
        MoodEntryEntity.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract MedicineDao medicineDao();
    public abstract ReminderTimeDao reminderTimeDao();
    public abstract DoseLogDao doseLogDao();
    public abstract MoodDao moodDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "medibloom_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
