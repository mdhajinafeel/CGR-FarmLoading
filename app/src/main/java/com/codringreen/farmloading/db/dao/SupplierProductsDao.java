package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.SupplierProducts;

import java.util.List;

@Dao
public interface SupplierProductsDao {

    @Query("DELETE FROM SupplierProducts")
    void deleteAll();

    @Query("SELECT * FROM SupplierProducts WHERE supplierId = :sid")
    List<SupplierProducts> getSuppliersProductsListsById(int sid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceSupplierProducts(List<SupplierProducts> supplierProducts);
}