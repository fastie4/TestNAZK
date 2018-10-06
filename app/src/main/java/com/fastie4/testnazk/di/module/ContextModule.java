package com.fastie4.testnazk.di.module;

import android.app.Application;
import android.content.Context;

import com.fastie4.testnazk.di.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ContextModule {
    @Binds
    @Singleton
    @ApplicationContext
    abstract Context context(Application app);
}
