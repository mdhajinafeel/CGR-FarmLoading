package com.codringreen.farmloading.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "PurchaseContract")
public class PurchaseContract implements Serializable {

    private String contractCode;
    private int contractId;
    private String currency;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean isSelected;
    private int product;
    private int productType;
    private String purchaseUnit;
    private int purchaseUnitId;
    private int supplierId;
    private double circAllowance;
    private double lengthAllowance;
    private String description;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public int getPurchaseUnitId() {
        return purchaseUnitId;
    }

    public void setPurchaseUnitId(int purchaseUnitId) {
        this.purchaseUnitId = purchaseUnitId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}