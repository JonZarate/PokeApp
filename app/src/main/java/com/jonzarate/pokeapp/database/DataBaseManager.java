package com.jonzarate.pokeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jonzarate.pokeapp.model.Pokemon;

import java.util.ArrayList;

/**
 * Offers methods related to the database
 * Created by JonZarate on 30/12/2016.
 */

public class DataBaseManager {

    private static PokemonDataBase pokemonDataBase;

    public static boolean initializeDataBase(Context context){
        // Create the pokemonDataBase instance
        pokemonDataBase = new PokemonDataBase(context);

        // Check if we can read & write in the pokemonDataBase
        SQLiteDatabase readableDatabase = pokemonDataBase.getReadableDatabase();
        if (readableDatabase == null){
            return false;
        }
        readableDatabase.close();

        SQLiteDatabase writableDatabase = pokemonDataBase.getWritableDatabase();
        if (writableDatabase == null){
            return false;
        }
        writableDatabase.close();

        return true;
    }

    public static long savePokemonOnDatabase (Pokemon pokemon){

        // Result holder
        long rowsAffected = 0;

        // Safe check
        if (pokemon != null){
            // Create that ContentValues to store them
            ContentValues values = new ContentValues();
            values.put(PokemonDataBase.Pokemon.API_ID, pokemon.getId());
            values.put(PokemonDataBase.Pokemon.NAME, pokemon.getName());
            values.put(PokemonDataBase.Pokemon.HEIGHT, pokemon.getHeight());
            values.put(PokemonDataBase.Pokemon.WEIGHT, pokemon.getWeight());
            values.put(PokemonDataBase.Pokemon.IMAGE_PATH, pokemon.getImageLocalPath());

            SQLiteDatabase writableDatabase = pokemonDataBase.getWritableDatabase();
            try {
                rowsAffected = writableDatabase.insertOrThrow(PokemonDataBase.Pokemon.TABLE_NAME, null, values);
            } catch (Exception e){
                // Could not insert value, already inserted?
            }
        }

        return rowsAffected;
    }

    public static ArrayList<Pokemon> readAllPokemonsFromDatabase (){

        // Declare ArrayList
        ArrayList<Pokemon> pokemons = new ArrayList<>();

        // Get database instance and query for all the results
        SQLiteDatabase readableDatabase = pokemonDataBase.getReadableDatabase();
        Cursor cursor = readableDatabase.query(PokemonDataBase.Pokemon.TABLE_NAME, null, null, null, null, null, null);

        // Check if the database is not empty
        if (cursor != null && cursor.moveToFirst()) {

            // Get columns indexes
            int columnApiId = cursor.getColumnIndex(PokemonDataBase.Pokemon.API_ID);
            int columnName = cursor.getColumnIndex(PokemonDataBase.Pokemon.NAME);
            int columnHeight = cursor.getColumnIndex(PokemonDataBase.Pokemon.HEIGHT);
            int columnWeight = cursor.getColumnIndex(PokemonDataBase.Pokemon.WEIGHT);
            int columnImagePath = cursor.getColumnIndex(PokemonDataBase.Pokemon.IMAGE_PATH);

            do {
                // Declare a new pokemon to save read data
                Pokemon pokemon = new Pokemon();

                // Store data
                pokemon.setId(cursor.getInt(columnApiId));
                pokemon.setName(cursor.getString(columnName));
                pokemon.setHeight(cursor.getInt(columnHeight));
                pokemon.setWeight(cursor.getInt(columnWeight));
                pokemon.setImageLocalPath(cursor.getString(columnImagePath));

                // Save pokemon in ArrayList
                pokemons.add(pokemon);
            } while (cursor.moveToNext());

            // Close the cursor before exiting function
            cursor.close();
        }

        return pokemons;
    }

}
