package com.codringreen.farmloading.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.codringreen.farmloading.BuildConfig;
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
import com.codringreen.farmloading.helper.PreferenceManager;
import com.codringreen.farmloading.model.response.DownloadMaserResponse;
import com.codringreen.farmloading.model.response.DownloadMasterDataResponse;
import com.codringreen.farmloading.model.response.FarmDataMasterResponse;
import com.codringreen.farmloading.model.response.FarmDetailsMasterResponse;
import com.codringreen.farmloading.model.response.FarmMasterResponse;
import com.codringreen.farmloading.model.response.PurchaseContractMasterResponse;
import com.codringreen.farmloading.model.response.SupplierMasterResponse;
import com.codringreen.farmloading.model.response.SupplierProductTypesMasterResponse;
import com.codringreen.farmloading.model.response.SupplierProductsMasterResponse;
import com.codringreen.farmloading.model.response.UserDataResponse;
import com.codringreen.farmloading.service.api.IMasterApiService;
import com.codringreen.farmloading.service.api.ResponseCallBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterRepository {

    private final IMasterApiService iMasterApiService;
    private final PurchaseContractDao purchaseContractDao;
    private final SuppliersDao suppliersDao;
    private final SupplierProductsDao supplierProductsDao;
    private final SupplierProductTypesDao supplierProductTypesDao;
    private final InventoryNumbersDao inventoryNumbersDao;
    private final FarmDetailsDao farmDetailsDao;
    private final FarmCapturedDataDao farmCapturedDataDao;

    public MasterRepository(IMasterApiService iMasterApiService, PurchaseContractDao purchaseContractDao, SuppliersDao suppliersDao,
                            SupplierProductsDao supplierProductsDao, SupplierProductTypesDao supplierProductTypesDao,
                            InventoryNumbersDao inventoryNumbersDao, FarmDetailsDao farmDetailsDao, FarmCapturedDataDao farmCapturedDataDao) {
        this.iMasterApiService = iMasterApiService;
        this.purchaseContractDao = purchaseContractDao;
        this.suppliersDao = suppliersDao;
        this.supplierProductsDao = supplierProductsDao;
        this.supplierProductTypesDao = supplierProductTypesDao;
        this.inventoryNumbersDao = inventoryNumbersDao;
        this.farmDetailsDao = farmDetailsDao;
        this.farmCapturedDataDao = farmCapturedDataDao;
    }

    public void masterDownload(final ResponseCallBack<DownloadMaserResponse> callBack) {
        try {
            iMasterApiService.masterDownload(BuildConfig.CONTENT_TYPE).enqueue(new Callback<DownloadMaserResponse>() {
                public void onResponse(@NonNull Call<DownloadMaserResponse> call, @NonNull Response<DownloadMaserResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().isStatus()) {
                                if (response.body().getData() != null) {
                                    deleteMasterData();
                                    UserDataResponse userData = response.body().getData().getUserData();
                                    PreferenceManager.INSTANCE.setOriginId(userData.getOriginId());
                                    PreferenceManager.INSTANCE.setUserId(userData.getUserId());
                                    PreferenceManager.INSTANCE.setName(userData.getFullName());
                                    PreferenceManager.INSTANCE.setPhoto(userData.getPhoto());
                                    saveMasterData(response.body().getData());
                                }
                                callBack.onSuccess(response.body());
                                return;
                            }
                            callBack.onError(response.body().getMessage());
                            return;
                        }
                        callBack.onError("ERROR_500");
                        return;
                    }
                    callBack.onError("ERROR_500");
                }

                public void onFailure(@NonNull Call<DownloadMaserResponse> call, @NonNull Throwable t) {
                    callBack.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("MasterRepository", "Error in MasterRepository masterDownload", e);
            callBack.onError(e.getMessage());
        }
    }

    public void deleteMasterData() {
        try {
            purchaseContractDao.deleteAll();
            suppliersDao.deleteAll();
            supplierProductsDao.deleteAll();
            supplierProductTypesDao.deleteAll();
            inventoryNumbersDao.deleteAll();
            farmDetailsDao.deleteAll();
            farmCapturedDataDao.deleteAll();
        } catch (Exception e) {
            Log.e("MasterRepository", "Error in MasterRepository deleteMasterData", e);
        }
    }

    public void saveMasterData(DownloadMasterDataResponse data) {
        insertPurchaseContracts(data.getPurchaseContract());
        insertSuppliers(data.getSuppliers());
        insertInventoryNumbers(data.getFarmMasters());
        insertFarmDetails(data.getFarmDetails());
    }

    private void insertPurchaseContracts(List<PurchaseContractMasterResponse> purchaseContracts) {
        try {
            List<PurchaseContract> purchaseContractList = new ArrayList<>();
            for (PurchaseContractMasterResponse purchaseContractMasterResponse : purchaseContracts) {
                PurchaseContract purchaseContract = new PurchaseContract();
                purchaseContract.setContractId(purchaseContractMasterResponse.getContractId());
                purchaseContract.setContractCode(purchaseContractMasterResponse.getContractCode());
                purchaseContract.setPurchaseUnit(purchaseContractMasterResponse.getPurchaseUnit());
                purchaseContract.setPurchaseUnitId(purchaseContractMasterResponse.getPurchaseUnitId());
                purchaseContract.setCurrency(purchaseContractMasterResponse.getCurrency());
                purchaseContract.setProduct(purchaseContractMasterResponse.getProduct());
                purchaseContract.setProductType(purchaseContractMasterResponse.getProductType());
                purchaseContract.setSupplierId(purchaseContractMasterResponse.getSupplierId());
                purchaseContract.setCircAllowance(purchaseContractMasterResponse.getCircAllowance());
                purchaseContract.setLengthAllowance(purchaseContractMasterResponse.getLengthAllowance());
                purchaseContract.setDescription(purchaseContractMasterResponse.getDescription());
                purchaseContractList.add(purchaseContract);
            }
            purchaseContractDao.insertOrReplacePurchaseContract(purchaseContractList);
        } catch (Exception e) {
            Log.e("MasterRepository", "Error in MasterRepository insertPurchaseContracts", e);
        }
    }

    private void insertSuppliers(List<SupplierMasterResponse> suppliers) {
        try {
            List<Suppliers> suppliersList = new ArrayList<>();
            List<SupplierProducts> supplierProductsList = new ArrayList<>();
            List<SupplierProductTypes> supplierProductTypesList = new ArrayList<>();
            for (SupplierMasterResponse supplierMasterResponse : suppliers) {
                Suppliers supplier = new Suppliers();
                supplier.setSupplierId(supplierMasterResponse.getSupplierId());
                supplier.setSupplierCode(supplierMasterResponse.getSupplierCode());
                supplier.setSupplierName(supplierMasterResponse.getSupplierName());
                suppliersList.add(supplier);
                if (supplierMasterResponse.getSupplierProducts() != null && !supplierMasterResponse.getSupplierProducts().isEmpty()) {
                    for (SupplierProductsMasterResponse supplierProductsMasterResponse : supplierMasterResponse.getSupplierProducts()) {
                        SupplierProducts supplierProducts = new SupplierProducts();
                        supplierProducts.setSupplierId(supplierMasterResponse.getSupplierId());
                        supplierProducts.setSupplierProductId(supplierProductsMasterResponse.getSupplierProductId());
                        supplierProducts.setProductId(supplierProductsMasterResponse.getProductId());
                        supplierProducts.setProductName(supplierProductsMasterResponse.getProductName());
                        supplierProductsList.add(supplierProducts);
                        if (supplierProductsMasterResponse.getSupplierProductTypes() != null && !supplierProductsMasterResponse.getSupplierProductTypes().isEmpty()) {
                            for (SupplierProductTypesMasterResponse supplierProductTypesMasterResponse : supplierProductsMasterResponse.getSupplierProductTypes()) {
                                SupplierProductTypes supplierProductType = new SupplierProductTypes();
                                supplierProductType.setSupplierId(supplierMasterResponse.getSupplierId());
                                supplierProductType.setProductId(supplierProductsMasterResponse.getProductId());
                                supplierProductType.setSupplierProductId(supplierProductsMasterResponse.getSupplierProductId());
                                supplierProductType.setTypeId(supplierProductTypesMasterResponse.getTypeId());
                                supplierProductType.setProductTypeName(supplierProductTypesMasterResponse.getProductTypeName());
                                supplierProductType.setProductTypeId(supplierProductTypesMasterResponse.getProductTypeId());
                                supplierProductTypesList.add(supplierProductType);
                            }
                        }
                    }
                }
            }
            if (!suppliersList.isEmpty()) {
                suppliersDao.insertOrReplaceSuppliers(suppliersList);
                supplierProductsDao.insertOrReplaceSupplierProducts(supplierProductsList);
                supplierProductTypesDao.insertOrReplaceSupplierProductTypes(supplierProductTypesList);
            }
        } catch (Exception e) {
            Log.e("MasterRepository", "Error in MasterRepository insertSuppliers", e);
        }
    }

    private void insertInventoryNumbers(List<FarmMasterResponse> inventoryNumbers) {
        try {
            List<InventoryNumbers> inventoryNumbersList = new ArrayList<>();
            for (FarmMasterResponse farmMasterResponse : inventoryNumbers) {
                InventoryNumbers inventoryNumber = new InventoryNumbers();
                inventoryNumber.setInventoryNumber(farmMasterResponse.getInventoryNumber());
                inventoryNumber.setSupplierId(farmMasterResponse.getSupplierId());
                inventoryNumbersList.add(inventoryNumber);
            }
            inventoryNumbersDao.insertOrReplaceInventoryNumbers(inventoryNumbersList);
        } catch (Exception e) {
            Log.e("MasterRepository", "Error in MasterRepository insertInventoryNumbers", e);
        }
    }

    private void insertFarmDetails(List<FarmDetailsMasterResponse> farmDetailsMasters) {
        try {
            List<FarmDetails> farmDetailsList = new ArrayList<>();
            List<FarmCapturedData> farmCapturedDataList = new ArrayList<>();
            for (FarmDetailsMasterResponse farmDetailsMasterResponse : farmDetailsMasters) {
                FarmDetails farmDetail = new FarmDetails();
                farmDetail.setFarmId(farmDetailsMasterResponse.getFarmId());
                farmDetail.setSupplierId(farmDetailsMasterResponse.getSupplierId());
                farmDetail.setProductId(farmDetailsMasterResponse.getProductId());
                farmDetail.setProductTypeId(farmDetailsMasterResponse.getProductTypeId());
                farmDetail.setInventoryOrder(farmDetailsMasterResponse.getInventoryOrder());
                farmDetail.setPurchaseContractId(farmDetailsMasterResponse.getPurchaseContractId());
                farmDetail.setPurchaseUnitId(farmDetailsMasterResponse.getPurchaseUnitId());
                farmDetail.setPurchaseDate(farmDetailsMasterResponse.getPurchaseDate());
                farmDetail.setTruckPlateNumber(farmDetailsMasterResponse.getTruckPlateNumber());
                farmDetail.setTruckDriverName(farmDetailsMasterResponse.getTruckDriverName());
                farmDetail.setTotalPieces(farmDetailsMasterResponse.getTotalPieces());
                farmDetail.setGrossVolume(farmDetailsMasterResponse.getGrossVolume());
                farmDetail.setNetVolume(farmDetailsMasterResponse.getNetVolume());
                farmDetail.setSupplierName(farmDetailsMasterResponse.getSupplierName());
                farmDetail.setMeasurementSystem(farmDetailsMasterResponse.getMeasurementSystem());
                farmDetail.setProductName(farmDetailsMasterResponse.getProductName());
                farmDetail.setTempFarmId("");
                farmDetail.setCircAllowance(farmDetailsMasterResponse.getCircAllowance());
                farmDetail.setLengthAllowance(farmDetailsMasterResponse.getLengthAllowance());
                farmDetail.setDescription(farmDetailsMasterResponse.getDescription());
                farmDetail.setSynced(true);
                farmDetail.setClosed(false);
                farmDetail.setClosedBy(0);
                farmDetail.setClosedDate("");
                farmDetail.setForData(farmDetailsMasterResponse.isForData());
                farmDetail.setProductTypeName(farmDetailsMasterResponse.getProductTypeName());

                if(farmDetailsMasterResponse.getFarmData() != null && !farmDetailsMasterResponse.getFarmData().isEmpty()) {
                    for (FarmDataMasterResponse farmDataMasterResponse : farmDetailsMasterResponse.getFarmData()) {
                        FarmCapturedData farmCapturedData = new FarmCapturedData();
                        farmCapturedData.setInventoryOrder(farmDetailsMasterResponse.getInventoryOrder());
                        farmCapturedData.setPieces(farmDataMasterResponse.getPieces());
                        farmCapturedData.setCircumference(farmDataMasterResponse.getCircumference());
                        farmCapturedData.setLength(farmDataMasterResponse.getLength());
                        farmCapturedData.setGrossVolume(farmDataMasterResponse.getGrossVolume());
                        farmCapturedData.setNetVolume(farmDataMasterResponse.getNetVolume());
                        farmCapturedData.setCircAllowance(farmDetailsMasterResponse.getCircAllowance());
                        farmCapturedData.setLengthAllowance(farmDetailsMasterResponse.getLengthAllowance());
                        farmCapturedData.setCaptureTimeStamp(0L);
                        farmCapturedData.setFarmDataId(farmDataMasterResponse.getFarmDataId());
                        farmCapturedData.setFarmId(farmDataMasterResponse.getFarmId());
                        farmCapturedData.setIsSynced(true);

                        farmCapturedDataList.add(farmCapturedData);
                    }
                }

                farmDetailsList.add(farmDetail);
            }
            farmDetailsDao.insertOrReplaceFarmDetails(farmDetailsList);
            farmCapturedDataDao.insertOrReplaceFarmCapturedData(farmCapturedDataList);
        } catch (Exception e) {
            Log.e("MasterRepository", "Error in MasterRepository insertFarmDetails", e);
        }
    }

    public List<Suppliers> getSuppliers() {
        return suppliersDao.getSuppliers();
    }

    public List<SupplierProducts> getSuppliersProductsListsById(int sid) {
        return supplierProductsDao.getSuppliersProductsListsById(sid);
    }

    public List<SupplierProductTypes> getSuppliersProductsTypeListsById(int sid, int productId) {
        return supplierProductTypesDao.getSuppliersProductTypesListsById(productId, sid);
    }

    public List<PurchaseContract> getPurchaseContractLists(int supplierId, int productId, List<Integer> productTypes) {
        return purchaseContractDao.getPurchaseContractLists(supplierId, productId, productTypes);
    }
}