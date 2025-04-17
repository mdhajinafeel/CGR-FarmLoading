package com.codringreen.farmloading.model;

import java.io.Serializable;

public class FarmDetailDashboardModel implements Serializable {

    private int supplierCount, totalICA, totalPieces;
    private double grossVolume;

    public int getSupplierCount() {
        return supplierCount;
    }

    public void setSupplierCount(int supplierCount) {
        this.supplierCount = supplierCount;
    }

    public int getTotalICA() {
        return totalICA;
    }

    public void setTotalICA(int totalICA) {
        this.totalICA = totalICA;
    }

    public double getGrossVolume() {
        return grossVolume;
    }

    public void setGrossVolume(double grossVolume) {
        this.grossVolume = grossVolume;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(int totalPieces) {
        this.totalPieces = totalPieces;
    }
}