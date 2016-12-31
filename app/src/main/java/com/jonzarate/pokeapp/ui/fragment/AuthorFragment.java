package com.jonzarate.pokeapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonzarate.pokeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorFragment extends Fragment {

    // Instance to give to PagerView
    private static AuthorFragment authorFragment;

    public AuthorFragment() {
        // Required empty public constructor
    }

    public static AuthorFragment getInstance(){
        if (authorFragment == null){
            authorFragment = new AuthorFragment();
        }

        return authorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_author, container, false);
    }

}
