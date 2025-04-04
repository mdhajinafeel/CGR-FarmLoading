package com.codringreen.farmloading.repository;

import androidx.annotation.NonNull;

import com.codringreen.farmloading.model.request.LoginRequest;
import com.codringreen.farmloading.model.response.LoginResponse;
import com.codringreen.farmloading.service.api.IAuthApiService;
import com.codringreen.farmloading.service.api.ResponseCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private final IAuthApiService iAuthApiService;

    public LoginRepository(IAuthApiService iAuthApiService) {
        this.iAuthApiService = iAuthApiService;
    }

    public void postLogin(LoginRequest loginRequest, final ResponseCallBack<LoginResponse> callBack) {
        try {
            this.iAuthApiService.postLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        callBack.onSuccess(response.body());
                    } else {
                        callBack.onError("ERROR_500");
                    }
                }

                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    callBack.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            callBack.onError(e.getMessage());
        }
    }
}