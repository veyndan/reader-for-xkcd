package com.example.veyndan.readerforxkcd.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.fragment.BaseFragment;
import com.example.veyndan.readerforxkcd.fragment.HomeFragment;
import com.example.veyndan.readerforxkcd.service.XkcdService;
import com.example.veyndan.readerforxkcd.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(MainActivity.class);

    private static final int PAGE_NUMBER = 1;

    private List<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XkcdService.startActionDownload(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(null);
        }

        fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setTabIcons(tabLayout, R.array.icons);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                RecyclerView recyclerView = fragments.get(tab.getPosition()).getRecyclerView();
                if (recyclerView != null) {
                    if (recyclerView.computeVerticalScrollOffset() > 25000) {
                        recyclerView.scrollToPosition(0);
                    } else {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
            }
        });
    }

    private void setTabIcons(@NonNull TabLayout tabLayout, @ArrayRes int iconDrawables) {
        TypedArray icons = getResources().obtainTypedArray(iconDrawables);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setIcon(icons.getDrawable(i));
            }
        }
        icons.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return PAGE_NUMBER;
        }
    }
}
