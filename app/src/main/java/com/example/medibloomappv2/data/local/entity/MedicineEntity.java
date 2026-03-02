package com.example.medibloomappv2.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicines")
public class MedicineEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "dosage")
    public String dosage;

    @ColumnInfo(name = "stock_amount")
    public int stockAmount;

    @ColumnInfo(name = "start_date")
    public String startDate; // ISO format: yyyy-MM-dd

    @ColumnInfo(name = "end_date")
    public String endDate; // nullable

    @ColumnInfo(name = "repeat_type")
    public String repeatType; // DAILY, WEEKLY, CUSTOM

    @ColumnInfo(name = "color_tag")
    public String colorTag; // hex color for card

    @ColumnInfo(name = "user_id")
    public String userId;

    @ColumnInfo(name = "is_active")
    public boolean isActive = true;

    public MedicineEntity() {}

    @Ignore
    public MedicineEntity(String name, String dosage, int stockAmount, String startDate,
                          String endDate, String repeatType, String colorTag, String userId) {
        this.name = name;
        this.dosage = dosage;
        this.stockAmount = stockAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.repeatType = repeatType;
        this.colorTag = colorTag;
        this.userId = userId;
        this.isActive = true;
    }
}



