package com.mohnage7.marvelapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mohnage7.marvelapp.characters.model.Character;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CharacterDao {
    @Insert(onConflict = REPLACE)
    void insertAll(List<Character> characterList);

    @Query("SELECT * FROM character")
    LiveData<List<Character>> getAllCharacters();

    @Query("SELECT * FROM character ORDER BY primaryKey DESC LIMIT 20")
    LiveData<List<Character>> getLatestCharacters();

    @Query("DELETE FROM character")
    void deleteAll();
}
