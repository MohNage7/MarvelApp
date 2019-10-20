package com.mohnage7.marvelapp.characters.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mohnage7.marvelapp.R;
import com.mohnage7.marvelapp.base.BaseViewHolder;
import com.mohnage7.marvelapp.characters.CharactersContract;
import com.mohnage7.marvelapp.characters.model.Character;
import com.mohnage7.marvelapp.network.NetworkUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharactersAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int SEARCH_ITEM = 2;
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;
    private boolean isSearchAdapter;
    private List<Character> mCharactersList;
    private CharactersContract.OnCharacterClickListener onCharacterClickListener;
    private RecyclerView recyclerView;
    private CharactersContract.OnLoadMoreListener onLoadMoreListener;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem;
    private int totalItemCount;
    private boolean loading;
    private Context mContext;
    private RequestOptions requestOptions;

    CharactersAdapter(List<Character> mCharactersList, CharactersContract.OnCharacterClickListener onCharacterClickListener, RecyclerView recyclerView, Context context) {
        this.mCharactersList = mCharactersList;
        this.onCharacterClickListener = onCharacterClickListener;
        this.recyclerView = recyclerView;
        this.mContext = context;
        // listen for scrolling changes
        onScrollListener();
    }

    CharactersAdapter(List<Character> mCharactersList, CharactersContract.OnCharacterClickListener onCharacterClickListener, Context context, boolean isSearchAdapter) {
        this.mCharactersList = mCharactersList;
        this.onCharacterClickListener = onCharacterClickListener;
        this.mContext = context;
        this.isSearchAdapter = isSearchAdapter;
        // resize images when using search view
        requestOptions = new RequestOptions().override(120, 150);
    }


    private void onScrollListener() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount != 0 &&
                        totalItemCount <= (lastVisibleItem + visibleThreshold) &&
                        onLoadMoreListener != null && NetworkUtils.isNetworkAvailable()) {
                    // get new data.
                    onLoadMoreListener.onLoadMore();
                    loading = true;
                }
            }
        });
    }

    /**
     * we use @isSearchAdapter flag to indicate that the adapter is being used for search view.
     * SEARCH_ITEM is meant to used for search view
     * VIEW_ITEM is meant to be used by main screen character's adapter.
     * VIEW_PROG is used to display load more item.
     * @param position item position
     * @return the view type that will be rendered depending on some factors
     */
    @Override
    public int getItemViewType(int position) {
        if (mCharactersList.isEmpty())
            return VIEW_ITEM;
        else {
            if (isSearchAdapter) {
                return SEARCH_ITEM;
            } else if (mCharactersList.get(position) != null) {
                return VIEW_ITEM;
            } else {
                return VIEW_PROG;
            }
        }
    }


    /**
     * Add temp item in the list to display progress view at the end of the list
     */
    void addLoadingItem() {
        if (mCharactersList.get(mCharactersList.size() - 1) != null) {
            mCharactersList.add(null);
            notifyItemInserted(mCharactersList.size() - 1);
        }
    }

    /**
     * remove loading layout after the data has been fetched
     */
    void removeLoadingItem() {
        mCharactersList.remove(mCharactersList.size() - 1);
        notifyItemRemoved(mCharactersList.size());
    }


    void setOnLoadMoreListener(CharactersContract.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * Very important to disable multiple calls when the recycler reaches the last position.
     */
    void setLoaded() {
        loading = false;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_character, parent, false);
            vh = new CharactersViewHolder(v);
        } else if (viewType == SEARCH_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_search, parent, false);
            vh = new CharactersSearchViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_load_more, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof CharactersViewHolder || holder instanceof CharactersSearchViewHolder) {
            holder.bindViews(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mCharactersList != null && !mCharactersList.isEmpty()) {
            return mCharactersList.size();
        } else {
            return 0;
        }
    }

    void updateList(List<Character> charactersList) {
        // reverse list if it's more items adding to the current list
        // because we're retrieving last 20 elements from the db DESC
        if (!this.mCharactersList.isEmpty()) {
            Collections.reverse(charactersList);
        }
        if (charactersList != null && !charactersList.isEmpty()) {
            mCharactersList.addAll(charactersList);
            notifyDataSetChanged();
        }
    }

    private void displayImage(String imageUrl, ImageView imageView, @Nullable RequestOptions requestOptions) {
        if (requestOptions == null) {
            Glide.with(mContext).load(imageUrl).error(R.drawable.placeholder).into(imageView);
        } else {
            Glide.with(mContext).load(imageUrl).apply(requestOptions).error(R.drawable.placeholder).into(imageView);
        }
    }

    private void displayGif(String imageUrl, ImageView imageView, @Nullable RequestOptions requestOptions) {
        if (requestOptions == null) {
            Glide.with(mContext).asGif().load(imageUrl).error(R.drawable.placeholder).into(imageView);
        } else {
            Glide.with(mContext).asGif().load(imageUrl).apply(requestOptions).error(R.drawable.placeholder).into(imageView);
        }
    }

    void clear() {
        mCharactersList.clear();
    }

    public static class ProgressViewHolder extends BaseViewHolder {

        ProgressViewHolder(View v) {
            super(v);
        }

        @Override
        public void clear() {
            // no imp needed
        }
    }

    protected class CharactersViewHolder extends BaseViewHolder {
        @BindView(R.id.thumbnail_img_view)
        ImageView characterImgView;
        @BindView(R.id.title_txt_view)
        TextView characterTitle;


        CharactersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            Character character = mCharactersList.get(position);
            characterTitle.setText(character.getName());
            if (character.getThumbnail() != null && character.getThumbnail().getPath() != null
                    && !character.getThumbnail().getExtension().isEmpty()) {
                if (character.getThumbnail().getPath().equalsIgnoreCase("gif")) {
                    displayGif(character.getThumbnail().getImageURL(), characterImgView, null);
                } else {
                    displayImage(character.getThumbnail().getImageURL(), characterImgView, null);
                }
            } else {
                Glide.with(itemView.getContext()).load(R.drawable.placeholder).into(characterImgView);
            }

            itemView.setOnClickListener(v -> onCharacterClickListener.onCharacterClick(character, characterImgView, characterTitle));
        }


        @Override
        public void clear() {
            characterTitle.setText("");
            characterImgView.setImageDrawable(null);
        }
    }

    protected class CharactersSearchViewHolder extends BaseViewHolder {
        @BindView(R.id.character_iv)
        ImageView characterImgView;
        @BindView(R.id.title_tv)
        TextView characterTitleTxtView;


        CharactersSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            Character character = mCharactersList.get(position);
            characterTitleTxtView.setText(character.getName());
            // display image/gif
            if (character.getThumbnail() != null && character.getThumbnail().getPath() != null
                    && !character.getThumbnail().getExtension().isEmpty()) {
                if (character.getThumbnail().getPath().equalsIgnoreCase("gif")) {
                    displayGif(character.getThumbnail().getImageURL(), characterImgView, requestOptions);
                } else {
                    displayImage(character.getThumbnail().getImageURL(), characterImgView, requestOptions);
                }
            } else {
                Glide.with(itemView.getContext()).load(R.drawable.placeholder).into(characterImgView);
            }

            itemView.setOnClickListener(v -> onCharacterClickListener.onCharacterClick(character, characterImgView, characterTitleTxtView));
        }


        @Override
        public void clear() {
            characterTitleTxtView.setText("");
            characterImgView.setImageDrawable(null);
        }
    }
}
