package com.fastie4.testnazk.di.module;

import android.app.Activity;
import android.content.Context;

import com.fastie4.testnazk.MainActivity;
import com.fastie4.testnazk.di.component.MainActivitySubcomponent;
import com.fastie4.testnazk.di.qualifier.ActivityContext;
import com.fastie4.testnazk.di.scopes.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = MainActivitySubcomponent.class)
public abstract class MainActivityModule {
    @Binds
    @ActivityScope
    @ActivityContext
    abstract Context context(MainActivity activity);

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
        bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);
}
