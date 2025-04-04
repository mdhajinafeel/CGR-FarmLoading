package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.FarmDetails;

import java.util.List;

@Dao
public interface FarmDetailsDao {

    @Query("DELETE FROM FarmDetails")
    void deleteAll();

    @Query("SELECT * FROM FarmDetails")
    List<FarmDetails> getFarmDetails();

    @Query("SELECT * FROM FarmDetails WHERE inventoryOrder = :inventoryOrder AND supplierId = :supplierId")
    FarmDetails getFarmDetails(String inventoryOrder, int supplierId);

    @Query("SELECT COUNT(*) AS cnt FROM FarmDetails WHERE inventoryOrder = :inventoryOrder")
    int getInventoryCount(String inventoryOrder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrReplaceFarmDetails(FarmDetails farmDetails);

    @Query("UPDATE FarmDetails SET netVolume = :netVolume, grossVolume = :grossVolume, totalPieces = :pieces WHERE supplierId = :supplierId " +
            "AND inventoryOrder = :inventoryOrder")
    void updateFarmDetails(String inventoryOrder, int supplierId, int pieces, double grossVolume, double netVolume);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceFarmDetails(List<FarmDetails> farmDetails);
}