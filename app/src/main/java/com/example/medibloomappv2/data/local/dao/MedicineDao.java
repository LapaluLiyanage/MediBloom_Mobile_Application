package com.example.medibloomappv2.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medibloomappv2.data.local.entity.MedicineEntity;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedicine(MedicineEntity medicine);

    @Update
    void updateMedicine(MedicineEntity medicine);

    @Delete
    void deleteMedicine(MedicineEntity medicine);

    @Query("SELECT * FROM medicines WHERE user_id = :userId AND is_active = 1 ORDER BY name ASC")
    LiveData<List<MedicineEntity>> getAllMedicines(String userId);

    @Query("SELECT * FROM medicines WHERE user_id = :userId AND is_active = 1 ORDER BY name ASC")
    List<MedicineEntity> getAllMedicinesSync(String userId);

    @Query("SELECT * FROM medicines WHERE id = :id")
    MedicineEntity getMedicineById(long id);

    @Query("SELECT COUNT(*) FROM medicines WHERE user_id = :userId AND is_active = 1")
    LiveData<Integer> getMedicineCount(String userId);

    @Query("UPDATE medicines SET is_active = 0 WHERE id = :id")
    void softDeleteMedicine(long id);

    @Query("UPDATE medicines SET stock_amount = stock_amount - 1 WHERE id = :id AND stock_amount > 0")
    void decrementStock(long id);
}

