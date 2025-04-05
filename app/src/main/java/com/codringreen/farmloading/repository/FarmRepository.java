package com.codringreen.farmloading.repository;

import com.codringreen.farmloading.db.dao.FarmCapturedDataDao;
import com.codringreen.farmloading.db.dao.FarmDetailsDao;
import com.codringreen.farmloading.db.dao.InventoryNumbersDao;
import com.codringreen.farmloading.db.entity.FarmCapturedData;
import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.db.entity.InventoryNumbers;

import java.util.List;

public class FarmRepository {

    private final FarmDetailsDao farmDetailsDao;
    private final FarmCapturedDataDao farmCapturedDataDao;
    private final InventoryNumbersDao inventoryNumbersDao;

    public FarmRepository(FarmDetailsDao farmDetailsDao, FarmCapturedDataDao farmCapturedDataDao, InventoryNumbersDao inventoryNumbersDao) {
        this.farmDetailsDao = farmDetailsDao;
        this.farmCapturedDataDao = farmCapturedDataDao;
        this.inventoryNumbersDao = inventoryNumbersDao;
    }

    public long saveFarmDetails(FarmDetails farmDetails) {
        return farmDetailsDao.insertOrReplaceFarmDetails(farmDetails);
    }

    public List<FarmDetails> fetchFarmLists() {
        return farmDetailsDao.getFarmDetails();
    }

    public FarmDetails getFarmDetails(String inventoryOrder, int supplierId) {
        return farmDetailsDao.getFarmDetails(inventoryOrder, supplierId);
    }

    public void saveFarmCapturedData(FarmCapturedData farmCapturedData, String inventoryOrder, int supplierId,
                                  int totalPieces, double totalGrossVolume, double totalNetVolume) {

        //DELETE
        farmCapturedDataDao.insertOrReplaceFarmCapturedData(farmCapturedData);

        farmDetailsDao.updateFarmDetails(inventoryOrder, supplierId, totalPieces, totalGrossVolume, totalNetVolume);
    }

    public List<FarmCapturedData> getFarmCapturedData(String inventoryOrder) {
        return farmCapturedDataDao.getFarmCapturedData(inventoryOrder);
    }

    public void updateFarmCapturedData(FarmDetails farmDetails, String inventoryOrder, int pieces, double length, double circumference, double grossVolume,
                                       double netVolume, String captureTimeStamp) {

        farmCapturedDataDao.updateFarmCapturedData(inventoryOrder, pieces, length, circumference, grossVolume, netVolume, captureTimeStamp);

        List<FarmCapturedData> farmCapturedDataList = farmCapturedDataDao.getFarmCapturedData(inventoryOrder);
        int totalPieces = 0;
        double totalGrossVolume = 0;
        double totalNetVolume = 0;
        for (FarmCapturedData farmCapturedData : farmCapturedDataList) {
            totalPieces = totalPieces + farmCapturedData.getPieces();
            totalGrossVolume = totalGrossVolume + farmCapturedData.getGrossVolume();
            totalNetVolume = totalNetVolume + farmCapturedData.getNetVolume();
        }

        farmDetailsDao.updateFarmDetails(farmDetails.getInventoryOrder(), farmDetails.getSupplierId(), totalPieces, totalGrossVolume, totalNetVolume);
    }

    public int deleteFarmCapturedData(FarmDetails farmDetails, String inventoryOrder, int pieces, double length, double circumference, double grossVolume,
                                      double netVolume, String captureTimeStamp) {
        int i = farmCapturedDataDao.deleteFarmCapturedData(inventoryOrder, pieces, circumference, length, grossVolume, netVolume, captureTimeStamp);

        if(i > 0 ) {
            List<FarmCapturedData> farmCapturedDataList = farmCapturedDataDao.getFarmCapturedData(inventoryOrder);
            int totalPieces = 0;
            double totalGrossVolume = 0;
            double totalNetVolume = 0;
            for (FarmCapturedData farmCapturedData : farmCapturedDataList) {
                totalPieces = totalPieces + farmCapturedData.getPieces();
                totalGrossVolume = totalGrossVolume + farmCapturedData.getGrossVolume();
                totalNetVolume = totalNetVolume + farmCapturedData.getNetVolume();
            }

            farmDetailsDao.updateFarmDetails(farmDetails.getInventoryOrder(), farmDetails.getSupplierId(), totalPieces, totalGrossVolume, totalNetVolume);
        }
        return i;
    }

    public int getInventoryCount(String inventoryNumber, int supplierId) {
        return inventoryNumbersDao.getInventoryCount(inventoryNumber, supplierId);
    }

    public void saveInventoryNumbers(InventoryNumbers inventoryNumber) {
        inventoryNumbersDao.insertOrReplaceInventoryNumbers(inventoryNumber);
    }
}