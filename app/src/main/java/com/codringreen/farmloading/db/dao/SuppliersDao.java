package com.codringreen.farmloading.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.codringreen.farmloading.db.entity.Suppliers;

import java.util.List;

@Dao
public interface SuppliersDao {

    @Query("DELETE FROM Suppliers")
    void deleteAll();

    @Query("SELECT * FROM Suppliers")
    List<Suppliers> getSuppliers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceSuppliers(List<Suppliers> suppliers);
}