package com.example.ahmed.mrhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class NewUserActivity extends AppCompatActivity {


    ArrayList<UserData> userList;
    ListView user_listview;
    String token;
    UserCustomAdapter adapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);


        sharedPreferences = getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        // get the token of the user from sharedPreferences
        token =sharedPreferences.getString(MainActivity.KEY_TOKEN,"not found token");

        // get the result from setting activity
        Intent intent = getIntent();
        String jsonData = intent.getStringExtra("JsonNewUser");

        getDatafromJSON(jsonData);

        // adapter get the data from array
        // and put it in listView
        adapter= new UserCustomAdapter(this,userList,token);
        user_listview = (ListView) findViewById(R.id.listview_newusersList);
        user_listview.setAdapter(adapter);



    }

    // get the data of the users from json data
    private void getDatafromJSON(String jsonData) {

        userList = new ArrayList<UserData>();

        try {

            JSONObject json = new JSONObject(jsonData);

              for(int i=0; i<json.length();i++){


               JSONObject  object =json.getJSONObject(String.valueOf(i));
                // get id , username , email , is_admin , is_approved
                String id = object.getString("id");
                String username = object.getString("username");
                String email = object.getString("email");
                String isAdmin= object.getString("is_admin");
                String isApproved = object.getString("is_approved");

                // add the in the list userList
                userList.add(new UserData(id,username,email,isAdmin,isApproved));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
