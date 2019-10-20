package com.mohnage7.marvelapp.details.model;

import com.google.gson.annotations.SerializedName;
import com.mohnage7.marvelapp.base.BaseResponse;

public class ComicResponse extends BaseResponse {

    @SerializedName("data")
    ComicData comicData;

    @Override
    public ComicData getData() {
        return comicData;
    }
}
