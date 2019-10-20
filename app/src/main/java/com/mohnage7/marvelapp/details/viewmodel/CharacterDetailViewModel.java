package com.mohnage7.marvelapp.details.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mohnage7.marvelapp.MarvelApplication;
import com.mohnage7.marvelapp.base.DataWrapper;
import com.mohnage7.marvelapp.details.model.Comic;
import com.mohnage7.marvelapp.details.repository.CharacterDetailsRepository;

import java.util.List;

import javax.inject.Inject;

public class CharacterDetailViewModel extends ViewModel {

    @Inject
    CharacterDetailsRepository repository;

    public CharacterDetailViewModel() {
        MarvelApplication.getInstance().getDataComponent().inject(this);
    }


    public LiveData<DataWrapper<List<Comic>>> getComics(long comicId) {
        return repository.getComics(comicId);
    }

}
