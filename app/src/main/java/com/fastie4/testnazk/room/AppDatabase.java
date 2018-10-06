package com.fastie4.testnazk.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Declaration.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeclarationDao declarationDao();
}