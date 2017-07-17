package com.example.ahmed.mrhome;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    EditText ed_mail;
    EditText ed_pass;
    TextView error_tv;
    TextView mr_tv;
    TextView tv_signup;
    Button btn_signIn;
    TextView tv_data;
    String token;
    String FCMTOKEN;
    BroadcastReceiver broadcastReceiver;
    ProgressDialog progressDialog;
    JSONObject json;
    SharedPreferences sharedPreferences;
    public static final String KEY_TOKEN="keytoken";
    public static final String PREF_NAME="pref";
    public static final String KEY_EMAIL="keyemail";



    //public static final String URL=" http://connectvia.netii.net";
    //public static final String URL="http://192.168.1.2/smarthome";
     // public static final String URL="http://192.168.43.154/smarthome";
     public static final String URL="http://104.236.106.121/SmartHome";








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sharedPreferences used to store data on the device
        sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        // BroadcastReceiver used to receive FireBase token when generated
        // we will use FireBase token in send notifications
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("TOKENN",SharedPrefManager.getInstance(MainActivity.this).getToken());
            }
        };

        // get FireBase token that stored in sharedPreferences
        FCMTOKEN = SharedPrefManager.getInstance(MainActivity.this).getToken();
        // log is used to display message in console with (Tag , message)
        Log.i("TOKEN",FCMTOKEN);

        // instantiate the objects (views) by getting the id of the view
        mr_tv = (TextView) findViewById(R.id.mr_home);
        btn_signIn = (Button) findViewById(R.id.btn_login);
        ed_mail= (EditText) findViewById(R.id.ed_email);
        ed_pass = (EditText) findViewById(R.id.ed_pass);
        tv_data= (TextView) findViewById(R.id.tv_data);
        error_tv = (TextView) findViewById(R.id.error_tv);
        tv_signup = (TextView) findViewById(R.id.tv_signup);

        //set custom font to TextView mr_tv
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lobster_1.3.otf");
        mr_tv.setTypeface(custom_font);


        // set setOnClickListener to the TextView tv_signup
        // to open activity (Register Activity ) when click on sign up TextView
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent is used to open new activity
                // to go from activity to another activity
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });


        // set setOnClickListener to the Button Log in
        // to send the email , password and firebase token to the sever to let the user log in
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the email from EditText
                String email = ed_mail.getText().toString();
                // get the password from EditText
                String pass = ed_pass.getText().toString();

                // json is used to store the email , password and firebase token
                // and send it to the server
                json = new JSONObject();
                try {
                    // put email in json (key,value)
                    json.put("email",email);
                    //put password in json (key,value)
                    json.put("password",pass);
                    // put FireBase token in json (key,value)
                    json.put("key",FCMTOKEN);

                     // check if there is internet connection
                    if(isNetworkAvailable()){
                        // class requestTask is used to connect to the server and send data
                        // and receive results
                        requestTask r = new requestTask();
                        // send parameter (url to connect to send data , data in json object)
                        r.execute(URL+"/mobile/login.php",json.toString());
                    }else {
                        // display message "please check your internet connection" if there is no internet connection
                        Toast.makeText(getApplicationContext(),"please check your internet connection",Toast.LENGTH_SHORT).show();
                    }

                 // catch if there are errors in connection
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                }
            });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    // class AsyncTask is used to perform background operations and publish results on the UI thread
    // operations like connect to the server or download files
    private class requestTask extends AsyncTask<String,Void,String>{

        String ResponseCode;

        // this function executes until doInBackground executes its operation
        // it will show progress dialog with loading message  until doInBackground finishes its operation
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("loading..");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        // this function is responsible to do background operations
        // connect to the server , send data and receive results
        @Override
        protected String doInBackground(String... params) {

            // params[0] refers to the url
            //params[1] refers to json data
            String content = SendLoginData(params[0],params[1]);

            // this content variable it has the result from the background operation
            // this is the result and it will send to onPostExecute() function
            return content;
        }


        // this function will execute after doInBackground
        //The result of the background computation is passed to this step as a parameter.
        //publish results on the UI thread
        @Override
        protected void onPostExecute(String result) {

            // dismiss the progress dialog is still showing
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            // check if the user send wrong password or wrong email
            if(result.equals("401")){
                Toast.makeText(getApplicationContext(),"email and password are wrong",Toast.LENGTH_SHORT).show();
            }

            else {

                try {
                    JSONObject object = new JSONObject(result);
                    // the user will receive token if he login successfully
                    token = object.getString("token");

                    // store the token and email of the user
                    // we will use them later
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_TOKEN,token);
                    editor.putString(KEY_EMAIL,ed_mail.getText().toString());
                    editor.apply();

                    if (token != null) {
                        // go to drawer activity if the user receive token from the server
                        Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


        public String SendLoginData(String uri, String jsondata){

             //this function are used to connect to the server and send data

            BufferedReader reader=null;
            try {
                URL url = new URL(uri);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // we will send json data to the server (email,password,firebase token)
                DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
                writer.writeBytes(jsondata);
                writer.flush();
                writer.close();

                // log is used to display message in console with (Tag , message)
                Log.i("URL", uri);
                Log.i("Internet Request" , String.valueOf(conn.getResponseCode()));

                // response code to check if there is error
                ResponseCode = String.valueOf(conn.getResponseCode());

                StringBuilder sb = new StringBuilder();
                // receive the result
                // put in BufferedReader object to read it
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line=reader.readLine())!=null){
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

    // function to check if there is internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





}
