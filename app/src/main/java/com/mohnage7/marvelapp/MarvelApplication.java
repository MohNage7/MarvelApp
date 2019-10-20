package com.mohnage7.marvelapp;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.mohnage7.marvelapp.di.component.DaggerDataComponent;
import com.mohnage7.marvelapp.di.component.DataComponent;
import com.mohnage7.marvelapp.di.module.DataModule;

public class MarvelApplication extends Application {

    private static MarvelApplication instance;
    DataComponent dataComponent;

    public static MarvelApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDataComponent();
        // init debug lib. used to monitor networks call logs and db
        Stetho.initializeWithDefaults(this);
    }

    private void initDataComponent() {
        dataComponent = DaggerDataComponent.builder().
                dataModule(new DataModule(this))
                .build();
        dataComponent.inject(this);
    }

    public DataComponent getDataComponent() {
        return dataComponent;
    }
}
