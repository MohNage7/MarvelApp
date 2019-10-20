package com.mohnage7.marvelapp.characters.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohnage7.marvelapp.network.ApiResponse;
import com.mohnage7.marvelapp.network.RestApiService;
import com.mohnage7.marvelapp.base.DataWrapper;
import com.mohnage7.marvelapp.characters.model.Character;
import com.mohnage7.marvelapp.characters.model.CharactersResponse;
import com.mohnage7.marvelapp.db.AppExecutors;
import com.mohnage7.marvelapp.db.CharacterDao;
import com.mohnage7.marvelapp.db.MarvelDatabase;
import com.mohnage7.marvelapp.network.NetworkBoundResource;
import com.mohnage7.marvelapp.network.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mohnage7.marvelapp.utils.Constants.DEFAULT_OFFSET;
import static com.mohnage7.marvelapp.utils.Constants.LIMIT;

public class CharactersRepository {

    private final AppExecutors appExecutors;
    private RestApiService apiService;
    private MutableLiveData<DataWrapper<List<Character>>> mutableSearchMovieLiveData = new MutableLiveData<>();
    private CharacterDao characterDao;

    @Inject
    public CharactersRepository(RestApiService apiService, MarvelDatabase marvelDatabase, AppExecutors appExecutors) {
        this.apiService = apiService;
        this.appExecutors = appExecutors;
        characterDao = marvelDatabase.getCharactersDao();
    }

    public LiveData<DataWrapper<List<Character>>> getCharacters(int offset) {
        return new NetworkBoundResource<List<Character>, CharactersResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull CharactersResponse item) {
                // make sure that the db is clean.
                if (offset == DEFAULT_OFFSET){
                    characterDao.deleteAll();
                }
                characterDao.insertAll(item.getData().getCharactersList());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Character> characterList) {
               return characterList == null || characterList.isEmpty() || NetworkUtils.isNetworkAvailable();
            }

            @NonNull
            @Override
            protected LiveData<List<Character>> loadFromDb() {
                if (offset == DEFAULT_OFFSET)
                    return characterDao.getAllCharacters();
                else
                    return characterDao.getLatestCharacters();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<CharactersResponse>> createCall() {
                return apiService.getCharacters(offset,LIMIT);
            }
        }.getAsLiveData();
    }


    public LiveData<DataWrapper<List<Character>>> search(String query) {
        mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.LOADING, null, null));
        Call<CharactersResponse> call = apiService.search(query);
        call.enqueue(new Callback<CharactersResponse>() {
            @Override
            public void onResponse(@NonNull Call<CharactersResponse> call, @NonNull Response<CharactersResponse> response) {
                CharactersResponse mResponse = response.body();
                if (mResponse != null) {
                    if (mResponse.getData().getCharactersList() != null && !mResponse.getData().getCharactersList().isEmpty()) {
                        mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.SUCCESS, mResponse.getData().getCharactersList(), null));
                    } else {
                        mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.ERROR, null, mResponse.getStatusMessage()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharactersResponse> call, @NonNull Throwable error) {
                mutableSearchMovieLiveData.setValue(new DataWrapper<>(DataWrapper.Status.ERROR, null, error.getMessage().equals("") ? error.getMessage() : "Unknown error"));
            }
        });
        return mutableSearchMovieLiveData;
    }
}
