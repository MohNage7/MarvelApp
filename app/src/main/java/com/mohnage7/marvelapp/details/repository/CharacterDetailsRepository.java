package com.mohnage7.marvelapp.details.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.mohnage7.marvelapp.network.ApiResponse;
import com.mohnage7.marvelapp.network.RestApiService;
import com.mohnage7.marvelapp.base.DataWrapper;
import com.mohnage7.marvelapp.db.AppExecutors;
import com.mohnage7.marvelapp.db.ComicDao;
import com.mohnage7.marvelapp.db.MarvelDatabase;
import com.mohnage7.marvelapp.details.model.Comic;
import com.mohnage7.marvelapp.details.model.ComicResponse;
import com.mohnage7.marvelapp.network.NetworkBoundResource;
import com.mohnage7.marvelapp.network.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

public class CharacterDetailsRepository {

    private final AppExecutors appExecutors;
    private RestApiService apiService;
    private ComicDao comicDao;

    @Inject
    public CharacterDetailsRepository(RestApiService apiService, AppExecutors appExecutors, Context context) {
        this.apiService = apiService;
        this.appExecutors = appExecutors;
        comicDao = MarvelDatabase.getDatabaseInstance(context).getVideosDao();
    }

    public LiveData<DataWrapper<List<Comic>>> getComics(long characterId) {
        return new NetworkBoundResource<List<Comic>, ComicResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull ComicResponse item) {
                addCharacterIdToEachComic(item, characterId);
                comicDao.insertAll(item.getData().getComicList());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Comic> comicList) {
                return comicList == null || comicList.isEmpty() || NetworkUtils.isNetworkAvailable();
            }

            @NonNull
            @Override
            protected LiveData<List<Comic>> loadFromDb() {
                return comicDao.getAllComics(characterId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ComicResponse>> createCall() {
                return apiService.getComics(characterId);
            }
        }.getAsLiveData();
    }

    private void addCharacterIdToEachComic(@NonNull ComicResponse item, long characterId) {
        for (Comic comic : item.getData().getComicList()){
            comic.setCharacterId(characterId);
        }
    }
}
