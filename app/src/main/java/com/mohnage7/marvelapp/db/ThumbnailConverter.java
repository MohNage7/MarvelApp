package com.mohnage7.marvelapp.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohnage7.marvelapp.model.Thumbnail;

import java.lang.reflect.Type;

public class ThumbnailConverter {

    @TypeConverter
    public String fromThumbnail(Thumbnail thumbnail) {
        if (thumbnail == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Thumbnail>() {
        }.getType();
        String json = gson.toJson(thumbnail, type);
        return json;
    }

    @TypeConverter
    public Thumbnail toThumbnail(String thumbnail1) {
        if (thumbnail1 == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Thumbnail>() {}.getType();
        return gson.fromJson(thumbnail1, type);
    }
}
