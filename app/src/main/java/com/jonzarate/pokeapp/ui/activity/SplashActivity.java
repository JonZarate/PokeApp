package com.jonzarate.pokeapp.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.jonzarate.pokeapp.R;
import com.jonzarate.pokeapp.api.PokeApiService;
import com.jonzarate.pokeapp.database.DataBaseManager;
import com.jonzarate.pokeapp.model.ApiInstance;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Shows the Splash screen while initialization is being carried out
 */
public class SplashActivity extends AppCompatActivity {

    // Screen components
    private TextView tvInitializationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout to the screen
        setContentView(R.layout.activity_splash);

        // Search for screen components
        tvInitializationStatus = (TextView) findViewById(R.id.tvInitializationStatus);

        // Start initialization
        new InitializeTask().execute();
    }


    private void launchPokeApp (){
        // Launch next Activity
        Intent intent = new Intent(SplashActivity.this, PokeAppActivity.class);

        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private class InitializeTask extends AsyncTask<Void, String, Integer[]> {

        // Initialization results
        private final int OK = 0;
        private final int ERR_NETWORK = 1;
        private final int ERR_DATABASE = 2;

        @Override
        protected void onProgressUpdate(String... values) {

            if (values.length > 0) {
                tvInitializationStatus.setText(values[0]);
            }
        }

        @Override
        protected Integer[] doInBackground(Void... params) {

            Integer result[] = {OK, OK};

            // Initialize debugging tools
            Stetho.initializeWithDefaults(SplashActivity.this);

            // Check Internet status
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isConnectedOrConnecting()){
                result[0] = ERR_NETWORK;
            }

            // Initialize Retrofit
            publishProgress(getString(R.string.text_initializing_retrofit));

            ApiInstance.retrofit = new Retrofit.Builder()
                    .baseUrl("http://pokeapi.co/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInstance.pokeApiService = ApiInstance.retrofit.create(PokeApiService.class);

            // Initialize Database
            publishProgress(getString(R.string.text_initializing_database));

            if (!DataBaseManager.initializeDataBase(SplashActivity.this)){
                result[1] = ERR_DATABASE;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer results[]) {

            // Safe check. There must be 2 results
            if (results != null && results.length == 2){
                int resultNetwork = results[0];
                int resultDatabase = results[1];

                // Check if initialization was correct
                if (resultNetwork == OK && resultDatabase == OK){
                    launchPokeApp();

                } else {
                    // There has been at least 1 error. Show them
                    String errors = "";
                    int errorAmount = 0;

                    for (int result : results) {
                        switch (result) {
                            case OK:
                                // Do nothing
                                break;

                            case ERR_NETWORK:
                                errorAmount++;
                                errors += getString(R.string.text_error_network) + "\n";
                                break;

                            case ERR_DATABASE:
                                errorAmount++;
                                errors += getString(R.string.text_error_database) + "\n";
                                break;

                            default:
                                errorAmount++;
                                errors += getString(R.string.text_error_unknown) + "\n";
                                break;
                        }
                    }

                    // Create dialog to show errors
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle(errorAmount + getString(R.string.text_errors_found));
                    builder.setMessage(errors);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            launchPokeApp();
                        }
                    });

                    // Display dialog with error messages
                    builder.create().show();

                }
            }
        }
    }
}
