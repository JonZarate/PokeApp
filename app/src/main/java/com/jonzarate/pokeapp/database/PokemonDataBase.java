package com.jonzarate.pokeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages local database that stores Pokemons
 * Created by JonZarate on 30/12/2016.
 */

public class PokemonDataBase extends SQLiteOpenHelper {

    // Database information
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PokeDb.db";

    // Define database content
    interface Pokemon {
        String TABLE_NAME = "pokemon";
        String ID = "id";
        String API_ID = "api_id";
        String NAME = "name";
        String HEIGHT = "height";
        String WEIGHT = "weight";
        String IMAGE_PATH = "image_path";
    }

    // Database queries
    private String SQL_CREATE_TABLE =  "CREATE TABLE " + Pokemon.TABLE_NAME + " (" +
            Pokemon.ID + " INTEGER PRIMARY KEY," +
            Pokemon.API_ID + " INTEGER," +
            Pokemon.NAME + " TEXT," +
            Pokemon.HEIGHT + " INTEGER," +
            Pokemon.WEIGHT + " INTEGER," +
            Pokemon.IMAGE_PATH + " TEXT)";

    private String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + Pokemon.TABLE_NAME;

    public PokemonDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        // Create the database
        db.execSQL(SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete database and create again
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}