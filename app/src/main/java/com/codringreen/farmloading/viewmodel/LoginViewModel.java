package com.codringreen.farmloading.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.helper.PreferenceManager;
import com.codringreen.farmloading.model.request.LoginRequest;
import com.codringreen.farmloading.model.response.LoginResponse;
import com.codringreen.farmloading.repository.LoginRepository;
import com.codringreen.farmloading.service.api.ResponseCallBack;
import com.codringreen.farmloading.utils.NetworkConnectivity;

import java.util.UUID;

import javax.inject.Inject;

@SuppressLint("StaticFieldLeak")
public class LoginViewModel extends BaseViewModel {

    private final Context context;
    private String errorTitle;
    private final LoginRepository loginRepository;
    private final NetworkConnectivity networkConnectivity;
    private final MutableLiveData<Boolean> progressState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public LoginViewModel(NetworkConnectivity networkConnectivity, LoginRepository loginRepository, Context context) {
        this.context = context;
        this.networkConnectivity = networkConnectivity;
        this.loginRepository = loginRepository;
    }

    public void postLogin(LoginRequest loginRequest) {
        try {
            if (networkConnectivity.isNetworkAvailable()) {
                progressState.postValue(true);
                Thread.sleep(1000L);

                loginRepository.postLogin(loginRequest, new ResponseCallBack<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse data) {

                        if (data.isStatus()) {
                            PreferenceManager.INSTANCE.setName(data.getData().getFullName());
                            PreferenceManager.INSTANCE.setLoginExpiry(data.getData().getExpiresIn());
                            PreferenceManager.INSTANCE.setPhoto(data.getData().getProfilePhoto());
                            PreferenceManager.INSTANCE.setUserId(data.getData().getUserId());
                            PreferenceManager.INSTANCE.setOriginId(data.getData().getOriginId());
                            PreferenceManager.INSTANCE.setAccessToken(data.getData().getAccessToken());
                            PreferenceManager.INSTANCE.setRefreshToken(data.getData().getRefreshToken());
                            PreferenceManager.INSTANCE.setLoggedIn(data.isStatus());
                            PreferenceManager.INSTANCE.setKeyInstanceid(UUID.randomUUID().toString());
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {
                                Log.e("LoginModel", "Error in LoginModel - Post Login", e);
                            }
                            progressState.postValue(false);
                            loginStatus.postValue(true);
                            return;
                        }
                        setErrorTitle(context.getString(R.string.error));
                        errorMessage.postValue(context.getString(R.string.invalid_user));
                    }

                    @Override
                    public void onError(String message) {
                        setErrorTitle(context.getString(R.string.error));
                        progressState.postValue(false);
                        errorMessage.postValue(context.getString(R.string.invalid_user));
                    }
                });
            } else {
                setErrorTitle(context.getString(R.string.information));
                this.progressState.postValue(false);
                this.errorMessage.postValue(context.getString(R.string.no_internet));
            }
        } catch (Exception e) {
            Log.e("LoginModel", "Error in LoginModel - Post Login", e);
            setErrorTitle(context.getString(R.string.error));
            this.progressState.postValue(false);
            this.errorMessage.postValue(e.getMessage());
        }
    }

    public LiveData<String> getError() {
        return errorMessage;
    }

    public LiveData<Boolean> getProgressBar() {
        return progressState;
    }

    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }
}