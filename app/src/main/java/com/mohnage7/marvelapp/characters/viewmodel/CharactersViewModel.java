package com.mohnage7.marvelapp.characters.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.mohnage7.marvelapp.MarvelApplication;
import com.mohnage7.marvelapp.base.DataWrapper;
import com.mohnage7.marvelapp.characters.repository.CharactersRepository;
import com.mohnage7.marvelapp.characters.model.Character;

import java.util.List;

import javax.inject.Inject;

public class CharactersViewModel extends ViewModel {

    @Inject
    CharactersRepository repository;

    private MutableLiveData<String> searchBy = new MutableLiveData<>();
    private LiveData<DataWrapper<List<Character>>> searchCharacterList;
    private LiveData<DataWrapper<List<Character>>> charactersList;
    private MutableLiveData<Integer> offsetMutableLiveData = new MutableLiveData<>();
    private boolean isLoadMore;

    public CharactersViewModel() {
        MarvelApplication.getInstance().getDataComponent().inject(this);
        searchCharacterList = Transformations.switchMap(searchBy, query -> repository.search(query));
        charactersList = Transformations.switchMap(offsetMutableLiveData, offset -> repository.getCharacters(offset));
    }

    public LiveData<DataWrapper<List<Character>>> getCharactersList() {
        return charactersList;
    }

    public LiveData<DataWrapper<List<Character>>> search() {
        return searchCharacterList;
    }



    public void setSearchBy(String query) {
        searchBy.setValue(query);
    }

    public void setOffset(int offset,boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        offsetMutableLiveData.setValue(offset);
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }
}
