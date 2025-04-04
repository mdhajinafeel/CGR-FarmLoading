package com.codringreen.farmloading.di.modules;

import com.codringreen.farmloading.view.activities.CreateFarmActivity;
import com.codringreen.farmloading.view.activities.DashboardActivity;
import com.codringreen.farmloading.view.activities.FarmCapturedDataActivity;
import com.codringreen.farmloading.view.activities.FarmDataActivity;
import com.codringreen.farmloading.view.activities.FarmListsActivity;
import com.codringreen.farmloading.view.activities.LoginActivity;
import com.codringreen.farmloading.view.activities.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector
    abstract DashboardActivity contributeDashboardActivity();

    @ContributesAndroidInjector
    abstract FarmListsActivity contributeFarmListsActivity();

    @ContributesAndroidInjector
    abstract CreateFarmActivity contributeCreateFarmActivity();

    @ContributesAndroidInjector
    abstract FarmDataActivity contributeFarmDataActivity();

    @ContributesAndroidInjector
    abstract FarmCapturedDataActivity contributeFarmCapturedDataActivity();
}