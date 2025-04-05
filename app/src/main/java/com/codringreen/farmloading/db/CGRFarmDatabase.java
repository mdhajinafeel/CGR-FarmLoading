package com.codringreen.farmloading.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codringreen.farmloading.db.dao.FarmCapturedDataDao;
import com.codringreen.farmloading.db.dao.FarmDetailsDao;
import com.codringreen.farmloading.db.dao.InventoryNumbersDao;
import com.codringreen.farmloading.db.dao.PurchaseContractDao;
import com.codringreen.farmloading.db.dao.SupplierProductTypesDao;
import com.codringreen.farmloading.db.dao.SupplierProductsDao;
import com.codringreen.farmloading.db.dao.SuppliersDao;
import com.codringreen.farmloading.db.entity.FarmCapturedData;
import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.db.entity.InventoryNumbers;
import com.codringreen.farmloading.db.entity.PurchaseContract;
import com.codringreen.farmloading.db.entity.SupplierProductTypes;
import com.codringreen.farmloading.db.entity.SupplierProducts;
import com.codringreen.farmloading.db.entity.Suppliers;

@Database(entities = {PurchaseContract.class, Suppliers.class, SupplierProducts.class,
        SupplierProductTypes.class, FarmDetails.class, FarmCapturedData.class, InventoryNumbers.class},
        version = 1, exportSchema = false)
public abstract class CGRFarmDatabase extends RoomDatabase {

    public abstract PurchaseContractDao purchaseContractDao();

    public abstract SuppliersDao suppliersDao();

    public abstract SupplierProductsDao supplierProductsDao();

    public abstract SupplierProductTypesDao supplierProductTypesDao();

    public abstract FarmDetailsDao farmDetailsDao();

    public abstract FarmCapturedDataDao farmCapturedDataDao();

    public abstract InventoryNumbersDao inventoryNumbersDao();
}