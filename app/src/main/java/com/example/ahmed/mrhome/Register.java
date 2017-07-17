package com.example.ahmed.mrhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    TextView tv_register;
    EditText ed_username , ed_email , ed_pass,ed_confirmpass , ed_phone;
    Button btn_signup;
    String user_name , user_email , user_pass, user_confirm_pass , user_phone;
    TextView tv_showdata;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // instantiate the views
        tv_showdata = (TextView) findViewById(R.id.tv_showdata);
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_email = (EditText) findViewById(R.id.ed_usermail);
        ed_pass = (EditText) findViewById(R.id.ed_user_pass);
        ed_confirmpass = (EditText) findViewById(R.id.ed_user_confirmPass);
        ed_phone = (EditText) findViewById(R.id.ed_userphone);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        tv_register= (TextView) findViewById(R.id.tv_register);

        // set custom font the TextView tv_register
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Pacifico.ttf");
        tv_register.setTypeface(custom_font);



        // send username , email , phone , password to the server
        // to register new user when click on sign up Button
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if there is internet connection
                if(isNetworkAvailable()) {
                    sendData();
                }else {
                    Toast.makeText(getApplicationContext(),"please check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


     private void sendData() {
         // get the username , email , password , phone and confirm password from EditText
         user_name = ed_username.getText().toString();
         user_email=ed_email.getText().toString();
         user_pass = ed_pass.getText().toString();
         user_phone = ed_phone.getText().toString();
         user_confirm_pass = ed_confirmpass.getText().toString();


         if(user_pass.length()>8&&isValidPassword(user_pass)){
             if(user_pass.equals(user_confirm_pass)){
                 // put code here

                 JSONObject data = new JSONObject();
                 try {
                     // put the data in json object
                     // we will send the data to server
                     // in form in json
                     data.put("username",user_name);
                     data.put("email",user_email);
                     data.put("phone",user_phone);
                     data.put("password",user_pass);

                     Log.i("register json", data.toString());

                     // class SendUserData is used to connect to the server and send data
                     // and receive results
                     SendUserData snd = new SendUserData();
                     // send parameter (url to connect to send data , data in json object)
                     snd.execute(MainActivity.URL+"/mobile/register.php",data.toString());

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }else {
                 Toast.makeText(getApplicationContext(),"please confirm your password",Toast.LENGTH_SHORT).show();
             }
         }
         else {
             Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_LONG).show();
         }
     }




    // class AsyncTask is used to perform background operations and publish results on the UI thread
    // operations like connect to the server or download files
    public class SendUserData extends AsyncTask<String,Void,String>{

        String ResponseCode;


        // this function executes until doInBackground executes its operation
        // it will show progress dialog with loading message  until doInBackground finishes its operation
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Register.this);
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
            String data = SendLoginData(params[0],params[1]);
            // this content variable it has the result from the background operation
            // this is result and it will send to onPostExecute() function
            return data;
        }


        // this function will execute after doInBackground
        //The result of the background computation is passed to this step as a parameter.
        //publish results on the UI thread
        @Override
        protected void onPostExecute(String result) {

            String message;

            // dismiss the progress dialog is still showing
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                message = jsonObject.getString("message");

                if(!message.equals(null)){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

             // if the user register successfully
            if (result.equals("200")){
                Toast.makeText(getApplicationContext(),"data register successfully",Toast.LENGTH_LONG).show();
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

                // we will send json data to the server (username ,email,password,phone ,password)

                DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
                writer.writeBytes(jsondata);
                writer.flush();
                writer.close();

                // log is used to display message in console with (Tag , message)
                Log.i("Internet Request" , String.valueOf(conn.getResponseCode()));

                // response code to check if there is error
                ResponseCode = String.valueOf(conn.getResponseCode());


                StringBuilder sb = new StringBuilder();
                // receive the result
                // put in BufferedReader object to read it
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
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


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
     //   final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}
