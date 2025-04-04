package com.codringreen.farmloading.model.response;

import java.io.Serializable;
import java.util.List;

public class SupplierProductsMasterResponse implements Serializable {

    private int productId;
    private String productName;
    private int supplierId;
    private int supplierProductId;
    private List<SupplierProductTypesMasterResponse> supplierProductTypes;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(int supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public List<SupplierProductTypesMasterResponse> getSupplierProductTypes() {
        return supplierProductTypes;
    }

    public void setSupplierProductTypes(List<SupplierProductTypesMasterResponse> supplierProductTypes) {
        this.supplierProductTypes = supplierProductTypes;
    }
}