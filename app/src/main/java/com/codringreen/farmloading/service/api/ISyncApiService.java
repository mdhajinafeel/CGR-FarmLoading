package com.codringreen.farmloading.service.api;

import com.codringreen.farmloading.constants.IAPIConstants;
import com.codringreen.farmloading.model.request.FarmSyncRequest;
import com.codringreen.farmloading.model.response.FarmSyncResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ISyncApiService {

    @POST(IAPIConstants.SYNC_FARM_DATA)
    Call<FarmSyncResponse> syncFarmData(@Header("Content-Type") String content_type, @Body FarmSyncRequest farmSyncRequest);
}