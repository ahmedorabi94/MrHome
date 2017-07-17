package com.example.ahmed.mrhome;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.ahmed.mrhome.Firebase.MyFirebaseMessagingService;
import org.json.JSONException;
import org.json.JSONObject;


public class BathRoomFragment extends Fragment {

    String token;
    SeekBar seekBar_light_bathroom;
    TextView tv_light_bathroom;
    Switch switch_light;
    String light_value;
    Switch switch_wahser;
    String washer_value;
    ToggleButton waterLeakage_toggle;
    ToggleButton fire_toggle;
    String light_state;
    String light_maxval;
    String washer_state;
    String water_state;
    String fire_state;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    BroadcastReceiver broadcastReceiver;


    public BathRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bath_room, container, false);

        setHasOptionsMenu(true);

        // instantiate the view
        seekBar_light_bathroom = (SeekBar) view.findViewById(R.id.seekbar_light_bathroom);
        tv_light_bathroom = (TextView) view.findViewById(R.id.tv_light_bathroom);
        switch_light = (Switch) view.findViewById(R.id.switch_light_bathroom);
        switch_wahser = (Switch) view.findViewById(R.id.switch_Washer_bathroom);


        sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_NAME,Context.MODE_PRIVATE);

        // get the token of the user that stored in sharedPreferences
        token = sharedPreferences.getString(MainActivity.KEY_TOKEN,"not found");

        // get the states and the values of the sensors of room 5 (Bathroom) from the database
        // and update them
        GetData(MainActivity.URL+"/mobile/get.php?token="+token,"5","room");


        // get the data from the notification and update the UI
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String jsondata = intent.getStringExtra("fcmnotification");

                try {
                    JSONObject jsonObject = new JSONObject(jsondata);
                    String id =  jsonObject.getString("sid");

                    if(id.equals("20")){
                        String state =  jsonObject.getString("state");
                        String val =jsonObject.getString("val");
                        seekBar_light_bathroom.setProgress(Integer.parseInt(val));

                        if(state.equals("1")&&!switch_light.isChecked()){
                            switch_light.setChecked(true);
                        }
                        if(state.equals("1")&&switch_light.isChecked()){
                        }
                        if(state.equals("0")&&switch_light.isChecked()){
                            switch_light.setChecked(false);
                        }
                        if(state.equals("0")&&!switch_light.isChecked()){

                        }

                    }

                    if(id.equals("23")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!switch_wahser.isChecked()){
                            switch_wahser.setChecked(true);
                        }
                        if(state.equals("1")&&switch_wahser.isChecked()){

                        }
                        if(state.equals("0")&&switch_wahser.isChecked()){
                            switch_wahser.setChecked(false);
                        }
                        if(state.equals("0")&&!switch_wahser.isChecked()){

                        }

                    }

                    if(id.equals("22")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!waterLeakage_toggle.isChecked()){
                            waterLeakage_toggle.setChecked(true);
                        }
                        if(state.equals("1")&&waterLeakage_toggle.isChecked()){

                        }
                        if(state.equals("0")&&waterLeakage_toggle.isChecked()){
                            waterLeakage_toggle.setChecked(false);
                        }
                        if(state.equals("0")&&!waterLeakage_toggle.isChecked()){

                        }

                    }

                    if(id.equals("21")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!fire_toggle.isChecked()){
                            fire_toggle.setChecked(true);
                        }
                        if(state.equals("1")&&fire_toggle.isChecked()){

                        }
                        if(state.equals("0")&&fire_toggle.isChecked()){
                            fire_toggle.setChecked(false);
                        }
                        if(state.equals("0")&&!fire_toggle.isChecked()){

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };




        seekBar_light_bathroom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_light_bathroom.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // we use this function instead of onProgressChanged to get the final value of the SeekBar
            // when the user release the SeekBar
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // get the progress of the SeekBar
                int progress = seekBar.getProgress();
                if (switch_light.isChecked()){
                    // when the switch of light is checked send the state 1 and value of progress to the server
                    //(URL,id of the sensor temp , state ,value of the SeekBar)
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"20","1", String.valueOf(progress));
                }else {
                    // when the switch of light is unchecked send the state 0 and value of progress to the server
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"20","0", String.valueOf(progress));
                }

            }
        });



        switch_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the switch is changed by the user or not
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 and value of progress to the server
                    light_value="1";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"20",light_value, String.valueOf(seekBar_light_bathroom.getProgress()));
                }else {
                    // when the switch  is unchecked send the state 0 and value of progress to the server
                    light_value="0";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"20",light_value, String.valueOf(seekBar_light_bathroom.getProgress()));
                }
            }
        });



        switch_wahser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 , it has no SeekBar so the value will be 0
                    washer_value="1";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"23",washer_value,"0");
                }else {
                    // when the switch  is unchecked send the state 0 , it has no SeekBar so the value will be 0
                    washer_value="0";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"23",washer_value,"0");
                }
            }
        });

        // toggle of waterLeakage
        waterLeakage_toggle = (ToggleButton) view.findViewById(R.id.waterLeakage_toggle_bathroom);
        waterLeakage_toggle.setEnabled(false);

       // toggle of fire
        fire_toggle= (ToggleButton) view.findViewById(R.id.fire_toggle_bathroom);
        fire_toggle.setEnabled(false);


        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        // we should register the BrodCast
        getContext().registerReceiver(broadcastReceiver,new IntentFilter(MyFirebaseMessagingService.MESSAGE_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        // we should unregister the BrodCast
        getContext().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bathroom, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.action_settings){
            // go to setting activity
            Intent intent = new Intent(getActivity(),Settings.class);
            intent.putExtra("token",token);
            startActivity(intent);
            return true;
        }
        if(id==R.id.refresh_bathroom){
            // to refresh the activity and get the updated states of the senors and display them
            GetData(MainActivity.URL+"/mobile/get.php?token="+token,"5","room");
        }

        return super.onOptionsItemSelected(item);
    }



    //(URL,Id of the sensor,State Of the Sensor,Value of the SeekBar)
    private void SetData(String uri, String id, String state, String value) {
        RequestPackage p = new RequestPackage();
        p.setUri(uri);
        p.setMethod("GET");
        p.setParam("id",id);
        p.setParam("state",state);
        p.setParam("val",value);

        //execute AsyncTask
        RequestTask r = new RequestTask();
        r.execute(p);

    }



    // class AsyncTask is used to perform background operations and publish results on the UI thread
    // operations like connect to the server or download files
    public class RequestTask extends AsyncTask<RequestPackage,Void,String> {


        // this function is responsible to do background operations
        // connect to the server , send data to the server
        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = Utility.getData(params[0]);
            return content;
        }

    }


    // get the data of the sensors (Url ,Id , Type)
    public void GetData(String uri,String id ,String type){
        RequestPackage p = new RequestPackage();
        p.setUri(uri);
        p.setMethod("GET");
        p.setParam("id",id);
        p.setParam("type",type);

        GetTask getTask = new GetTask();
        getTask.execute(p);



    }




    public class GetTask extends AsyncTask<RequestPackage,Void,String>{

        // this function executes until doInBackground executes its operation
        // it will show progress dialog with loading message  until doInBackground finishes its operation
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("loading..");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            String content = Utility.getData(params[0]);

            return content;
        }


        // this function will execute after doInBackground
        //The result of the background computation is passed to this step as a parameter.
        //publish results on the UI thread
        @Override
        protected void onPostExecute(String result) {

            // dismiss the progress dialog if still showing
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            // we will get the states and values of the sensor from json data
            // that we receive from the database server

            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONObject light_object = jsonObject.getJSONObject("0");
                light_state = light_object.getString("state");
                light_maxval = light_object.getString("maxVal");

                JSONObject washer_object = jsonObject.getJSONObject("3");
                washer_state = washer_object.getString("state");

                JSONObject water_object = jsonObject.getJSONObject("2");
                water_state  = water_object.getString("state");


                JSONObject fire_object = jsonObject.getJSONObject("1");
                fire_state = fire_object.getString("state");


                // update the states and values of the sensors

                if(light_state.equals("1")){
                    switch_light.setChecked(true);
                }else {
                    switch_light.setChecked(false);
                }
                seekBar_light_bathroom.setProgress(Integer.parseInt(light_maxval));



                if(washer_state.equals("1")){
                    switch_wahser.setChecked(true);
                }else{
                    switch_wahser.setChecked(false);
                }


                if(water_state.equals("1")){
                    waterLeakage_toggle.setChecked(true);
                }else {
                    waterLeakage_toggle.setChecked(false);
                }


                if(fire_state.equals("1")){
                    fire_toggle.setChecked(true);
                }else {
                    fire_toggle.setChecked(false);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}
