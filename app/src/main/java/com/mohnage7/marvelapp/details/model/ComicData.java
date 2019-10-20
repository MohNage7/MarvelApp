package com.mohnage7.marvelapp.details.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComicData {
    @SerializedName("results")
    List<Comic> comicList;

    public List<Comic> getComicList() {
        return comicList;
    }

    public void setComicList(List<Comic> comicList) {
        this.comicList = comicList;
    }
}
