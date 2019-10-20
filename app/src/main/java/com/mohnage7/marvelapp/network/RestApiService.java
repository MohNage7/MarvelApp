package com.mohnage7.marvelapp.network;


import androidx.lifecycle.LiveData;

import com.mohnage7.marvelapp.characters.model.CharactersResponse;
import com.mohnage7.marvelapp.details.model.ComicResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

    @GET("characters")
    LiveData<ApiResponse<CharactersResponse>> getCharacters(@Query("offset") int offset,@Query("limit") int limit);


    @GET("characters/{characterId}/comics")
    LiveData<ApiResponse<ComicResponse>> getComics(@Path("characterId") long id);

    @GET("characters")
    Call<CharactersResponse> search(@Query("name") String query);
}
