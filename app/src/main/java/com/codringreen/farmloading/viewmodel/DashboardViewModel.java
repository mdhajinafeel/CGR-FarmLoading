package com.codringreen.farmloading.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.db.entity.FarmCapturedData;
import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.helper.PreferenceManager;
import com.codringreen.farmloading.model.FarmDetailDashboardModel;
import com.codringreen.farmloading.model.request.FarmCapturedDataRequest;
import com.codringreen.farmloading.model.request.FarmDetailsRequest;
import com.codringreen.farmloading.model.request.FarmSyncRequest;
import com.codringreen.farmloading.model.response.DownloadMaserResponse;
import com.codringreen.farmloading.model.response.FarmSyncResponse;
import com.codringreen.farmloading.repository.FarmRepository;
import com.codringreen.farmloading.repository.MasterRepository;
import com.codringreen.farmloading.repository.SyncRepository;
import com.codringreen.farmloading.service.api.ResponseCallBack;
import com.codringreen.farmloading.utils.CommonUtils;
import com.codringreen.farmloading.utils.NetworkConnectivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@SuppressLint("StaticFieldLeak")
public class DashboardViewModel extends BaseViewModel {
    private final Context context;
    private String errorTitle;
    private final MasterRepository masterRepository;
    private final FarmRepository farmRepository;
    private final SyncRepository syncRepository;
    private final NetworkConnectivity networkConnectivity;
    private final MutableLiveData<Boolean> progressState = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> downloadState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> syncStatus = new MutableLiveData<>();

    @Inject
    public DashboardViewModel(NetworkConnectivity networkConnectivity, MasterRepository masterRepository, FarmRepository farmRepository,
                              SyncRepository syncRepository, Context context) {
        this.context = context;
        this.networkConnectivity = networkConnectivity;
        this.masterRepository = masterRepository;
        this.farmRepository = farmRepository;
        this.syncRepository = syncRepository;
    }

    public void syncFarmData() {
        try {
            if (networkConnectivity.isNetworkAvailable()) {
                progressState.postValue(true);
                Thread.sleep(1000);

                //SYNC FARM DATA
                List<FarmDetails> farmDetailsUnSyncedList = farmRepository.unSyncedFarmDetails();
                List<FarmDetailsRequest> farmDetailsRequestList = new ArrayList<>();

                if (!farmDetailsUnSyncedList.isEmpty()) {

                    for (FarmDetails farmDetail : farmDetailsUnSyncedList) {
                        FarmDetailsRequest farmDetailsRequest = new FarmDetailsRequest();
                        farmDetailsRequest.setFarmId(farmDetail.getFarmId());
                        farmDetailsRequest.setTempFarmId(farmDetail.getTempFarmId());
                        farmDetailsRequest.setInventoryOrder(farmDetail.getInventoryOrder());
                        farmDetailsRequest.setPurchaseDate(CommonUtils.convertDateTimeFormatWithDefaultLocale(farmDetail.getPurchaseDate(), "dd/MM/yyyy", "yyyy-MM-dd"));
                        farmDetailsRequest.setTruckPlateNumber(farmDetail.getTruckPlateNumber());
                        farmDetailsRequest.setTruckDriverName(farmDetail.getTruckDriverName());
                        farmDetailsRequest.setClosedDate(farmDetail.getClosedDate());
                        farmDetailsRequest.setSupplierId(farmDetail.getSupplierId());
                        farmDetailsRequest.setProductId(farmDetail.getProductId());
                        farmDetailsRequest.setProductTypeId(farmDetail.getProductTypeId());
                        farmDetailsRequest.setPurchaseContractId(farmDetail.getPurchaseContractId());
                        farmDetailsRequest.setTotalPieces(farmDetail.getTotalPieces());
                        farmDetailsRequest.setCreatedBy(PreferenceManager.INSTANCE.getUserId());
                        farmDetailsRequest.setClosedBy(farmDetail.getClosedBy());
                        farmDetailsRequest.setOriginId(PreferenceManager.INSTANCE.getOriginId());
                        farmDetailsRequest.setCircAllowance(farmDetail.getCircAllowance());
                        farmDetailsRequest.setLengthAllowance(farmDetail.getLengthAllowance());
                        farmDetailsRequest.setGrossVolume(farmDetail.getGrossVolume());
                        farmDetailsRequest.setNetVolume(farmDetail.getNetVolume());
                        farmDetailsRequest.setClosed(farmDetail.isClosed());
                        farmDetailsRequest.setPurchaseUnitId(farmDetail.getPurchaseUnitId());

                        List<FarmCapturedData> farmCapturedDataUnSyncedList = farmRepository.unSyncedFarmCapturedData(farmDetail.getInventoryOrder());
                        List<FarmCapturedDataRequest> farmCapturedDataRequestList = new ArrayList<>();
                        if (!farmCapturedDataUnSyncedList.isEmpty()) {
                            for (FarmCapturedData farmCapturedData : farmCapturedDataUnSyncedList) {
                                FarmCapturedDataRequest farmCapturedDataRequest = new FarmCapturedDataRequest();
                                farmCapturedDataRequest.setPieces(farmCapturedData.getPieces());
                                farmCapturedDataRequest.setFarmDataId(farmCapturedData.getFarmDataId());
                                farmCapturedDataRequest.setCircumference(farmCapturedData.getCircumference());
                                farmCapturedDataRequest.setLength(farmCapturedData.getLength());
                                farmCapturedDataRequest.setGrossVolume(farmCapturedData.getGrossVolume());
                                farmCapturedDataRequest.setNetVolume(farmCapturedData.getNetVolume());
                                farmCapturedDataRequest.setCapturedTimeStamp(farmCapturedData.getCaptureTimeStamp());

                                farmCapturedDataRequestList.add(farmCapturedDataRequest);
                            }
                        }
                        farmDetailsRequest.setFarmCapturedData(farmCapturedDataRequestList);

                        farmDetailsRequestList.add(farmDetailsRequest);
                    }

                    FarmSyncRequest farmSyncRequest = new FarmSyncRequest();
                    farmSyncRequest.setFarmData(farmDetailsRequestList);

                    syncRepository.syncFarmData(farmSyncRequest, new ResponseCallBack<FarmSyncResponse>() {
                        @Override
                        public void onSuccess(FarmSyncResponse data) {
                            progressState.postValue(false);
                            if (data != null) {
                                if (data.isStatus()) {
                                    downloadMaster(true);
                                } else {
                                    syncStatus.postValue(false);
                                }
                            } else {
                                setErrorTitle(context.getString(R.string.error));
                                errorMessage.postValue(context.getString(R.string.common_error));
                                syncStatus.setValue(false);
                                downloadState.setValue(false);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            setErrorTitle(context.getString(R.string.error));
                            progressState.postValue(false);
                            errorMessage.postValue(message);
                            syncStatus.setValue(false);
                            downloadState.setValue(false);
                        }
                    });
                } else {
                    downloadMaster(false);
                }
            } else {
                setErrorTitle(context.getString(R.string.information));
                progressState.postValue(false);
                errorMessage.postValue(context.getString(R.string.no_internet));
            }
        } catch (Exception e) {
            Log.e("DashboardViewModel", "Error in DashboardViewModel syncFarmData", e);
            setErrorTitle(context.getString(R.string.error));
            progressState.postValue(false);
            errorMessage.postValue(e.getMessage());
        }
    }

    public void getMasterDownload() {
        try {
            if (networkConnectivity.isNetworkAvailable()) {
                progressState.postValue(true);
                Thread.sleep(1000L);
                downloadMaster(false);
            } else {
                setErrorTitle(context.getString(R.string.information));
                progressState.postValue(false);
                errorMessage.postValue(context.getString(R.string.no_internet));
            }
        } catch (Exception e) {
            Log.e("DashboardViewModel", "Error in DashboardViewModel getMasterDownload", e);
            setErrorTitle(context.getString(R.string.error));
            progressState.postValue(false);
            errorMessage.postValue(e.getMessage());
        }
    }

    public void downloadMaster(boolean isSync) {
        masterRepository.masterDownload(new ResponseCallBack<DownloadMaserResponse>() {
            @Override
            public void onSuccess(DownloadMaserResponse data) {
                progressState.postValue(false);
                if (data != null) {
                    if (data.isStatus()) {
                        if(isSync) {
                            setErrorTitle(context.getString(R.string.information));
                            syncStatus.postValue(true);
                        } else {
                            downloadState.postValue(true);
                        }
                    } else {
                        if(isSync) {
                            syncStatus.postValue(false);
                        } else {
                            downloadState.postValue(false);
                        }
                    }
                } else {
                    setErrorTitle(context.getString(R.string.error));
                    errorMessage.postValue(context.getString(R.string.common_error));
                    syncStatus.postValue(false);
                    downloadState.postValue(false);
                }
            }

            @Override
            public void onError(String message) {
                setErrorTitle(context.getString(R.string.error));
                progressState.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public LiveData<String> getError() {
        return errorMessage;
    }

    public LiveData<Boolean> getProgressBar() {
        return progressState;
    }

    public LiveData<Boolean> getDownloadState() {
        return downloadState;
    }

    public LiveData<Boolean> getSyncStatus() {
        return syncStatus;
    }

    public FarmDetailDashboardModel fetchTodayDashboardData(String todayDate) {
        return farmRepository.fetchTodayDashboardData(todayDate);
    }

    public FarmDetailDashboardModel fetchRecentDashboardData(String startDate, String endDate) {
        return farmRepository.fetchRecentDashboardData(startDate, endDate);
    }
}