package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.PurchaseContract;

import java.util.List;

@Dao
public interface PurchaseContractDao {

    @Query("DELETE FROM PurchaseContract")
    void deleteAll();

    @Query("SELECT * FROM PurchaseContract WHERE supplierId = :supplierId AND product = :productId AND productType IN (:productTypes)")
    List<PurchaseContract> getPurchaseContractLists(int supplierId, int productId, List<Integer> productTypes);

    @Query("SELECT * FROM PurchaseContract")
    List<PurchaseContract> getPurchaseContracts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplacePurchaseContract(List<PurchaseContract> purchaseContracts);
}