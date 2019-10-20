package com.mohnage7.marvelapp.di.component;


import com.mohnage7.marvelapp.MarvelApplication;
import com.mohnage7.marvelapp.di.module.DataModule;
import com.mohnage7.marvelapp.details.viewmodel.CharacterDetailViewModel;
import com.mohnage7.marvelapp.characters.viewmodel.CharactersViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class})
public interface DataComponent {


    void inject(CharacterDetailViewModel characterDetailViewModel);

    void inject(CharactersViewModel charactersViewModel);

    void inject(MarvelApplication marvelApplication);
}
