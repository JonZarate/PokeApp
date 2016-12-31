package com.jonzarate.pokeapp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonzarate.pokeapp.R;
import com.jonzarate.pokeapp.model.Pokemon;
import com.jonzarate.pokeapp.tools.Tools;

import java.util.ArrayList;

/**
 * Adapter for the Pokemon list
 * Created by JonZarate on 31/12/2016.
 */

public class PokemonListAdapter extends BaseAdapter {

    // Pokemons holder
    private ArrayList<Pokemon> pokemons;

    // Context holder
    private Context context;

    // Inflater holder
    private LayoutInflater inflater;

    public PokemonListAdapter (Context context, ArrayList<Pokemon> pokemons){
        this.pokemons = pokemons;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pokemons.size();
    }

    @Override
    public Object getItem(int position) {
        return pokemons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pokemons.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflate the layout that shows information of a single pokemon
        View pokemonView = inflater.inflate(R.layout.layout_pokemon, null);

        // Search components
        TextView tvPokemonId = (TextView) pokemonView.findViewById(R.id.tvPokemonId);
        TextView tvPokemonName = (TextView) pokemonView.findViewById(R.id.tvPokemonName);
        TextView tvPokemonHeight = (TextView) pokemonView.findViewById(R.id.tvPokemonHeight);
        TextView tvPokemonWeight = (TextView) pokemonView.findViewById(R.id.tvPokemonWeight);

        ImageView ivPokemonImage = (ImageView) pokemonView.findViewById(R.id.ivPokemonImage);

        // Get the corresponding pokemon of the view
        Pokemon pokemon = pokemons.get(position);

        // Set values to the view
        tvPokemonId.setText(String.valueOf(pokemon.getId()));
        tvPokemonName.setText(pokemon.getName());
        tvPokemonHeight.setText(String.valueOf(pokemon.getHeight()));
        tvPokemonWeight.setText(String.valueOf(pokemon.getWeight()));

        // Get the image path to load the image
        String imagePath = pokemon.getImageLocalPath();
        Bitmap pokemonImage = Tools.loadBitmapFromLocalStorage(context, imagePath);

        if (pokemonImage != null){
            ivPokemonImage.setImageBitmap(pokemonImage);
        }

        return pokemonView;
    }
}
