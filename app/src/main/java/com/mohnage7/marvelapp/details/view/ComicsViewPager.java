package com.mohnage7.marvelapp.details.view;

import android.content.Context;
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
import com.mohnage7.marvelapp.details.model.Comic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicsViewPager extends RecyclerView.Adapter<ComicsViewPager.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Comic> comicList;
    private Context mContext;

    ComicsViewPager(List<Comic> comicList, Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_comic_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.title_tv)
        TextView titleTxtView;
        @BindView(R.id.content_iv)
        ImageView contentImgView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            Comic comic = comicList.get(position);
            titleTxtView.setText(comic.getTitle());
            if (comic.getThumbnail() != null && comic.getThumbnail().getPath() != null
                    && !comic.getThumbnail().getExtension().isEmpty()) {
                if (comic.getThumbnail().getPath().equalsIgnoreCase("gif")) {
                    displayGif(comic.getThumbnail().getImageURL());
                } else {
                    displayImage(comic.getThumbnail().getImageURL());
                }
            } else {
                Glide.with(mContext).load(R.drawable.placeholder).into(contentImgView);
            }
        }

        private void displayImage(String imageUrl) {
            Glide.with(mContext).load(imageUrl).error(R.drawable.placeholder).into(contentImgView);
        }

        private void displayGif(String imageUrl) {
            Glide.with(mContext).asGif().load(imageUrl).error(R.drawable.placeholder).into(contentImgView);
        }

        @Override
        public void clear() {
            titleTxtView.setText("");
            contentImgView.setImageDrawable(null);
        }
    }
}
