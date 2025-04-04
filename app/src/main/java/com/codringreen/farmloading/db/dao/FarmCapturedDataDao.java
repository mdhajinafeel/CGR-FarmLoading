package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.FarmCapturedData;

import java.util.List;

@Dao
public interface FarmCapturedDataDao {

    @Query("DELETE FROM FarmCapturedData")
    void deleteAll();

    @Query("DELETE FROM FarmCapturedData WHERE inventoryOrder = :inventoryOrder")
    void deleteFarmCapturedData(String inventoryOrder);

    @Query("DELETE FROM FarmCapturedData WHERE pieces = :pieces AND inventoryOrder = :inventoryOrder " +
            "AND circumference = :circumference AND length = :length AND grossVolume = :grossVolume AND netVolume = :netVolume " +
            "AND captureTimeStamp = :capturedTimeStamp")
    int deleteFarmCapturedData(String inventoryOrder, int pieces, double circumference, double length, double grossVolume, double netVolume, String capturedTimeStamp);

    @Query("SELECT * FROM FarmCapturedData")
    List<FarmCapturedData> getFarmCapturedData();

    @Query("SELECT * FROM FarmCapturedData WHERE inventoryOrder = :inventoryOrder")
    List<FarmCapturedData> getFarmCapturedData(String inventoryOrder);

    @Query("SELECT COUNT(*) AS cnt FROM FarmCapturedData WHERE inventoryOrder = :inventoryOrder")
    int getFarmCapturedDataCount(String inventoryOrder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceFarmCapturedData(FarmCapturedData farmDataEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceFarmCapturedData(List<FarmCapturedData> farmDataEntryList);

    @Query("UPDATE FarmCapturedData SET circumference = :circumference, length = :length, grossVolume = :grossVolume, " +
            "netVolume = :netVolume, pieces = :pieces " +
            "WHERE captureTimeStamp = :captureTimeStamp " +
            "AND inventoryOrder = :inventoryOrder")
    void updateFarmCapturedData(String inventoryOrder, int pieces, double length, double circumference, double grossVolume, double netVolume, String captureTimeStamp);
}