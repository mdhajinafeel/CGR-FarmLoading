package com.codringreen.farmloading;

import android.app.Application;

import com.codringreen.farmloading.di.component.DaggerAppComponent;
import com.codringreen.farmloading.helper.PreferenceManager;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class CGRFarmLoadingApplication extends Application implements HasAndroidInjector {

    @Inject
    public CGRFarmLoadingApplication() {
    }

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        //STETHO
        Stetho.initializeWithDefaults(this);

        //PREFERENCE MANAGER
        PreferenceManager.INSTANCE.createPreferences(this);
        DaggerAppComponent.factory().create(this).inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}