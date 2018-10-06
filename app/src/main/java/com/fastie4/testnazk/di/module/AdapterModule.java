package com.fastie4.testnazk.di.module;

import com.fastie4.testnazk.MainActivity;
import com.fastie4.testnazk.adapter.RecyclerViewAdapter;
import com.fastie4.testnazk.di.scopes.ActivityScope;
import com.fastie4.testnazk.mvp.PresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class AdapterModule {
    @Provides
    @ActivityScope
    RecyclerViewAdapter adapter(PresenterImpl presenter, MainActivity activity) {
        return new RecyclerViewAdapter(presenter, activity);
    }
}
