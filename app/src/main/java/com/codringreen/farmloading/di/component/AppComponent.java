package com.codringreen.farmloading.di.component;

import android.app.Application;

import com.codringreen.farmloading.CGRFarmLoadingApplication;
import com.codringreen.farmloading.di.modules.ActivityModule;
import com.codringreen.farmloading.di.modules.ApiModule;
import com.codringreen.farmloading.di.modules.AppModule;
import com.codringreen.farmloading.di.modules.DBModule;
import com.codringreen.farmloading.di.modules.RepoModule;
import com.codringreen.farmloading.di.modules.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, DBModule.class, ActivityModule.class, AppModule.class,
        ApiModule.class, RepoModule.class, ViewModelModule.class})
public interface AppComponent {
    void inject(CGRFarmLoadingApplication application);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }
}