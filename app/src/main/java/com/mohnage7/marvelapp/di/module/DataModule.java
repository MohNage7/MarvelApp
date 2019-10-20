package com.mohnage7.marvelapp.di.module;

import android.app.Application;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.mohnage7.marvelapp.details.repository.CharacterDetailsRepository;
import com.mohnage7.marvelapp.network.RestApiService;
import com.mohnage7.marvelapp.characters.repository.CharactersRepository;
import com.mohnage7.marvelapp.db.AppExecutors;
import com.mohnage7.marvelapp.db.MarvelDatabase;
import com.mohnage7.marvelapp.utils.LiveDataCallAdapterFactory;
import com.mohnage7.marvelapp.network.MarvelInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mohnage7.marvelapp.utils.Constants.BASE_URL;
import static com.mohnage7.marvelapp.utils.Constants.CONNECTION_TIMEOUT;
import static com.mohnage7.marvelapp.utils.Constants.PRIVATE_KEY;
import static com.mohnage7.marvelapp.utils.Constants.PUBLIC_KEY;
import static com.mohnage7.marvelapp.utils.Constants.READ_TIMEOUT;
import static com.mohnage7.marvelapp.utils.Constants.WRITE_TIMEOUT;

@Module
public class DataModule {
    private Application application;

    public DataModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                //converts Retrofit response into Observable
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

    /**
     * @return A configured {@link okhttp3.OkHttpClient} that will be used for executing network requests
     */
    @Provides
    @Singleton
    OkHttpClient providesHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // add request time out
        builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        // Add logging into
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.interceptors().add(httpLoggingInterceptor);
        builder.addNetworkInterceptor(new StethoInterceptor());
        builder.interceptors().add(new MarvelInterceptor(PRIVATE_KEY,PUBLIC_KEY));
        return builder.build();
    }



    @Provides
    @Singleton
    RestApiService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestApiService.class);
    }

    @Provides
    @Singleton
    AppExecutors providesAppExecutor() {
        return AppExecutors.getInstance();
    }

    @Provides
    CharacterDetailsRepository providesMovieDetailsRepository(RestApiService apiService, AppExecutors appExecutors) {
        return new CharacterDetailsRepository(apiService, appExecutors, application);
    }

    @Provides
    CharactersRepository providesMoviesRepository(RestApiService apiService, MarvelDatabase database, AppExecutors appExecutors) {
        return new CharactersRepository(apiService, database, appExecutors);
    }

    @Provides
    @Singleton
    MarvelDatabase providesDatabase(){
        return MarvelDatabase.getDatabaseInstance(application);
    }


}
