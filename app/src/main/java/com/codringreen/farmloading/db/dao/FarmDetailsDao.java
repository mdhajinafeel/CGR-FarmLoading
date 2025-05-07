package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.model.FarmDetailDashboardModel;

import java.util.List;

@Dao
public interface FarmDetailsDao {

    @Query("DELETE FROM FarmDetails WHERE isSynced = 1")
    void deleteAll();

    @Query("SELECT * FROM FarmDetails WHERE isForData = 0 ORDER BY purchaseDate DESC")
    List<FarmDetails> getFarmDetails();

    @Query("SELECT * FROM FarmDetails WHERE inventoryOrder = :inventoryOrder AND supplierId = :supplierId")
    FarmDetails getFarmDetails(String inventoryOrder, int supplierId);

    @Query("SELECT * FROM FarmDetails WHERE isForData = 0")
    List<FarmDetails> getFarmDetailsUnSynced();

    @Query("SELECT COUNT(*) AS cnt FROM FarmDetails WHERE inventoryOrder = :inventoryOrder")
    int getInventoryCount(String inventoryOrder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrReplaceFarmDetails(FarmDetails farmDetails);

    @Query("UPDATE FarmDetails SET netVolume = :netVolume, grossVolume = :grossVolume, totalPieces = :pieces WHERE supplierId = :supplierId " +
            "AND inventoryOrder = :inventoryOrder")
    void updateFarmDetails(String inventoryOrder, int supplierId, int pieces, double grossVolume, double netVolume);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceFarmDetails(List<FarmDetails> farmDetails);

    @Query("UPDATE FarmDetails SET farmId = :farmId, isSynced = 1 WHERE tempFarmId = :tempFarmId AND inventoryOrder = :inventoryOrder")
    void updateFarmDetailsSynced(int farmId, String tempFarmId, String inventoryOrder);

    @Query("UPDATE FarmDetails SET farmId = :farmId, isSynced = 1 WHERE inventoryOrder = :inventoryOrder")
    void updateFarmDetailsSynced(int farmId, String inventoryOrder);

    @Query("UPDATE FarmDetails SET isClosed = :isClosed, closedBy = :closedBy, closedDate = :closedDate WHERE inventoryOrder = :inventoryOrder")
    int updateFarmDetailsClosed(boolean isClosed, int closedBy, String closedDate, String inventoryOrder);

    @Query("SELECT COUNT(DISTINCT supplierId) AS supplierCount, SUM(totalPieces) AS totalPieces, SUM(grossVolume) AS grossVolume, COUNT(inventoryOrder) AS totalICA " +
            "FROM FarmDetails WHERE purchaseDate = :todayDate")
    FarmDetailDashboardModel fetchTodayDashboardData(String todayDate);

    @Query("SELECT COUNT(DISTINCT supplierId) AS supplierCount, SUM(totalPieces) AS totalPieces, SUM(grossVolume) AS grossVolume, " +
            "COUNT(inventoryOrder) AS totalICA " +
            "FROM FarmDetails " +
            "WHERE DATE(SUBSTR(purchaseDate, 7, 4) || '-' || SUBSTR(purchaseDate, 4, 2) || '-' || " +
            "SUBSTR(purchaseDate, 1, 2)) BETWEEN DATE(:startDate) AND DATE(:endDate)")
    FarmDetailDashboardModel fetchRecentDashboardData(String startDate, String endDate);

    @Query("UPDATE FarmDetails SET isSynced = 0, inventoryOrder = :inventoryOrder, truckDriverName = :truckDriverName, truckPlateNumber = :truckPlateNumber WHERE inventoryOrder = :existingInventoryOrder AND supplierId = :supplierId")
    int updateFarmDetails(String truckDriverName, String truckPlateNumber, String inventoryOrder, String existingInventoryOrder, int supplierId);
}