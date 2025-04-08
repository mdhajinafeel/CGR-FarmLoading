package com.codringreen.farmloading.model.response;

import java.io.Serializable;

public class FarmDataMasterResponse implements Serializable {

    private int pieces, farmDataId, farmId;
    private double circumference, length, grossVolume, netVolume;

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public int getFarmDataId() {
        return farmDataId;
    }

    public void setFarmDataId(int farmDataId) {
        this.farmDataId = farmDataId;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public double getCircumference() {
        return circumference;
    }

    public void setCircumference(double circumference) {
        this.circumference = circumference;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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
}