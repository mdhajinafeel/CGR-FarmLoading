package com.codringreen.farmloading.model.request;

import java.io.Serializable;

public class FarmCapturedDataRequest implements Serializable {

    private int pieces, farmDataId;
    private double circumference, length, grossVolume,  netVolume;
    private Long capturedTimeStamp;

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
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

    public int getFarmDataId() {
        return farmDataId;
    }

    public void setFarmDataId(int farmDataId) {
        this.farmDataId = farmDataId;
    }

    public Long getCapturedTimeStamp() {
        return capturedTimeStamp;
    }

    public void setCapturedTimeStamp(Long capturedTimeStamp) {
        this.capturedTimeStamp = capturedTimeStamp;
    }
}