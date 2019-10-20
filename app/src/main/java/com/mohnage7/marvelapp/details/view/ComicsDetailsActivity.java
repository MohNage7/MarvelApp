package com.mohnage7.marvelapp.details.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.mohnage7.marvelapp.R;
import com.mohnage7.marvelapp.details.model.Comic;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Math.abs;

public class ComicsDetailsActivity extends AppCompatActivity {

    public static final String COMIC_LIST = "COMIC_LIST";
    public static final String COMIC_POSITION = "position";

    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.count_tv)
    TextView countTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics_details);
        ButterKnife.bind(this);
        // get list from intent extras.
        List<Comic> comicList = getComicList();
        // get current item that will be displayed at first in the view pager.
        int currentItem = getCurrentItem();
        setupViewpager(currentItem,comicList);
    }

    private int getCurrentItem() {
        return getIntent().getIntExtra(COMIC_POSITION,0);
    }

    private void setupViewpager(int currentItem ,List<Comic> comicList) {
        ComicsViewPager comicsViewPager = new ComicsViewPager(comicList,this);
        viewPager.setAdapter(comicsViewPager);
        // set selected item
        viewPager.setCurrentItem(currentItem);
        // You need to retain one page on each side so that the next and previous items are visible
        viewPager.setOffscreenPageLimit(1);
        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        int nextItemVisiblePx = (int) getResources().getDimension(R.dimen.viewpager_next_item_visible);
        int currentItemHorizontalMarginPx = (int) getResources().getDimension(R.dimen.viewpager_current_item_horizontal_margin);
        int pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx;
        ViewPager2.PageTransformer pageTransformer = (page, position) -> {
            page.setTranslationX(-pageTranslationX * position);
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.setScaleY(1 - (0.25f * abs(position)));
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        };
        viewPager.setPageTransformer(pageTransformer);
        viewPager.addItemDecoration(new HorizontalMarginItemDecoration(this,R.dimen.viewpager_current_item_horizontal_margin));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                countTxtView.setText(String.format(Locale.ENGLISH,"%d/%d", position+1, comicList.size()));
            }
        });
    }

    private List<Comic> getComicList() {
        List<Comic> comicList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.hasExtra(COMIC_LIST)){
            comicList = intent.getParcelableArrayListExtra(COMIC_LIST);
        }
        return comicList;
    }

    @OnClick(R.id.close_btn)
    public void finishActivity(){
        finish();
    }

}
