package com.jonzarate.pokeapp.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class that offers IO functions
 * Created by JonZarate on 31/12/2016.
 */

public class Tools {

    public static boolean saveBitmapInLocalStorage (Context context, Bitmap image, String filename) {

        // Result holder
        boolean result = false;

        // Declare output stream
        FileOutputStream outputStream = null;
        try {
            // Create destination path string
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);

            // Save the image
            result = image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        } catch (Exception e) {
            // Error happened, do nothing, result already set
        } finally {
            // Close stream
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // Do nothing
            }
        }

        return result;
    }

    public static Bitmap loadBitmapFromLocalStorage (Context context, String filename) {

        // Image holder
        Bitmap image = null;

        try {
            // Get full path of the image
            String fullPath = context.getFilesDir() + File.separator + filename;

            // Read the image
            image = BitmapFactory.decodeFile(fullPath);
        }catch (Exception e){
            // Do nothing
        }

        return image;
    }

}