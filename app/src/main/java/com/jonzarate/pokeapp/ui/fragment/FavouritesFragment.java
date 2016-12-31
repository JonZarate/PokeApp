package com.jonzarate.pokeapp.ui.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.jonzarate.pokeapp.R;
import com.jonzarate.pokeapp.database.DataBaseManager;
import com.jonzarate.pokeapp.model.Pokemon;
import com.jonzarate.pokeapp.ui.adapter.PokemonListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements View.OnClickListener{

    // List holder to be able to refresh
    private ListView lvPokemonList;

    // Instance to give to PagerView
    private static FavouritesFragment favouritesFragment;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment getInstance(){
        if (favouritesFragment == null){
            favouritesFragment = new FavouritesFragment();
        }

        return favouritesFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        // Search screen views
        lvPokemonList = (ListView) rootView.findViewById(R.id.lvPokemonList);
        Button btnRefresh = (Button) rootView.findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Load every time the fragment is restored
        new LoadPokemonsTask().execute();
    }

    @Override
    public void onClick(View v) {

        // Check click origin
        switch (v.getId()){

            case R.id.btnRefresh:
                // Reload Pokemons from database
                new LoadPokemonsTask().execute();
                break;

        }

    }

    private class LoadPokemonsTask extends AsyncTask<Void, String, PokemonListAdapter> {

        private ProgressDialog dialog;

        @Override
        protected void onProgressUpdate(String... values) {

            if (values.length > 0){
                dialog.setMessage(values[0]);
            }
        }

        @Override
        protected void onPreExecute() {
            // Block UI with a dialog
            dialog = new ProgressDialog(FavouritesFragment.this.getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected PokemonListAdapter doInBackground(Void... params) {

            // Load the Pokemons from the database
            publishProgress(getString(R.string.text_favourites_loading));

            ArrayList<Pokemon> pokemons = DataBaseManager.readAllPokemonsFromDatabase();

            // Create the adapter for the ListView
            publishProgress(getString(R.string.text_favourites_listing));

            PokemonListAdapter listAdapter = new PokemonListAdapter(getContext(), pokemons);

            return listAdapter;
        }

        @Override
        protected void onPostExecute(PokemonListAdapter listAdapter) {

            // Set the adapter to the listview
            lvPokemonList.setAdapter(listAdapter);

            //Hide UI blocking dialog
            dialog.dismiss();
        }
    }
}
