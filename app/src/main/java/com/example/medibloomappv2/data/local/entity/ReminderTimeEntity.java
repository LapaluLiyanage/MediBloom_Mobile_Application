package com.example.medibloomappv2.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder_times",
        foreignKeys = @ForeignKey(entity = MedicineEntity.class,
                parentColumns = "id",
                childColumns = "medicine_id",
                onDelete = ForeignKey.CASCADE))
public class ReminderTimeEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "medicine_id", index = true)
    public long medicineId;

    @ColumnInfo(name = "time_hour")
    public int timeHour;

    @ColumnInfo(name = "time_minute")
    public int timeMinute;

    public ReminderTimeEntity() {}

    @Ignore
    public ReminderTimeEntity(long medicineId, int timeHour, int timeMinute) {
        this.medicineId = medicineId;
        this.timeHour = timeHour;
        this.timeMinute = timeMinute;
    }
}



