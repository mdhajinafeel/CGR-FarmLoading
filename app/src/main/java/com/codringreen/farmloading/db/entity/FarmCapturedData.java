package com.codringreen.farmloading.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "FarmCapturedData")
public class FarmCapturedData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String inventoryOrder;
    private int pieces;
    private double circumference;
    private double length;
    private double grossVolume;
    private double netVolume;
    private double circAllowance;
    private double lengthAllowance;
    private String captureTimeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInventoryOrder() {
        return inventoryOrder;
    }

    public void setInventoryOrder(String inventoryOrder) {
        this.inventoryOrder = inventoryOrder;
    }

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

    public String getCaptureTimeStamp() {
        return captureTimeStamp;
    }

    public void setCaptureTimeStamp(String captureTimeStamp) {
        this.captureTimeStamp = captureTimeStamp;
    }
}