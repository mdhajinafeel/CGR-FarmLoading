package com.codringreen.farmloading.model.response;

import java.io.Serializable;
import java.util.List;

public class DownloadMasterDataResponse implements Serializable {

    private List<PurchaseContractMasterResponse> purchaseContract;
    private List<SupplierMasterResponse> suppliers;
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

    public UserDataResponse getUserData() {
        return userData;
    }

    public void setUserData(UserDataResponse userData) {
        this.userData = userData;
    }
}