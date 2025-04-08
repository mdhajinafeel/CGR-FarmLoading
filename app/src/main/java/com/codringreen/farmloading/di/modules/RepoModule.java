package com.codringreen.farmloading.di.modules;

import com.codringreen.farmloading.db.dao.FarmCapturedDataDao;
import com.codringreen.farmloading.db.dao.FarmDetailsDao;
import com.codringreen.farmloading.db.dao.InventoryNumbersDao;
import com.codringreen.farmloading.db.dao.PurchaseContractDao;
import com.codringreen.farmloading.db.dao.SupplierProductTypesDao;
import com.codringreen.farmloading.db.dao.SupplierProductsDao;
import com.codringreen.farmloading.db.dao.SuppliersDao;
import com.codringreen.farmloading.repository.FarmRepository;
import com.codringreen.farmloading.repository.LoginRepository;
import com.codringreen.farmloading.repository.MasterRepository;
import com.codringreen.farmloading.repository.SyncRepository;
import com.codringreen.farmloading.service.api.IAuthApiService;
import com.codringreen.farmloading.service.api.IMasterApiService;
import com.codringreen.farmloading.service.api.ISyncApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiModule.class, DBModule.class})
public class RepoModule {

    @Provides
    @Singleton
    LoginRepository provideLoginRepository(IAuthApiService iAuthApiService) {
        return new LoginRepository(iAuthApiService);
    }

    @Provides
    @Singleton
    MasterRepository provideMasterRepository(IMasterApiService iMasterApiService, PurchaseContractDao purchaseContractDao, SuppliersDao suppliersDao,
                                             SupplierProductsDao supplierProductsDao, SupplierProductTypesDao supplierProductTypesDao,
                                             InventoryNumbersDao inventoryNumbersDao, FarmDetailsDao farmDetailsDao, FarmCapturedDataDao farmCapturedDataDao) {
        return new MasterRepository(iMasterApiService, purchaseContractDao, suppliersDao, supplierProductsDao, supplierProductTypesDao, inventoryNumbersDao,
                farmDetailsDao, farmCapturedDataDao);
    }

    @Provides
    @Singleton
    FarmRepository provideFarmRepository(FarmDetailsDao farmDetailsDao, FarmCapturedDataDao farmCapturedDataDao, InventoryNumbersDao inventoryNumbersDao) {
        return new FarmRepository(farmDetailsDao, farmCapturedDataDao, inventoryNumbersDao);
    }

    @Provides
    @Singleton
    SyncRepository provideSyncRepository(ISyncApiService iSyncApiService, FarmDetailsDao farmDetailsDao, FarmCapturedDataDao farmCapturedDataDao) {
        return new SyncRepository(iSyncApiService, farmDetailsDao, farmCapturedDataDao);
    }
}