package com.codringreen.farmloading.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codringreen.farmloading.db.entity.FarmCapturedData;
import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.db.entity.InventoryNumbers;
import com.codringreen.farmloading.db.entity.PurchaseContract;
import com.codringreen.farmloading.db.entity.SupplierProductTypes;
import com.codringreen.farmloading.db.entity.SupplierProducts;
import com.codringreen.farmloading.db.entity.Suppliers;
import com.codringreen.farmloading.repository.FarmRepository;
import com.codringreen.farmloading.repository.MasterRepository;

import java.util.List;

import javax.inject.Inject;

public class FarmViewModel extends BaseViewModel{

    private String errorTitle;
    private final MasterRepository masterRepository;
    private final FarmRepository farmRepository;
    public Suppliers selectedSupplier;
    public SupplierProducts selectedSuppliersProducts;
    public SupplierProductTypes selectedSuppliersProductTypes;
    public PurchaseContract selectedPurchaseContract;
    private final MutableLiveData<Boolean> progressState = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> farmCreateStatus = new MutableLiveData<>();

    @Inject
    public FarmViewModel(MasterRepository masterRepository, FarmRepository farmRepository) {
        this.masterRepository = masterRepository;
        this.farmRepository = farmRepository;
    }

    public List<Suppliers> fetchSupplierList() {
        return masterRepository.getSuppliers();
    }

    public List<SupplierProducts> fetchSupplierProducts(int sid) {
        return this.masterRepository.getSuppliersProductsListsById(sid);
    }

    public List<SupplierProductTypes> fetchSuppliersProductsType(int sid, int productId) {
        return this.masterRepository.getSuppliersProductsTypeListsById(sid, productId);
    }

    public List<PurchaseContract> fetchPurchaseContractList(int supplierId, int productId, List<Integer> productTypes) {
        return this.masterRepository.getPurchaseContractLists(supplierId, productId, productTypes);
    }

    public void setSelectedSupplier(List<Suppliers> suppliersList, List<Suppliers> suppliersFilteredList, Suppliers supplier, int position) {
        selectedSupplier = supplier;
        if (!suppliersFilteredList.isEmpty()) {
            for (Suppliers suppliers : suppliersList) {
                suppliers.setSelected(false);
            }
            int i = 0;
            while (i < suppliersFilteredList.size()) {
                suppliersFilteredList.get(i).setSelected(i == position);
                i++;
            }
            return;
        }
        int i2 = 0;
        while (i2 < suppliersList.size()) {
            suppliersList.get(i2).setSelected(i2 == position);
            i2++;
        }
    }

    public void setSelectedSupplierProducts(List<SupplierProducts> suppliersProductsList, List<SupplierProducts> suppliersProductsFilteredList, SupplierProducts suppliersProduct, int position) {
        this.selectedSuppliersProducts = suppliersProduct;
        if (!suppliersProductsFilteredList.isEmpty()) {
            for (SupplierProducts supplierProducts : suppliersProductsList) {
                supplierProducts.setSelected(false);
            }
            int i = 0;
            while (i < suppliersProductsFilteredList.size()) {
                suppliersProductsFilteredList.get(i).setSelected(i == position);
                i++;
            }
            return;
        }
        int i2 = 0;
        while (i2 < suppliersProductsList.size()) {
            suppliersProductsList.get(i2).setSelected(i2 == position);
            i2++;
        }
    }

    public void setSelectedSuppliersProductTypes(List<SupplierProductTypes> suppliersProductTypesList, List<SupplierProductTypes> suppliersProductTypesFilteredList, SupplierProductTypes suppliersProductTypes, int position) {
        this.selectedSuppliersProductTypes = suppliersProductTypes;
        if (!suppliersProductTypesFilteredList.isEmpty()) {
            for (SupplierProductTypes supplierProductTypes : suppliersProductTypesList) {
                supplierProductTypes.setSelected(false);
            }
            int i = 0;
            while (i < suppliersProductTypesFilteredList.size()) {
                suppliersProductTypesFilteredList.get(i).setSelected(i == position);
                i++;
            }
            return;
        }
        int i2 = 0;
        while (i2 < suppliersProductTypesList.size()) {
            suppliersProductTypesList.get(i2).setSelected(i2 == position);
            i2++;
        }
    }

    public void setSelectedPurchaseContract(List<PurchaseContract> purchaseContractList, List<PurchaseContract> purchaseContractFilteredList, PurchaseContract purchaseContract, int position) {
        this.selectedPurchaseContract = purchaseContract;
        if (!purchaseContractFilteredList.isEmpty()) {
            for (PurchaseContract contract : purchaseContractList) {
                contract.setSelected(false);
            }
            int i = 0;
            while (i < purchaseContractFilteredList.size()) {
                purchaseContractFilteredList.get(i).setSelected(i == position);
                i++;
            }
            return;
        }
        int i2 = 0;
        while (i2 < purchaseContractList.size()) {
            purchaseContractList.get(i2).setSelected(i2 == position);
            i2++;
        }
    }

    public long saveFarmDetails(FarmDetails farmDetails) {
        return farmRepository.saveFarmDetails(farmDetails);
    }

    public List<FarmDetails> fetchFarmDetails() {
        return farmRepository.fetchFarmLists();
    }

    public FarmDetails fetchFarmDetails(String inventoryOrder, int supplierId) {
        return farmRepository.getFarmDetails(inventoryOrder, supplierId);
    }

    public void saveOrUpdateFarmData(FarmCapturedData farmCapturedData, String inventoryOrder, int supplierId,
                                     int totalPieces, double totalGrossVolume, double totalNetVolume) {
        farmRepository.saveFarmCapturedData(farmCapturedData, inventoryOrder, supplierId, totalPieces, totalGrossVolume, totalNetVolume);
    }

    public void updateFarmCapturedData(FarmDetails farmDetails, FarmCapturedData farmCapturedData) {
        farmRepository.updateFarmCapturedData(farmDetails, farmCapturedData.getInventoryOrder(), farmCapturedData.getPieces(), farmCapturedData.getLength(), farmCapturedData.getCircumference(),
                farmCapturedData.getGrossVolume(), farmCapturedData.getNetVolume(), farmCapturedData.getCaptureTimeStamp(), farmCapturedData.getFarmDataId());
    }

    public List<FarmCapturedData> getFarmCapturedData(String inventoryOrder) {
        return farmRepository.getFarmCapturedData(inventoryOrder);
    }

    public int deleteFarmCapturedData(FarmDetails farmDetails, FarmCapturedData farmCapturedData) {
        return farmRepository.deleteFarmCapturedData(farmDetails, farmCapturedData.getInventoryOrder(), farmCapturedData.getPieces(), farmCapturedData.getLength(),
                farmCapturedData.getCircumference(), farmCapturedData.getGrossVolume(), farmCapturedData.getNetVolume(), farmCapturedData.getCaptureTimeStamp());
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public LiveData<String> getError() {
        return errorMessage;
    }

    public LiveData<Boolean> getProgressBar() {
        return progressState;
    }

    public LiveData<Boolean> getFarmCreateStatus() {
        return farmCreateStatus;
    }

    public int getInventoryCount(String inventoryNumber, int supplierId) {
        return farmRepository.getInventoryCount(inventoryNumber, supplierId);
    }

    public void saveInventoryNumbers(InventoryNumbers inventoryNumbers) {
        farmRepository.saveInventoryNumbers(inventoryNumbers);
    }

    public int updateFarmDetailsClosed(boolean isClosed, int closedBy, String closedDate, String inventoryOrder) {
        return farmRepository.updateFarmDetailsClosed(isClosed, closedBy, closedDate, inventoryOrder);
    }

    public int updateFarmDetails(String truckDriverName, String truckPlateNumber, String inventoryOrder, String existingInventoryOrder, int supplierId) {
        return farmRepository.updateFarmDetails(truckDriverName, truckPlateNumber, inventoryOrder, existingInventoryOrder, supplierId);
    }
}