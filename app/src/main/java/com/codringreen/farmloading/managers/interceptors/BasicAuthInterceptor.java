package com.codringreen.farmloading.managers.interceptors;

import androidx.annotation.NonNull;

import com.codringreen.farmloading.BuildConfig;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private final String credentials = Credentials.basic(BuildConfig.CLIENT_USERNAME, BuildConfig.CLIENT_PASSWORD);

    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        try {
            return chain.proceed(chain.request().newBuilder().header("Authorization", this.credentials).build());
        } catch (Exception e) {
            throw e;
        }
    }
}