package com.codringreen.farmloading.di.modules;

import android.app.Application;

import androidx.room.Room;

import com.codringreen.farmloading.db.AppExecutors;
import com.codringreen.farmloading.db.CGRFarmDatabase;
import com.codringreen.farmloading.db.dao.FarmCapturedDataDao;
import com.codringreen.farmloading.db.dao.FarmDetailsDao;
import com.codringreen.farmloading.db.dao.InventoryNumbersDao;
import com.codringreen.farmloading.db.dao.PurchaseContractDao;
import com.codringreen.farmloading.db.dao.SupplierProductTypesDao;
import com.codringreen.farmloading.db.dao.SupplierProductsDao;
import com.codringreen.farmloading.db.dao.SuppliersDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DBModule {

    @Provides
    @Singleton
    CGRFarmDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, CGRFarmDatabase.class, "cgr_farmloadingv1.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }

    @Provides
    @Singleton
    SuppliersDao provideSuppliersDao(CGRFarmDatabase database) {
        return database.suppliersDao();
    }

    @Provides
    @Singleton
    SupplierProductsDao provideSupplierProductsDao(CGRFarmDatabase database) {
        return database.supplierProductsDao();
    }

    @Provides
    @Singleton
    SupplierProductTypesDao provideSupplierProductTypesDao(CGRFarmDatabase database) {
        return database.supplierProductTypesDao();
    }

    @Provides
    @Singleton
    PurchaseContractDao providePurchaseContractDao(CGRFarmDatabase database) {
        return database.purchaseContractDao();
    }

    @Provides
    @Singleton
    FarmDetailsDao provideFarmDetailsDao(CGRFarmDatabase database) {
        return database.farmDetailsDao();
    }

    @Provides
    @Singleton
    FarmCapturedDataDao provideFarmCapturedDataDao(CGRFarmDatabase database) {
        return database.farmCapturedDataDao();
    }

    @Provides
    @Singleton
    InventoryNumbersDao provideInventoryNumbersDao(CGRFarmDatabase database) {
        return database.inventoryNumbersDao();
    }
}
