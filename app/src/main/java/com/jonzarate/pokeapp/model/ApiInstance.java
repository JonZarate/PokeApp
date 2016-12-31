package com.jonzarate.pokeapp.model;

import com.jonzarate.pokeapp.api.PokeApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Stored Retrofit instance to use in the App and provide an easy usage
 * Created by JonZarate on 30/12/2016.
 */

public class ApiInstance {

    public static Retrofit retrofit;

    public static PokeApiService pokeApiService;

}
