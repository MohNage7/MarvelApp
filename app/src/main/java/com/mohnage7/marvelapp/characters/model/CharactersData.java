package com.mohnage7.marvelapp.characters.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CharactersData {
    @SerializedName("results")
    List<Character> charactersList;

    public List<Character> getCharactersList() {
        return charactersList;
    }

    public void setCharactersList(List<Character> charactersList) {
        this.charactersList = charactersList;
    }
}
