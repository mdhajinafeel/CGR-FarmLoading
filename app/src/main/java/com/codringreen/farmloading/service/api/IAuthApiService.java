package com.codringreen.farmloading.service.api;

import com.codringreen.farmloading.constants.IAPIConstants;
import com.codringreen.farmloading.model.request.LoginRequest;
import com.codringreen.farmloading.model.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthApiService {

    @POST(IAPIConstants.LOGIN)
    Call<LoginResponse> postLogin(@Body LoginRequest loginRequest);
}