package com.fastie4.testnazk.di.module;

import android.content.Context;

import com.fastie4.testnazk.di.qualifier.ApplicationContext;
import com.fastie4.testnazk.room.AppDatabase;
import com.fastie4.testnazk.room.DeclarationDao;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    @Provides
    @Singleton
    AppDatabase database(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "db").build();
    }

    @Provides
    @Singleton
    DeclarationDao declarationDao(AppDatabase database) {
        return database.declarationDao();
    }
}
