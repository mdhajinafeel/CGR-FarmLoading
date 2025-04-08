package com.codringreen.farmloading.model.request;

import java.io.Serializable;
import java.util.List;

public class FarmSyncRequest implements Serializable {

    private List<FarmDetailsRequest> farmData;

    public List<FarmDetailsRequest> getFarmData() {
        return farmData;
    }

    public void setFarmData(List<FarmDetailsRequest> farmData) {
        this.farmData = farmData;
    }
}