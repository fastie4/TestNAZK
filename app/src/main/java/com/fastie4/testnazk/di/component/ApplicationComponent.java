package com.fastie4.testnazk.di.component;

import android.app.Application;

import com.fastie4.testnazk.App;
import com.fastie4.testnazk.di.module.ContextModule;
import com.fastie4.testnazk.di.module.MainActivityModule;
import com.fastie4.testnazk.di.module.RetrofitModule;
import com.fastie4.testnazk.di.module.RoomModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, ContextModule.class, RetrofitModule.class, MainActivityModule.class, RoomModule.class})
public interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application app);
        ApplicationComponent build();
    }

    void inject(App app);
}
