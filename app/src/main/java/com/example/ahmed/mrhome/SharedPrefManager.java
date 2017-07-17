package com.example.ahmed.mrhome;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ahmed Orabi on 27/06/2017.
 */

public class SharedPrefManager {

    private static final String Shared_pref_name="fcmsharedpref";
    private static final String KEY_ACCESS_TOKEN="token";


    private static Context context;
    private static SharedPrefManager mInstance;

    public SharedPrefManager(Context context){
        this.context=context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance==null){
            mInstance = new SharedPrefManager(context);
        }

        return  mInstance;
    }


    // store FireBase token in sharedPreferences
    public boolean storeToken(String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Shared_pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;

    }

    // get FireBase token
    public String getToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,"Not Found FCM Token");
    }





}
