package com.mohnage7.marvelapp.details.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration {
    private int marginLeft;
    private int mMargin;

    HorizontalMarginItemDecoration(Context context, @DimenRes int margin) {
        mMargin = (int) context.getResources().getDimension(margin);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = mMargin;
        outRect.left = mMargin;
    }
}
