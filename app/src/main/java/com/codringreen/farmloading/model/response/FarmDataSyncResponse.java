package com.codringreen.farmloading.model.response;

import java.io.Serializable;

public class FarmDataSyncResponse implements Serializable {

    private int farmId;
    private String inventoryOrder, tempFarmId;
    private boolean syncStatus;

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public String getInventoryOrder() {
        return inventoryOrder;
    }

    public void setInventoryOrder(String inventoryOrder) {
        this.inventoryOrder = inventoryOrder;
    }

    public String getTempFarmId() {
        return tempFarmId;
    }

    public void setTempFarmId(String tempFarmId) {
        this.tempFarmId = tempFarmId;
    }

    public boolean isSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(boolean syncStatus) {
        this.syncStatus = syncStatus;
    }
}