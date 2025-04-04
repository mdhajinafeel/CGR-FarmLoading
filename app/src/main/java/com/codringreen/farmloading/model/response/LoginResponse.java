package com.codringreen.farmloading.model.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private LoginDataResponse data;
    private boolean status;

    public LoginDataResponse getData() {
        return data;
    }

    public void setData(LoginDataResponse data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}