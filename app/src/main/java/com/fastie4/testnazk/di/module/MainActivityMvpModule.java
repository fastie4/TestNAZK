package com.fastie4.testnazk.di.module;

import com.fastie4.testnazk.MainActivity;
import com.fastie4.testnazk.di.scopes.ActivityScope;
import com.fastie4.testnazk.mvp.MainActivityContract;
import com.fastie4.testnazk.mvp.PresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainActivityMvpModule {
    @Binds
    @ActivityScope
    abstract MainActivityContract.View view(MainActivity a);

    @Binds
    @ActivityScope
    abstract MainActivityContract.Presenter presenter(PresenterImpl presenter);
}