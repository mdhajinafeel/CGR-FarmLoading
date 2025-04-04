package com.codringreen.farmloading.managers.interceptors;

import androidx.annotation.NonNull;

import com.codringreen.farmloading.helper.PreferenceManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


public class BasicTokenInterceptor implements Interceptor {
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        try {
            return chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + PreferenceManager.INSTANCE.getAccessToken()).build());
        } catch (Exception e) {
            throw e;
        }
    }
}