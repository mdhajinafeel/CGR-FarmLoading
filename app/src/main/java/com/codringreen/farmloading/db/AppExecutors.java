package com.codringreen.farmloading.db;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private final Executor diskIO;
    AppExecutors(Executor diskIO){
        this.diskIO = diskIO;
    }

    public AppExecutors() {
        this(Executors.newSingleThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }
}