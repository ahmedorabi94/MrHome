package com.example.ahmed.mrhome;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ahmed Orabi on 09/05/2017.
 */

public class Utility {

    public static String getData(RequestPackage p){

        // to read the data coming from the server
        BufferedReader reader =null;
        String uri =p.getUri();

         // if the method is GET put the params in uri
        if(p.getMethod().equals("GET")){
            uri += "&" + p.getEncodedParams();
        }

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //set the request method POST or GET
            con.setRequestMethod(p.getMethod());


            Log.i("Internet Request" , String.valueOf(con.getResponseCode()));
            Log.i("URL " , uri);

            StringBuilder sb =  new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while((line=reader.readLine())!=null){
                //put the data in string Builder
                sb.append(line + "\n");
            }
            // return thr result
            return sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
