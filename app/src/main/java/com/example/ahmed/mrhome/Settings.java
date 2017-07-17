package com.example.ahmed.mrhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Settings extends AppCompatActivity {

    String token;
    String [] setting_list ={"New Users","Admins","All Users","Log Out"};
    ListView setting_listview;
    String isAdmin="0";
    String email_u;
    TextView tv_error;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tv_error = (TextView) findViewById(R.id.error_user);
        sharedPreferences = getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);

        // get the token of the user from sharedPreferences
        token =sharedPreferences.getString(MainActivity.KEY_TOKEN,"not found token");

        // get the email of the user who login
        email_u = sharedPreferences.getString(MainActivity.KEY_EMAIL,"not found email");
        Log.i("email_user",email_u);

        // get the data of all admins in the database
        // we will this data to check if the user is admin or not
        // by check the email of the user with emails of all admins
        GetAllAdmins(MainActivity.URL+"/mobile/user.php?token="+token,"admin","");


    }



    private void Selectitem(int position) {

        switch (position){
            case 0:{
                // get data of new users
                GetUserData(MainActivity.URL+"/mobile/user.php?token="+token,"new","");
                break;
            }
            case 1:{
                //get data of all admins
                GetUserData(MainActivity.URL+"/mobile/user.php?token="+token,"admin","");
                break;
            }
            case 2:{
                // get data of all users (new , admins)
                GetUserData(MainActivity.URL+"/mobile/user.php?token="+token,"all","");
                break;
            }
            case 3:{
                //log out
                // go to login page
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                break;
            }

        }
    }



    //(Url , type (new ,admin,all) , id of the user)
    public void GetAllAdmins(String uri,String type,String id){
        RequestPackage p = new RequestPackage();
        p.setMethod("GET");
        p.setUri(uri);
        p.setParam("type",type);
        p.setParam("id",id);

        // execute AsyncTask
        GetAdminsTask getAdminsTask= new GetAdminsTask();
        getAdminsTask.execute(p);


    }

    // class AsyncTask is used to perform background operations and publish results on the UI thread
    // operations like connect to the server or download files
    private class GetAdminsTask extends AsyncTask<RequestPackage,Void,String>{

        String ResponseCode;

        // this function executes until doInBackground executes its operation
        // it will show progress dialog with loading message  until doInBackground finishes its operation
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Settings.this);
            progressDialog.setMessage("loading..");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        // this function is responsible to do background operations
        // connect to the server , send data and receive results
        @Override
        protected String doInBackground(RequestPackage... params) {
            String data = getData(params[0]);
            // this data variable it has the result from the background operation
            // this is the result and it will send to onPostExecute() function
            return data;
        }

        // this function will execute after doInBackground
        //The result of the background computation is passed to this step as a parameter.
        //publish results on the UI thread
        @Override
        protected void onPostExecute(String result) {

            // dismiss the progress dialog is still showing
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            Log.i("admin data", result);

            // get the data of all admins from json data
            //that we receive from the server
            try {
                JSONObject jsonObject = new JSONObject(result);

                    for (int i = 0; i < jsonObject.length(); i++) {

                        JSONObject object = jsonObject.getJSONObject(String.valueOf(i));
                        String email = object.getString("email");

                        // check if the email of the user is equal to email of admins in database
                        if (email.equals(email_u)) {
                            isAdmin = "1";
                            Log.i("isAdmin", isAdmin);
                            // if he is an admin he can access the activity
                            // he can view all users and admins
                            //he can approve users , delete users
                            setting_listview = (ListView) findViewById(R.id.setting_list);
                            setting_listview.setAdapter(new SettingCustomAdapter(Settings.this, setting_list));
                            setting_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Selectitem(position);
                                }
                            });
                            break;

                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                 // if he is not an admin
                if(!isAdmin.equals("1")){
                    tv_error.setText("You are not an Admin to view this page");
                }

        }

        public  String getData(RequestPackage p){

            //this function are used to connect to the server and send data
            // receive data
            BufferedReader reader =null;
            String uri =p.getUri();

            if(p.getMethod().equals("GET")){
                uri += "&" + p.getEncodedParams();
            }

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(p.getMethod());


                Log.i("Internet Request" , String.valueOf(con.getResponseCode()));
                Log.i("URL " , uri);


                ResponseCode = String.valueOf(con.getResponseCode());

                StringBuilder sb =  new StringBuilder();
                // receive the result
                // put in BufferedReader object to read it
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line=reader.readLine())!=null){
                    sb.append(line + "\n");
                }
                // return the result and passes to onPostExecute() function
                return sb.toString();


            } catch (Exception e) {
                return ResponseCode;
                //close connection in finally
            }finally {
                if(reader !=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }


        }


    }



    //(Url , type (new ,admin,all) , id of the user)
    public void GetUserData(String uri,String type,String id){
        RequestPackage p = new RequestPackage();
        p.setMethod("GET");
        p.setUri(uri);
        p.setParam("type",type);
        p.setParam("id",id);

        if(type.equals("new")){
            GetNewUser gettask= new GetNewUser();
            gettask.execute(p);
        }

        if(type.equals("admin")){
            GetAdmin getadmin= new GetAdmin();
            getadmin.execute(p);
        }

        if(type.equals("all")){
            GetAlluser getall= new GetAlluser();
            getall.execute(p);
        }



    }


    private class GetNewUser extends AsyncTask<RequestPackage,Void,String>{


        @Override
        protected String doInBackground(RequestPackage... params) {
            String data = Utility.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            // it will go to new user activity
            // we will pass the result to it
            // we will display the result there
            Intent intent = new Intent(Settings.this,NewUserActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("JsonNewUser",result);
            startActivity(intent);

        }
    }




    private class GetAdmin extends AsyncTask<RequestPackage,Void,String>{


        @Override
        protected String doInBackground(RequestPackage... params) {
            String data = Utility.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            // it will go to new user activity
            // we will pass the result to it
            // we will display the result there
            Intent intent = new Intent(Settings.this,AdminActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("JsonNewUser",result);
            startActivity(intent);

        }
    }


    private class GetAlluser extends AsyncTask<RequestPackage,Void,String>{


        @Override
        protected String doInBackground(RequestPackage... params) {
            String data = Utility.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            // it will go to new user activity
            // we will pass the result to it
            // we will display the result there
            Intent intent = new Intent(Settings.this,AllUsersActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("JsonNewUser",result);
            startActivity(intent);

        }
    }











}
