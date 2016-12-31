package com.jonzarate.pokeapp.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model to store Pokemon data from web service
 * Created by JonZarate on 30/12/2016.
 */

public class Pokemon {

    // Attributes
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("height")
    private int height;

    @Expose
    @SerializedName("weight")
    private int weight;

    @Expose
    @SerializedName("sprites")
    private Sprite sprite;

    private Bitmap image;
    private String imageLocalPath;

    // Invisible not visible class outside this class. Use getters
    private class Sprite {

        @Expose
        @SerializedName("front_shiny")
        private String imageUrl;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getImageUrl(){
        if (sprite != null){
            return sprite.imageUrl;
        }
        return null;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getImageLocalPath () {
        return imageLocalPath;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setImageLocalPath(String imagePath){
        this.imageLocalPath = imagePath;
    }
}
