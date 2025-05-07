package com.codringreen.farmloading.model.response;

import java.io.Serializable;
import java.util.List;

public class FarmDetailsMasterResponse implements Serializable {

    private int farmId, supplierId, productId, productTypeId, purchaseContractId, purchaseUnitId, totalPieces;
    private String inventoryOrder, purchaseDate, truckPlateNumber, truckDriverName, supplierName, measurementSystem, productName, description, productTypeName;
    private double grossVolume, netVolume, circAllowance, lengthAllowance;
    private boolean isForData;
    private List<FarmDataMasterResponse> farmData;

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

    public int getPurchaseContractId() {
        return purchaseContractId;
    }

    public void setPurchaseContractId(int purchaseContractId) {
        this.purchaseContractId = purchaseContractId;
    }

    public int getPurchaseUnitId() {
        return purchaseUnitId;
    }

    public void setPurchaseUnitId(int purchaseUnitId) {
        this.purchaseUnitId = purchaseUnitId;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(int totalPieces) {
        this.totalPieces = totalPieces;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMeasurementSystem() {
        return measurementSystem;
    }

    public void setMeasurementSystem(String measurementSystem) {
        this.measurementSystem = measurementSystem;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isForData() {
        return isForData;
    }

    public void setForData(boolean forData) {
        isForData = forData;
    }

    public List<FarmDataMasterResponse> getFarmData() {
        return farmData;
    }

    public void setFarmData(List<FarmDataMasterResponse> farmData) {
        this.farmData = farmData;
    }

    public String getTruckDriverName() {
        return truckDriverName;
    }

    public void setTruckDriverName(String truckDriverName) {
        this.truckDriverName = truckDriverName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}