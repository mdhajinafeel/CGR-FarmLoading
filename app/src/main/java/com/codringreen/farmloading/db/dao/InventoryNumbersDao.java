package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.InventoryNumbers;

import java.util.List;

@Dao
public interface InventoryNumbersDao {

    @Query("DELETE FROM InventoryNumbers")
    void deleteAll();

    @Query("SELECT * FROM InventoryNumbers")
    List<InventoryNumbers> getInventoryNumbers();

    @Query("SELECT COUNT(*) AS cnt FROM InventoryNumbers WHERE inventoryNumber = :inventoryNumber AND supplierId = :supplierId")
    int getInventoryCount(String inventoryNumber, int supplierId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInventoryNumbers(InventoryNumbers inventoryNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInventoryNumbers(List<InventoryNumbers> inventoryNumbers);
}