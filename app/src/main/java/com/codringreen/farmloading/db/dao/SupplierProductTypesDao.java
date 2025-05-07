package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.SupplierProductTypes;

import java.util.List;

@Dao
public interface SupplierProductTypesDao {

    @Query("DELETE FROM SupplierProductTypes")
    void deleteAll();

    @Query("SELECT * FROM SupplierProductTypes WHERE productId = :productId AND supplierId = :sid AND productTypeId IN (2,4)")
    List<SupplierProductTypes> getSuppliersProductTypesListsById(int productId, int sid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceSupplierProductTypes(List<SupplierProductTypes> supplierProductTypes);
}