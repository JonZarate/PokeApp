package com.jonzarate.pokeapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jonzarate.pokeapp.R;
import com.jonzarate.pokeapp.ui.fragment.AuthorFragment;
import com.jonzarate.pokeapp.ui.fragment.ExploreFragment;
import com.jonzarate.pokeapp.ui.fragment.FavouritesFragment;

/**
 * An Adapter for the Fragments displayed within the ViewPager
 * Created by JonZarate on 30/12/2016.
 */

public class PokemonPagerAdapter extends FragmentStatePagerAdapter {

    private AppCompatActivity activity;

    public PokemonPagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());

        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ExploreFragment.getInstance();
            case 1:
                return FavouritesFragment.getInstance();
            case 2:
                return AuthorFragment.getInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return activity.getString(R.string.tab_explore);
            case 1:
                return activity.getString(R.string.tab_favourites);
            case 2:
                return activity.getString(R.string.tab_author);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}