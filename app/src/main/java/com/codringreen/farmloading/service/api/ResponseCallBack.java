package com.codringreen.farmloading.service.api;

public interface ResponseCallBack<T> {

    void onError(String message);

    void onSuccess(T data);
}