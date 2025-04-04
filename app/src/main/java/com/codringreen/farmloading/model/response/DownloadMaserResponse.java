package com.codringreen.farmloading.model.response;

import java.io.Serializable;

public class DownloadMaserResponse implements Serializable {

    private DownloadMasterDataResponse data;
    private String message;
    private boolean status;

    public DownloadMasterDataResponse getData() {
        return data;
    }

    public void setData(DownloadMasterDataResponse data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}