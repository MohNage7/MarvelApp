package com.mohnage7.marvelapp.characters.model;

import com.google.gson.annotations.SerializedName;
import com.mohnage7.marvelapp.base.BaseResponse;

public class CharactersResponse extends BaseResponse {

    @SerializedName("data")
    CharactersData charactersData;

    @Override
    public CharactersData getData() {
        return charactersData;
    }
}
