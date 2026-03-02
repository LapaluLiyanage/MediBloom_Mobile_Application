package com.example.medibloomappv2.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "dose_logs",
        foreignKeys = @ForeignKey(entity = MedicineEntity.class,
                parentColumns = "id",
                childColumns = "medicine_id",
                onDelete = ForeignKey.CASCADE))
public class DoseLogEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "medicine_id", index = true)
    public long medicineId;

    @ColumnInfo(name = "medicine_name")
    public String medicineName;

    @ColumnInfo(name = "scheduled_at")
    public long scheduledAt; // Unix timestamp

    @ColumnInfo(name = "status")
    public String status; // TAKEN, MISSED, UPCOMING

    @ColumnInfo(name = "taken_at")
    public long takenAt; // Unix timestamp, 0 if not taken

    @ColumnInfo(name = "date_key")
    public String dateKey; // yyyy-MM-dd for easy querying

    public DoseLogEntity() {}

    @Ignore
    public DoseLogEntity(long medicineId, String medicineName, long scheduledAt,
                         String status, String dateKey) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.dateKey = dateKey;
        this.takenAt = 0;
    }
}



