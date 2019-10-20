package com.mohnage7.marvelapp.details.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mohnage7.marvelapp.R;
import com.mohnage7.marvelapp.base.BaseActivity;
import com.mohnage7.marvelapp.characters.model.Character;
import com.mohnage7.marvelapp.details.DetailsContract;
import com.mohnage7.marvelapp.details.viewmodel.CharacterDetailViewModel;
import com.mohnage7.marvelapp.details.model.Comic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.marvelapp.details.view.ComicsDetailsActivity.COMIC_LIST;
import static com.mohnage7.marvelapp.details.view.ComicsDetailsActivity.COMIC_POSITION;

public class CharacterDetailsActivity extends BaseActivity implements DetailsContract.OnClickListener {

    public static final String CHARACTER = "character";

    @BindView(R.id.title_txt_view)
    TextView titleTxtView;
    @BindView(R.id.character_description)
    TextView descTxtView;
    @BindView(R.id.desc_label_tv)
    TextView descriptionLabelTxtView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.back_drop_iv)
    ImageView backdropImgView;
    @BindView(R.id.shimmer_loading_layout)
    ShimmerFrameLayout shimmerFrameLayout;

    private Character character;
    private CharacterDetailViewModel viewModel;

    @Override
    protected int layoutRes() {
        return R.layout.activity_character_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject views
        ButterKnife.bind(this);
        // init view model
        viewModel = ViewModelProviders.of(this).get(CharacterDetailViewModel.class);
        setupToolbar();
        // get character object
        getMovieFromIntent();
        // fill views with data
        setViews();
    }

    private void setupToolbar() {
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // set status bar color as transparent to be able to see poster below it.
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           onBackPressed();
        }
        return true;
    }

    private void setViews() {
        titleTxtView.setText(character.getName());
        if (character.getDescription()!=null && !character.getDescription().isEmpty()){
            descTxtView.setText(character.getDescription());
            descriptionLabelTxtView.setVisibility(View.VISIBLE);
            descTxtView.setVisibility(View.VISIBLE);
        }else {
            descriptionLabelTxtView.setVisibility(View.GONE);
            descTxtView.setVisibility(View.GONE);
        }
        Glide.with(this).load(character.getThumbnail().getImageURL()).into(backdropImgView);
    }

    private void showLoadingLayout() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
    private void hideLoadingLayout() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * this method gets character object from coming intent
     */
    private void getMovieFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            character = intent.getParcelableExtra(CHARACTER);
            getComics();
        }
    }

    private void getComics() {
        if (character != null) {
            viewModel.getComics(character.getId()).observe(this, dataWrapper -> {
                switch (dataWrapper.status) {
                    case LOADING:
                        showLoadingLayout();
                        break;
                    case SUCCESS:
                        hideLoadingLayout();
                        setupRecycler(dataWrapper.data);
                        break;
                    case ERROR:
                        hideLoadingLayout();
                        handleError(dataWrapper.message);
                        break;
                    default:
                        break;
                }
            });
        } else {
            hideLoadingLayout();
            showToast(R.string.character_load_failure);
            finish();
        }
    }

    private void setupRecycler(List<Comic> comicList) {
        ComicAdapter videoAdapter = new ComicAdapter(comicList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(videoAdapter);
    }

    private void handleError(String message) {
        showToast(message);
    }

    @Override
    public void onBackPressed() {
        // exits activity with image transaction animation
        //overridePendingTransition(R.anim.no_animation, R.anim.anim_slide_down);
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public void onClick(List<Comic> comicList,int position) {
        Intent intent = new Intent(CharacterDetailsActivity.this,ComicsDetailsActivity.class);
        intent.putParcelableArrayListExtra(COMIC_LIST, (ArrayList<? extends Parcelable>) comicList);
        intent.putExtra(COMIC_POSITION,position);
        startActivity(intent);
    }
}
