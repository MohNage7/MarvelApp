package com.mohnage7.marvelapp.characters.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.mohnage7.marvelapp.db.ThumbnailConverter;
import com.mohnage7.marvelapp.model.Thumbnail;

@Entity(tableName = "character")
public class Character implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long primaryKey;
    private long id;
    private String name;
    private String description;
    @TypeConverters(ThumbnailConverter.class)
    @SerializedName("thumbnail")
    private Thumbnail thumbnail;

    public Character() {
    }


    protected Character(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        primaryKey = in.readLong();
    }

    public static final Creator<Character> CREATOR = new Creator<Character>() {
        @Override
        public Character createFromParcel(Parcel in) {
            return new Character(in);
        }

        @Override
        public Character[] newArray(int size) {
            return new Character[size];
        }
    };

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(thumbnail, flags);
        dest.writeLong(primaryKey);
    }
}
