package com.mohnage7.marvelapp.details.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.mohnage7.marvelapp.db.ThumbnailConverter;
import com.mohnage7.marvelapp.model.Thumbnail;

@Entity(tableName = "comic")
public class Comic implements Parcelable {
    @PrimaryKey
    private int id;
    private String title;
    @TypeConverters(ThumbnailConverter.class)
    private
    Thumbnail thumbnail;
    private long characterId;

    public Comic() {
    }

    protected Comic(Parcel in) {
        id = in.readInt();
        title = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        characterId = in.readLong();
    }

    public static final Creator<Comic> CREATOR = new Creator<Comic>() {
        @Override
        public Comic createFromParcel(Parcel in) {
            return new Comic(in);
        }

        @Override
        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };

    public long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeParcelable(thumbnail, flags);
        dest.writeLong(characterId);
    }
}
