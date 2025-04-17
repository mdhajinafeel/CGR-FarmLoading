package com.codringreen.farmloading.model.request;

import java.io.Serializable;
import java.util.List;

public class FarmDetailsRequest implements Serializable {

    private String tempFarmId, inventoryOrder, purchaseDate, truckPlateNumber, truckDriverName, closedDate;
    private int farmId, supplierId, productId, productTypeId, purchaseUnitId, purchaseContractId, totalPieces, createdBy, closedBy, originId;
    private double grossVolume, netVolume, circAllowance, lengthAllowance;
    private boolean isClosed;
    private List<FarmCapturedDataRequest> farmCapturedData;

    public String getTempFarmId() {
        return tempFarmId;
    }

    public void setTempFarmId(String tempFarmId) {
        this.tempFarmId = tempFarmId;
    }

    public String getInventoryOrder() {
        return inventoryOrder;
    }

    public void setInventoryOrder(String inventoryOrder) {
        this.inventoryOrder = inventoryOrder;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getTruckPlateNumber() {
        return truckPlateNumber;
    }

    public void setTruckPlateNumber(String truckPlateNumber) {
        this.truckPlateNumber = truckPlateNumber;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getPurchaseUnitId() {
        return purchaseUnitId;
    }

    public void setPurchaseUnitId(int purchaseUnitId) {
        this.purchaseUnitId = purchaseUnitId;
    }

    public int getPurchaseContractId() {
        return purchaseContractId;
    }

    public void setPurchaseContractId(int purchaseContractId) {
        this.purchaseContractId = purchaseContractId;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(int totalPieces) {
        this.totalPieces = totalPieces;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(int closedBy) {
        this.closedBy = closedBy;
    }

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public double getCircAllowance() {
        return circAllowance;
    }

    public void setCircAllowance(double circAllowance) {
        this.circAllowance = circAllowance;
    }

    public double getLengthAllowance() {
        return lengthAllowance;
    }

    public void setLengthAllowance(double lengthAllowance) {
        this.lengthAllowance = lengthAllowance;
    }

    public double getGrossVolume() {
        return grossVolume;
    }

    public void setGrossVolume(double grossVolume) {
        this.grossVolume = grossVolume;
    }

    public double getNetVolume() {
        return netVolume;
    }

    public void setNetVolume(double netVolume) {
        this.netVolume = netVolume;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<FarmCapturedDataRequest> getFarmCapturedData() {
        return farmCapturedData;
    }

    public void setFarmCapturedData(List<FarmCapturedDataRequest> farmCapturedData) {
        this.farmCapturedData = farmCapturedData;
    }

    public String getTruckDriverName() {
        return truckDriverName;
    }

    public void setTruckDriverName(String truckDriverName) {
        this.truckDriverName = truckDriverName;
    }
}