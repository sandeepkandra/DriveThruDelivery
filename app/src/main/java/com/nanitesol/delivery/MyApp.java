package com.nanitesol.delivery;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by sandeep on 8/25/2015.
 */
public class MyApp extends Application {

    static int checkLDT=2;
    static RequestQueue reqstQ;
    public static final String PREFS_NAME = "MyPrefsFile";

    static SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();

        reqstQ= Volley.newRequestQueue(getApplicationContext());
        sharedPreferences=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
