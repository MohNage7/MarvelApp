package com.mohnage7.marvelapp.characters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohnage7.marvelapp.characters.model.Character;

public interface CharactersContract {
    interface OnCharacterClickListener{
        void onCharacterClick(Character character, ImageView view, TextView textView);
    }
    interface OnLoadMoreListener {
        void onLoadMore();
    }
}
