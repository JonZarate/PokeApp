package com.jonzarate.pokeapp.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jonzarate.pokeapp.BuildConfig;
import com.jonzarate.pokeapp.R;
import com.jonzarate.pokeapp.ui.adapter.PokemonPagerAdapter;

/**
 * Main screen of the Application. Shows all the fragments within the ViewPager
 */
public class PokeAppActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_app);

        // Search screen components
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set up the only component - the PagerView
        PokemonPagerAdapter pagerAdapter = new PokemonPagerAdapter(PokeAppActivity.this);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
