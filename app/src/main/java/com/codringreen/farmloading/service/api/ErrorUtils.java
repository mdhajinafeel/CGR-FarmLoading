package com.codringreen.farmloading.service.api;

import com.codringreen.farmloading.model.response.ErrorResponse;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

public class ErrorUtils {

    public static ErrorResponse getErrorResponse(Response response) {
        ErrorResponse errorResponse = new ErrorResponse();
        try {
            return (ErrorResponse) new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
        } catch (IOException e) {
            System.err.println("Error parsing response: " + e.getMessage());
            return errorResponse;
        }
    }
}