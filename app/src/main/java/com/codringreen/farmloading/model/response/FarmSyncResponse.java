package com.codringreen.farmloading.model.response;

import java.io.Serializable;
import java.util.List;

public class FarmSyncResponse implements Serializable {

    private boolean status;
    private String message;
    private List<FarmDataSyncResponse> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FarmDataSyncResponse> getData() {
        return data;
    }

    public void setData(List<FarmDataSyncResponse> data) {
        this.data = data;
    }
}