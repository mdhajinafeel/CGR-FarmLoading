package com.codringreen.farmloading.di.modules;

import android.util.Log;

import com.codringreen.farmloading.BuildConfig;
import com.codringreen.farmloading.managers.interceptors.BasicAuthInterceptor;
import com.codringreen.farmloading.managers.interceptors.BasicTokenInterceptor;
import com.codringreen.farmloading.service.api.IAuthApiService;
import com.codringreen.farmloading.service.api.IMasterApiService;
import com.codringreen.farmloading.service.api.ISyncApiService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    OkHttpClient provideOkhttpClient() {
        try {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(httpLoggingInterceptor);
            builder.addInterceptor(new BasicTokenInterceptor());
            builder.connectTimeout(300L, TimeUnit.SECONDS);
            builder.readTimeout(300L, TimeUnit.SECONDS);
            return builder.build();
        } catch (Exception e) {
            Log.e("ApiModule", "Error in ApiModule", e);
            return null;
        }
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BuildConfig.INVENTORY_BASE_URL)
                .client(okHttpClient).build();
    }

    @Provides
    IAuthApiService provideAuthApi() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(httpLoggingInterceptor);
        builder.addInterceptor(new BasicAuthInterceptor());
        builder.connectTimeout(300L, TimeUnit.SECONDS);
        builder.readTimeout(300L, TimeUnit.SECONDS);
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BuildConfig.INVENTORY_BASE_URL).client(builder.build()).build().create(IAuthApiService.class);
    }

    @Provides
    @Singleton
    IMasterApiService provideIMasterApiService(Retrofit retrofit) {
        return retrofit.create(IMasterApiService.class);
    }

    @Provides
    @Singleton
    ISyncApiService provideISyncApiService(Retrofit retrofit) {
        return retrofit.create(ISyncApiService.class);
    }
}
