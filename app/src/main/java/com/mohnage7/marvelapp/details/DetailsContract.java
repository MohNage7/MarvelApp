package com.mohnage7.marvelapp.details;

import com.mohnage7.marvelapp.details.model.Comic;

import java.util.List;

public interface DetailsContract {
    interface OnClickListener{
        void onClick(List<Comic> comic,int position);
    }
}
