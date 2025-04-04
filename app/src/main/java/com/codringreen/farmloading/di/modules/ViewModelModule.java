package com.codringreen.farmloading.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.codringreen.farmloading.viewmodel.DashboardViewModel;
import com.codringreen.farmloading.viewmodel.FarmViewModel;
import com.codringreen.farmloading.viewmodel.LoginViewModel;
import com.codringreen.farmloading.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @ViewModelKey(LoginViewModel.class)
    @Binds
    @IntoMap
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @ViewModelKey(DashboardViewModel.class)
    @Binds
    @IntoMap
    abstract ViewModel bindDashboardViewModel(DashboardViewModel dashboardViewModel);

    @ViewModelKey(FarmViewModel.class)
    @Binds
    @IntoMap
    abstract ViewModel bindFarmViewModel(FarmViewModel farmViewModel);
}