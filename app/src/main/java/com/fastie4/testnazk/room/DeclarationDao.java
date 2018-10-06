package com.fastie4.testnazk.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Flowable;

@Dao
public interface DeclarationDao {
    @Query("SELECT * FROM declaration")
    Flowable<List<Declaration>> getAll();

    @Query("SELECT id,note FROM declaration")
    Flowable<List<Declaration>> getFavouritesIdsAndNotes();

    @Insert
    void insert(Declaration declaration);

    @Delete
    void delete(Declaration declaration);

    @Query("UPDATE declaration SET note=:note WHERE id=:id ")
    void updateNote(String id, String note);
}
