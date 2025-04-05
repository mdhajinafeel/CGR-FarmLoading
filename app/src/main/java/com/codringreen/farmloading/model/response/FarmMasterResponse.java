package com.codringreen.farmloading.model.response;

import java.io.Serializable;

public class FarmMasterResponse implements Serializable {

    private String inventoryNumber;
    private int supplierId;

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
}