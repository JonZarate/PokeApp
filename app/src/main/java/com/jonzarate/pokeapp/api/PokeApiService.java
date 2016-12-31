package com.jonzarate.pokeapp.api;

import com.jonzarate.pokeapp.model.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Pokemon Api urls for Retrofit
 * Created by JonZarate on 30/12/2016.
 */

public interface PokeApiService {

    // Api call to get a single pokemon data
    @GET("pokemon/{id}")
    Call<Pokemon> getPokemon(@Path("id") int pokemonId);

}
