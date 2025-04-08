package com.codringreen.farmloading.model.response;

import java.io.Serializable;
import java.util.List;

public class DownloadMasterDataResponse implements Serializable {

    private List<PurchaseContractMasterResponse> purchaseContract;
    private List<SupplierMasterResponse> suppliers;
    private List<FarmMasterResponse> farmMasters;
    private List<FarmDetailsMasterResponse> farmDetails;
    private UserDataResponse userData;

    public List<PurchaseContractMasterResponse> getPurchaseContract() {
        return purchaseContract;
    }

    public void setPurchaseContract(List<PurchaseContractMasterResponse> purchaseContract) {
        this.purchaseContract = purchaseContract;
    }

    public List<SupplierMasterResponse> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierMasterResponse> suppliers) {
        this.suppliers = suppliers;
    }

    public List<FarmMasterResponse> getFarmMasters() {
        return farmMasters;
    }

    public void setFarmMasters(List<FarmMasterResponse> farmMasters) {
        this.farmMasters = farmMasters;
    }

    public UserDataResponse getUserData() {
        return userData;
    }

    public void setUserData(UserDataResponse userData) {
        this.userData = userData;
    }

    public List<FarmDetailsMasterResponse> getFarmDetails() {
        return farmDetails;
    }

    public void setFarmDetails(List<FarmDetailsMasterResponse> farmDetails) {
        this.farmDetails = farmDetails;
    }
}