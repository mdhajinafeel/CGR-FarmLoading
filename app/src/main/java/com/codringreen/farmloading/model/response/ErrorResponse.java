package com.codringreen.farmloading.model.response;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    private Long errorCode;
    private String errorMessage;

    public ErrorResponse() {
        // Default constructor
    }

    public ErrorResponse(Long errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}