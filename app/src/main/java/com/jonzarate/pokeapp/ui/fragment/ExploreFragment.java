package com.jonzarate.pokeapp.ui.fragment;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jonzarate.pokeapp.R;
import com.jonzarate.pokeapp.database.DataBaseManager;
import com.jonzarate.pokeapp.model.ApiInstance;
import com.jonzarate.pokeapp.model.Pokemon;
import com.jonzarate.pokeapp.tools.Tools;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements View.OnClickListener{

    // Screen views
    private EditText etPokemonInput;
    private Button btnSearch, btnAddPokemon;

    private TextView tvPokemonId, tvPokemonName, tvPokemonHeight, tvPokemonWeight;
    private ImageView ivPokemonImage;

    // Data holder
    private Pokemon mSearchedPokemon;

    // Instance to give to PagerView
    private static ExploreFragment exploreFragment;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment getInstance(){
        if (exploreFragment == null) {
            exploreFragment = new ExploreFragment();
        }

        return exploreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        // Search screen views
        etPokemonInput = (EditText) view.findViewById(R.id.etExplorePokemonInput);
        btnSearch = (Button) view.findViewById(R.id.btnExploreSearch);
        btnAddPokemon = (Button) view.findViewById(R.id.btnAddPokemon);

        ivPokemonImage = (ImageView) view.findViewById(R.id.ivPokemonImage);
        tvPokemonId = (TextView) view.findViewById(R.id.tvPokemonId);
        tvPokemonName = (TextView) view.findViewById(R.id.tvPokemonName);
        tvPokemonHeight = (TextView) view.findViewById(R.id.tvPokemonHeight);
        tvPokemonWeight = (TextView) view.findViewById(R.id.tvPokemonWeight);

        // Set button
        btnSearch.setOnClickListener(this);
        btnAddPokemon.setOnClickListener(this);
        return view;
    }

    private void setPokemonInViews(Pokemon pokemon){

        // Safe check
        if (pokemon != null) {
            // Set all values on screen
            tvPokemonId.setText(String.valueOf(pokemon.getId()));
            tvPokemonName.setText(pokemon.getName());
            tvPokemonHeight.setText(String.valueOf(pokemon.getHeight()));
            tvPokemonWeight.setText(String.valueOf(pokemon.getWeight()));

            Bitmap pokemonImage = pokemon.getImage();
            if (pokemonImage != null) {
                // Set the image of the pokemon
                ivPokemonImage.setImageBitmap(pokemonImage);

                // Save the Pokemon in case the user wants to store it on the database
                mSearchedPokemon = pokemon;
            } else {
                // The image couldn't be downloaded. Delete current image if any
                ivPokemonImage.setImageDrawable(null);
            }
        }
    }

    @Override
    public void onClick(View clickedView) {

        // Check the source of the view to determine which one was clicked
        switch (clickedView.getId()){

            case R.id.btnExploreSearch:
                searchPokemon();
                break;

            case R.id.btnAddPokemon:
                addPokemonToDatabase();
                break;

            default:
                // Do nothing
                break;
        }
    }

    private void searchPokemon(){

        // Read user input
        String userInput = etPokemonInput.getText().toString();

        try{
            // Parse the text to a number
            Integer pokemonId = Integer.valueOf(userInput);

            // Launch task to load Pokemon data
            new DownloadPokemonTask().execute(pokemonId);

        } catch (Exception e){
            // Display error details
            String errorMessage = getString(R.string.text_error_parse )+ "\n" + e.getMessage();
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void addPokemonToDatabase(){

        // Check if the pokemon instance is valid
        if (mSearchedPokemon != null && mSearchedPokemon.getImage() != null){
            // Launch task to save the Pokemon on the database
            new SaveInDatabaseTask().execute(mSearchedPokemon);

        } else {
            // Failed search or not searched yet. Show error message
            Toast.makeText(getContext(), "Make a successful search first",Toast.LENGTH_SHORT).show();
        }

    }


    private class SaveInDatabaseTask extends AsyncTask<Pokemon, String, Integer>{

        // Task result definition
        private final int OK = 0;
        private final int ERR_NULL_VALUE = 1;
        private final int ERR_STORAGE = 2;
        private final int ERR_DATABASE = 3;

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
            dialog = new ProgressDialog(ExploreFragment.this.getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Integer doInBackground(Pokemon... params) {

            // Create result holding variable
            int result = OK;

            // Safe check
            if (params.length == 1){
                // Save pokemon image in local application storage
                publishProgress(getString(R.string.text_explore_save_image));

                Pokemon pokemon = params[0];
                String imageFilename = pokemon.getId() + ".png";

                boolean saveOnDisk = Tools.saveBitmapInLocalStorage(getContext(), pokemon.getImage(), imageFilename);
                if (saveOnDisk) {
                    // Save filename on pokemon instance
                    pokemon.setImageLocalPath(imageFilename);

                    // Save pokemon and its image path on database
                    publishProgress(getString(R.string.text_explore_save_pokemon));

                    long affectedRows = DataBaseManager.savePokemonOnDatabase(pokemon);
                    if (affectedRows > 0){
                        // Saved successfully
                    } else {
                        result = ERR_DATABASE;
                    }
                } else {
                    result = ERR_STORAGE;
                }
            } else {
                result = ERR_NULL_VALUE;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer taskResult) {

            String message;

            // Check task result
            switch (taskResult){

                case OK:
                    message = getString(R.string.text_explore_save_ok);
                    break;

                case ERR_STORAGE:
                    message = getString(R.string.text_explore_save_error_storage);
                    break;

                case ERR_DATABASE:
                    message = getString(R.string.text_explore_save_error_database);
                    break;

                default:
                    message = getString(R.string.text_error_unknown);
            }

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            //Hide UI blocking dialog
            dialog.dismiss();
        }
    }

    private class DownloadPokemonTask extends AsyncTask<Integer, String, Pokemon> {

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
            dialog = new ProgressDialog(ExploreFragment.this.getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Pokemon doInBackground(Integer... params) {

            // Safe check
            if (params.length > 0) {

                // Download Pokemon data using provided id with Retrofit
                publishProgress(getString(R.string.text_downloading_pokemon_data));

                Call<Pokemon> pokemonCall = ApiInstance.pokeApiService.getPokemon(params[0]);
                Response<Pokemon> pokemonResponse = null;

                try {
                    // Attempt to download data
                    pokemonResponse = pokemonCall.execute();
                } catch (IOException e) {
                    // Do nothing. It is managed later
                }

                if (pokemonResponse != null && pokemonResponse.isSuccessful()){
                    Pokemon pokemon = pokemonResponse.body();

                    // Download pokemon image
                    publishProgress(getString(R.string.text_downloading_pokemon_image));

                    String imageUrl = pokemon.getImageUrl();
                    try {
                        Bitmap pokemonImage = Picasso.with(getContext()).load(imageUrl).get();
                        pokemon.setImage(pokemonImage);
                    } catch (IOException e) {
                        // Do nothing. It is managed later
                    }

                    return pokemon;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {

            if (pokemon != null) {
                // Show Pokemon on screen
                setPokemonInViews(pokemon);

            } else {
                // Data couldn't be downloaded, show message and discard previous pokemon info
                Toast.makeText(getContext(), getString(R.string.text_error_network), Toast.LENGTH_SHORT).show();
                mSearchedPokemon = null;
            }
            //Hide UI blocking dialog
            dialog.dismiss();
        }
    }
}
