package com.codringreen.farmloading.model.response;

import java.io.Serializable;
import java.util.List;

public class SupplierMasterResponse implements Serializable {

    private String supplierCode;
    private int supplierId;
    private String supplierName;
    private List<SupplierProductsMasterResponse> supplierProducts;

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<SupplierProductsMasterResponse> getSupplierProducts() {
        return supplierProducts;
    }

    public void setSupplierProducts(List<SupplierProductsMasterResponse> supplierProducts) {
        this.supplierProducts = supplierProducts;
    }
}