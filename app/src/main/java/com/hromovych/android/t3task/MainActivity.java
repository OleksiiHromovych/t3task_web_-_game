package com.hromovych.android.t3task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String GET_SHARED_PREFERENCES = "com.hromovych.android.t3task";
    public static final String FIRST_RUN_PREFERENCES = "first_run";
    public static final String IS_INTERNET_PREFERENCES = "internet access";

    private Boolean isInternetAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreferences = getSharedPreferences(GET_SHARED_PREFERENCES,
                MODE_PRIVATE);
        if (mSharedPreferences.getBoolean(FIRST_RUN_PREFERENCES, true)) {
            mSharedPreferences.edit().putBoolean(IS_INTERNET_PREFERENCES, isNetworkAvailable()).apply();
            mSharedPreferences.edit().putBoolean(FIRST_RUN_PREFERENCES, false).apply();
        }
//        isInternetAccess = mSharedPreferences.getBoolean(IS_INTERNET_PREFERENCES, true);
isInternetAccess = false;
        if (isInternetAccess){
            startActivity(BrowserActivity.newIntent(this, "http://html5test.com"));
        } else
            startActivity(GameActivity.newIntent(this));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}