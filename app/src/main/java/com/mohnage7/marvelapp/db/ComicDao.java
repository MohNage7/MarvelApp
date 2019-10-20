package com.mohnage7.marvelapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mohnage7.marvelapp.details.model.Comic;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ComicDao {
    @Insert(onConflict = REPLACE)
    void insertAll(List<Comic> comicList);

    @Query("SELECT * FROM comic WHERE characterId = :id")
    LiveData<List<Comic>> getAllComics(long id);
}
