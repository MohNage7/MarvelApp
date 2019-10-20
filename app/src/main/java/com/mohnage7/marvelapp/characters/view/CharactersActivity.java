package com.mohnage7.marvelapp.characters.view;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mohnage7.marvelapp.R;
import com.mohnage7.marvelapp.base.BaseActivity;
import com.mohnage7.marvelapp.characters.CharactersContract;
import com.mohnage7.marvelapp.characters.model.Character;
import com.mohnage7.marvelapp.characters.viewmodel.CharactersViewModel;
import com.mohnage7.marvelapp.details.view.CharacterDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.mohnage7.marvelapp.details.view.CharacterDetailsActivity.CHARACTER;
import static com.mohnage7.marvelapp.utils.Constants.DEFAULT_OFFSET;

public class CharactersActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, CharactersContract.OnCharacterClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView charactersRecyclerView;
    @BindView(R.id.search_recycler_view)
    RecyclerView searchRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_data_layout)
    RelativeLayout noDataLayout;
    @BindView(R.id.no_data_tv)
    TextView noDataTxtView;
    @BindView(R.id.no_search_data_tv)
    TextView noSearchDataTxtView;
    @BindView(R.id.shimmer_loading_layout)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.search_card_view)
    CardView searchLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CharactersViewModel charactersViewModel;
    private CharactersAdapter charactersAdapter;

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
        // set swipe listener
        swipeRefreshLayout.setOnRefreshListener(this);
        // init view model
        charactersViewModel = ViewModelProviders.of(this).get(CharactersViewModel.class);
        // init adapter and recycler
        setupCharactersRecycler();
        // load characters from network or db source
        getCharacters();
        // listen to search data
        charactersViewModel.search().observe(this, dataWrapper -> {
            switch (dataWrapper.status) {
                case LOADING:
                    showSearchLoading();
                    break;
                case ERROR:
                    handleSearchError(dataWrapper.message);
                    break;
                case SUCCESS:
                    setupSearchAdapter(dataWrapper.data);
                    break;
                default:
                    break;
            }
        });
    }

    private void setupToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void showSearchLoading() {
        searchLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        noSearchDataTxtView.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        EditText searchEditText = searchView.findViewById(R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.soft_grey));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setQueryHint(getString(R.string.hint_search));
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() > 1) {
                    searchView.clearFocus();
                    searchInMovies(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchLayout.setVisibility(View.GONE);
                searchView.onActionViewExpanded();
                return true;
            }
        });
        return true;
    }


    private void getCharacters() {
        charactersViewModel.getCharactersList().observe(this, dataWrapper -> {
            switch (dataWrapper.status) {
                case LOADING:
                    if (charactersViewModel.isLoadMore()) {
                        charactersAdapter.addLoadingItem();
                    } else {
                        showLoadingLayout();
                    }
                    break;
                case SUCCESS:
                    if (charactersViewModel.isLoadMore()) {
                        charactersAdapter.removeLoadingItem();
                    } else {
                        hideLoadingLayout();
                    }
                    updateCharactersList(dataWrapper.data);
                    break;
                case ERROR:
                    hideLoadingLayout();
                    handleMoviesListError(dataWrapper.message);
                    break;
                default:
                    break;
            }
        });
        charactersViewModel.setOffset(DEFAULT_OFFSET, false);
    }

    private void updateCharactersList(List<Character> charactersList) {
        setDataViewsVisibility(true);
        charactersAdapter.setLoaded();
        charactersAdapter.updateList(charactersList);
    }

    private void searchInMovies(String query) {
        charactersViewModel.setSearchBy(query);
    }

    private void showLoadingLayout() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        charactersRecyclerView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
    }

    private void hideLoadingLayout() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        charactersRecyclerView.setVisibility(View.VISIBLE);
    }


    private void setDataViewsVisibility(boolean dataAvailable) {
        if (dataAvailable) {
            charactersRecyclerView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        } else {
            charactersRecyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }


    private void handleMoviesListError(String message) {
        setDataViewsVisibility(false);
        noDataTxtView.setText(message);
    }

    private void handleSearchError(String message) {
        setSearchViewsVisibility(false);
        noSearchDataTxtView.setText(message == null || message.isEmpty() ? getString(R.string.no_data_found) : message);
    }

    private void setSearchViewsVisibility(boolean dataAvailable) {
        searchLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (dataAvailable) {
            searchRecyclerView.setVisibility(View.VISIBLE);
            noSearchDataTxtView.setVisibility(View.GONE);
        } else {
            searchRecyclerView.setVisibility(View.GONE);
            noSearchDataTxtView.setVisibility(View.VISIBLE);
        }
    }

    private void setupCharactersRecycler() {
        List<Character> charactersList = new ArrayList<>();
        charactersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        charactersAdapter = new CharactersAdapter(charactersList, this, charactersRecyclerView,this);
        charactersRecyclerView.setAdapter(charactersAdapter);
        charactersAdapter.setOnLoadMoreListener(new CharactersContract.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                charactersViewModel.setOffset(charactersAdapter.getItemCount(), true);
            }
        });
    }

    private void setupSearchAdapter(List<Character> characterList) {
        setSearchViewsVisibility(true);
        CharactersAdapter adapter = new CharactersAdapter(characterList, this,this,true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        charactersAdapter.clear();
        charactersViewModel.setOffset(DEFAULT_OFFSET,false);
    }

    @Override
    public void onCharacterClick(Character character, ImageView imageView,TextView textView) {
        Intent intent = new Intent(this, CharacterDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CHARACTER, character);
        intent.putExtras(bundle);
        Pair<View, String> p1 = Pair.create(imageView, getString(R.string.character_image));
        Pair<View, String> p2 = Pair.create(textView, getString(R.string.title));
        ActivityOptions  activityOptionsCompat = ActivityOptions.makeSceneTransitionAnimation(this,p1,p2);
        startActivity(intent,activityOptionsCompat.toBundle());
    }


}
