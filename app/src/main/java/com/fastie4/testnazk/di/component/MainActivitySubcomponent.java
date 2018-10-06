package com.fastie4.testnazk.di.component;

import com.fastie4.testnazk.MainActivity;
import com.fastie4.testnazk.di.module.AdapterModule;
import com.fastie4.testnazk.di.module.MainActivityMvpModule;
import com.fastie4.testnazk.di.scopes.ActivityScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ActivityScope
@Subcomponent(modules = {MainActivityMvpModule.class, AdapterModule.class})
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    }
}