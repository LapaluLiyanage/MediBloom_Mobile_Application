package com.example.medibloomappv2.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mood_entries")
public class MoodEntryEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "date_key")
    public String dateKey; // yyyy-MM-dd

    @ColumnInfo(name = "mood_type")
    public String moodType; // GOOD, OKAY, NOT_WELL

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "user_id")
    public String userId;

    public MoodEntryEntity() {}

    @Ignore
    public MoodEntryEntity(String dateKey, String moodType, String note, long timestamp, String userId) {
        this.dateKey = dateKey;
        this.moodType = moodType;
        this.note = note;
        this.timestamp = timestamp;
        this.userId = userId;
    }
}



