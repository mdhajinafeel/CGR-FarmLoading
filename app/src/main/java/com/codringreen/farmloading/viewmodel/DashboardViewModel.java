package com.codringreen.farmloading.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.model.response.DownloadMaserResponse;
import com.codringreen.farmloading.repository.MasterRepository;
import com.codringreen.farmloading.service.api.ResponseCallBack;
import com.codringreen.farmloading.utils.NetworkConnectivity;

import javax.inject.Inject;

@SuppressLint("StaticFieldLeak")
public class DashboardViewModel extends BaseViewModel {
    private final Context context;
    private String errorTitle;
    private final MasterRepository masterRepository;
    private final NetworkConnectivity networkConnectivity;
    private final MutableLiveData<Boolean> progressState = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> downloadState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> syncStatus = new MutableLiveData<>();

    @Inject
    public DashboardViewModel(NetworkConnectivity networkConnectivity, MasterRepository masterRepository, Context context) {
        this.context = context;
        this.networkConnectivity = networkConnectivity;
        this.masterRepository = masterRepository;
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
            Log.e("DashboardViewModel", "Error in MasterRepository getMasterDownload", e);
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
                        if (!isSync) {
                            downloadState.postValue(true);
                            return;
                        }
                        setErrorTitle(context.getString(R.string.information));
                        syncStatus.postValue(true);
                        return;
                    }
                    if (isSync) {
                        syncStatus.postValue(false);
                    } else {
                        downloadState.postValue(false);
                    }
                    return;
                }
                setErrorTitle(context.getString(R.string.error));
                errorMessage.postValue(context.getString(R.string.common_error));
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
}