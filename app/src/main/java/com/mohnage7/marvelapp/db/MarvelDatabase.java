package com.mohnage7.marvelapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mohnage7.marvelapp.characters.model.Character;
import com.mohnage7.marvelapp.details.model.Comic;

@Database(entities = {Character.class, Comic.class}, version = 1, exportSchema = false)
public abstract class MarvelDatabase extends RoomDatabase {

    private static final String DATA_BASE_NAME = "marvel_db";
    private static MarvelDatabase INSTANCE;

    public static synchronized MarvelDatabase getDatabaseInstance(Context context) {
        // insure that no other reference is created on different threads.
        if (INSTANCE == null) {
            // create our one and only db object.
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    // fallbackToDestructiveMigration is a migration strategy that destroy and re-creating existing db
                    // fallbackToDestructiveMigration is only used for small applications like we are implementing now
                    // for real projects we need to implement non-destructive migration strategy.
                    MarvelDatabase.class, DATA_BASE_NAME).fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public abstract CharacterDao getCharactersDao();
    public abstract ComicDao getVideosDao();
}
