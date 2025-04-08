package com.codringreen.farmloading.repository;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codringreen.farmloading.BuildConfig;
import com.codringreen.farmloading.db.dao.FarmCapturedDataDao;
import com.codringreen.farmloading.db.dao.FarmDetailsDao;
import com.codringreen.farmloading.model.request.FarmSyncRequest;
import com.codringreen.farmloading.model.response.FarmDataSyncResponse;
import com.codringreen.farmloading.model.response.FarmSyncResponse;
import com.codringreen.farmloading.service.api.ISyncApiService;
import com.codringreen.farmloading.service.api.ResponseCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncRepository {

    private final ISyncApiService iSyncApiService;
    private final FarmDetailsDao farmDetailsDao;
    private final FarmCapturedDataDao farmCapturedDataDao;

    public SyncRepository(ISyncApiService iSyncApiService, FarmDetailsDao farmDetailsDao, FarmCapturedDataDao farmCapturedDataDao) {
        this.iSyncApiService = iSyncApiService;
        this.farmDetailsDao = farmDetailsDao;
        this.farmCapturedDataDao = farmCapturedDataDao;
    }

    public void syncFarmData(FarmSyncRequest farmSyncRequest, ResponseCallBack<FarmSyncResponse> callBack) {
        try {
            iSyncApiService.syncFarmData(BuildConfig.CONTENT_TYPE, farmSyncRequest).enqueue(new Callback<FarmSyncResponse>() {
                @Override
                public void onResponse(@NonNull Call<FarmSyncResponse> call, @NonNull Response<FarmSyncResponse> farmSyncResponseResponse) {
                    if (farmSyncResponseResponse.isSuccessful()) {
                        if (farmSyncResponseResponse.body() != null && farmSyncResponseResponse.body().isStatus()) {
                            if (farmSyncResponseResponse.body().getData() != null && !farmSyncResponseResponse.body().getData().isEmpty()) {
                                for (FarmDataSyncResponse farmDataSyncResponse : farmSyncResponseResponse.body().getData()) {
                                    if (farmDataSyncResponse.isSyncStatus()) {
                                        if (!TextUtils.isEmpty(farmDataSyncResponse.getTempFarmId()) && farmDataSyncResponse.getFarmId() > 0) {
                                            farmDetailsDao.updateFarmDetailsSynced(farmDataSyncResponse.getFarmId(), farmDataSyncResponse.getTempFarmId(),
                                                    farmDataSyncResponse.getInventoryOrder());
                                            farmCapturedDataDao.updateFarmCapturedDataSynced(farmDataSyncResponse.getInventoryOrder());
                                        } else {
                                            farmDetailsDao.updateFarmDetailsSynced(farmDataSyncResponse.getFarmId(), farmDataSyncResponse.getInventoryOrder());
                                            farmCapturedDataDao.updateFarmCapturedDataSynced(farmDataSyncResponse.getInventoryOrder());
                                        }
                                    }
                                }
                            }
                        }

                        callBack.onSuccess(farmSyncResponseResponse.body());
                    } else {
                        callBack.onError("ERROR_500");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FarmSyncResponse> call, @NonNull Throwable t) {
                    callBack.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("SyncRepository", "Error in SyncRepository syncFarmData", e);
            callBack.onError(e.getMessage());
        }
    }
}