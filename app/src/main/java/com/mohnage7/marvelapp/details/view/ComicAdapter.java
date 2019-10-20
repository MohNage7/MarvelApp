package com.mohnage7.marvelapp.details.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohnage7.marvelapp.R;
import com.mohnage7.marvelapp.base.BaseViewHolder;
import com.mohnage7.marvelapp.details.DetailsContract;
import com.mohnage7.marvelapp.details.model.Comic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Comic> comicList;
    private DetailsContract.OnClickListener onClickListener;

    ComicAdapter(List<Comic> comicList, DetailsContract.OnClickListener onClickListener) {
        this.comicList = comicList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_comic, parent, false);
        return new ComicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof ComicViewHolder) {
            holder.bindViews(position);
        }
    }

    @Override
    public int getItemCount() {
        if (comicList != null && !comicList.isEmpty()) {
            return comicList.size();
        } else {
            return 0;
        }
    }

    protected class ComicViewHolder extends BaseViewHolder {
        @BindView(R.id.content_img_view)
        ImageView comicImgView;
        @BindView(R.id.content_title_txt_view)
        TextView comicTitle;

        ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            Comic comic = comicList.get(position);
            comicTitle.setText(comic.getTitle());
            if (comic.getThumbnail() != null && comic.getThumbnail().getPath() != null
                    && !comic.getThumbnail().getExtension().isEmpty()) {
                if (comic.getThumbnail().getPath().equalsIgnoreCase("gif")) {
                    displayGif(comic.getThumbnail().getImageURL());
                } else {
                    displayImage(comic.getThumbnail().getImageURL());
                }
            } else {
                Glide.with(itemView.getContext()).load(R.drawable.placeholder).into(comicImgView);
            }
            itemView.setOnClickListener(v -> onClickListener.onClick(comicList,position));
        }

        private void displayImage(String imageUrl) {
            Glide.with(itemView.getContext()).load(imageUrl).error(R.drawable.placeholder).into(comicImgView);
        }


        private void displayGif(String imageUrl) {
            Glide.with(itemView.getContext()).asGif()
                    .load(imageUrl).error(R.drawable.placeholder).into(comicImgView);
        }

        @Override
        public void clear() {
            comicTitle.setText("");
            comicImgView.setImageDrawable(null);
        }


    }
}
