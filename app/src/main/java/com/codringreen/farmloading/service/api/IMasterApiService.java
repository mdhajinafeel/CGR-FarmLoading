package com.codringreen.farmloading.service.api;

import com.codringreen.farmloading.constants.IAPIConstants;
import com.codringreen.farmloading.model.response.DownloadMaserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface IMasterApiService {

    @GET(IAPIConstants.MASTERDOWNLOAD)
    Call<DownloadMaserResponse> masterDownload(@Header("Content-Type") String content_type);
}