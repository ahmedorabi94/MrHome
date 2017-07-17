package com.example.ahmed.mrhome.Firebase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.example.ahmed.mrhome.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by Ahmed Orabi on 27/06/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG="fcmMessaging";

    Map<String,String> params;


    public static final String MESSAGE_BROADCAST="message";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        params = remoteMessage.getData();
        JSONObject jsonObject  =new JSONObject(params);
        Log.d("Json_Data",jsonObject.toString());

        Intent intent = new Intent(MESSAGE_BROADCAST);
        intent.putExtra("fcmnotification",jsonObject.toString());

        getApplicationContext().sendBroadcast(intent);

        String json_data = jsonObject.toString();

        showNotification(json_data);


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


    }

    private void showNotification(String json_data) {
        String id;
        String state;
        String val;

        try {
            JSONObject jsonobject = new JSONObject(json_data);
            id = jsonobject.getString("sid");


            /////////////Living Room////////////////////
            if(id.equals("1")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Light in Living Room , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("2")){
                state = jsonobject.getString("state");
                String message = "Fire in Living Room , " + "state = " + state ;
                notifyUser("Update", message);
            }
            if(id.equals("3")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Temp in Living Room , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("4")){
                state = jsonobject.getString("state");
                String message = "Motion in Living Room , " + "state = " + state ;
                notifyUser("Update", message);
            }
            if(id.equals("5")){
                state = jsonobject.getString("state");
                String message = "Door in Living Room , " + "state = " + state ;
                notifyUser("Update", message);
            }
            if(id.equals("6")){
                state = jsonobject.getString("state");
                String message = "TV in Living Room , " + "state = " + state ;
                notifyUser("Update", message);
            }
            ///////////Kitchen///////////////////////
            if(id.equals("7")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Light in Kitchen , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("8")){
                state = jsonobject.getString("state");
                String message = "Fire in Kitchen , " + "state = " + state ;
                notifyUser("Update", message);
            }
            if(id.equals("9")){
                state = jsonobject.getString("state");
                String message = "Gas Leakage in Kitchen , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("10")){
                state = jsonobject.getString("state");
                String message = "Water Leakage in Kitchen , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("11")){
                state = jsonobject.getString("state");
                String message = "Fridge in Kitchen , " + "state = " + state;
                notifyUser("Update", message);
            }
            //////////////Garage/////////////////////////
            if(id.equals("12")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Light in Garage , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("13")){
                state = jsonobject.getString("state");
                String message = "Fire in Garage , " + "state = " + state ;
                notifyUser("Update", message);
            }
            if(id.equals("14")){
                state = jsonobject.getString("state");
                String message = "Motion in Garage , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("15")){
                state = jsonobject.getString("state");
                String message = "Door in Garage , " + "state = " + state;
                notifyUser("Update", message);
            }
            ////////////////Bedroom 1///////////////////////////////
            if(id.equals("16")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Light in Bedroom 1 , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("17")){
                state = jsonobject.getString("state");
                String message = "Fire in Bedroom 1 , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("18")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Temp in Bedroom 1 , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("19")){
                state = jsonobject.getString("state");
                String message = "TV in Bedroom 1 , " + "state = " + state;
                notifyUser("Update", message);
            }

            ////////////////////Bathroom/////////////////////////////
            if(id.equals("20")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Light in Bathroom , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("21")){
                state = jsonobject.getString("state");
                String message = "Fire in Bathroom , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("22")){
                state = jsonobject.getString("state");
                String message = "Water Leakage in Bathroom , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("23")){
                state = jsonobject.getString("state");
                String message = "Washer in Bathroom , " + "state = " + state ;
                notifyUser("Update", message);
            }
            /////////////////////Bedroom 2//////////////////////////
            if(id.equals("24")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Light in Bedroom 2 , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("25")){
                state = jsonobject.getString("state");
                String message = "Fire in Bedroom 2 , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("26")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Temp in Bedroom 2 , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("27")){
                state = jsonobject.getString("state");
                String message = "TV in Bedroom 2 , " + "state = " + state;
                notifyUser("Update", message);
            }
            ////////////////////Bedroom 3///////////////////////////////
            if(id.equals("28")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Light in Bedroom 3 , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("29")){
                state = jsonobject.getString("state");
                String message = "Fire in Bedroom 3 , " + "state = " + state;
                notifyUser("Update", message);
            }
            if(id.equals("30")){
                state = jsonobject.getString("state");
                val = jsonobject.getString("val");
                String message = "Temp in Bedroom 3 , " + "state = " + state + " , value = " + val;
                notifyUser("Update", message);
            }
            if(id.equals("31")){
                state = jsonobject.getString("state");
                String message = "TV in Bedroom 3 , " + "state = " + state;
                notifyUser("Update", message);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void notifyUser(String from, String body) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        MyNotificationManager myNotificaionManager = new MyNotificationManager(getApplicationContext());
        myNotificaionManager.showNotification(from,body,intent);
    }



}
