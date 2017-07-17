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


public class GarageFragment extends Fragment {

    String token;
    SeekBar seekBar_light_garage;
    TextView tv_light_garage;
    Switch switch_light;
    String light_value;
    Switch switch_door;
    String door_value;
    ToggleButton fire_toggle;
    ToggleButton motion_toggle;
    String light_state;
    String light_maxVal;
    String door_state;
    String fire_state;
    String motion_state;
    ProgressDialog progressDialog;
    BroadcastReceiver broadcastReceiver;
    SharedPreferences sharedPreferences;




    public GarageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_garage, container, false);
        setHasOptionsMenu(true);

        //instantiate the views
        seekBar_light_garage = (SeekBar) view.findViewById(R.id.seekbar_light_garage);
        tv_light_garage= (TextView) view.findViewById(R.id.tv_light_garage);
        switch_light = (Switch) view.findViewById(R.id.switch_light_garage);
        switch_door= (Switch) view.findViewById(R.id.switch_door_garage);

        sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_NAME,Context.MODE_PRIVATE);

        // get the token of the user that stored in sharedPreferences
        token = sharedPreferences.getString(MainActivity.KEY_TOKEN,"not found");

        // get the states and the values of the sensors of room 3 (Garage) from the database
        // and update them
        GetData(MainActivity.URL+"/mobile/get.php?token="+token,"3","room");


        // get the data from the notification and update the UI
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String jsondata = intent.getStringExtra("fcmnotification");

                try {
                    JSONObject jsonObject = new JSONObject(jsondata);
                    String id =  jsonObject.getString("sid");

                    if(id.equals("12")){
                        String state =  jsonObject.getString("state");
                        String val = jsonObject.getString("val");
                        seekBar_light_garage.setProgress(Integer.parseInt(val));


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

                    if(id.equals("15")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!switch_door.isChecked()){
                            switch_door.setChecked(true);
                        }
                        if(state.equals("1")&&switch_door.isChecked()){

                        }
                        if(state.equals("0")&&switch_door.isChecked()){
                            switch_door.setChecked(false);
                        }
                        if(state.equals("0")&&!switch_door.isChecked()){

                        }

                    }

                    if(id.equals("13")){
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

                    if(id.equals("14")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!motion_toggle.isChecked()){
                            motion_toggle.setChecked(true);
                        }
                        if(state.equals("1")&&motion_toggle.isChecked()){

                        }
                        if(state.equals("0")&&motion_toggle.isChecked()){
                            motion_toggle.setChecked(false);
                        }
                        if(state.equals("0")&&!motion_toggle.isChecked()){

                        }

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };



        // SeekBar of the Light
        seekBar_light_garage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_light_garage.setText(String.valueOf(progress));
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
                if(switch_light.isChecked()){
                    // when the switch of light is checked send the state 1 and value of progress to the server
                    //(URL,id of the sensor temp , state ,value of the SeekBar)
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"12","1", String.valueOf(progress));
                }else {
                    // when the switch of light is unchecked send the state 0 and value of progress to the server
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"12","0", String.valueOf(progress));
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
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"12",light_value, String.valueOf(seekBar_light_garage.getProgress()));
                }else {
                    // when the switch  is unchecked send the state 0 and value of progress to the server
                    light_value="0";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"12",light_value, String.valueOf(seekBar_light_garage.getProgress()));
                }
            }
        });




        switch_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 , it has no SeekBar so the value will be 0
                    door_value="1";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"15",door_value,"0");
                }else {
                    // when the switch  is unchecked send the state 0 , it has no SeekBar so the value will be 0
                    door_value="0";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"15",door_value,"0");
                }
            }
        });


        // toggle button to the fire
        // it will show the state only
        fire_toggle= (ToggleButton) view.findViewById(R.id.fire_toggle_garage);
        fire_toggle.setEnabled(false);

        // toggle button to the motion
        motion_toggle= (ToggleButton) view.findViewById(R.id.motion_toggle_garage);
        motion_toggle.setEnabled(false);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        // we should register the Broadcast
        getContext().registerReceiver(broadcastReceiver,new IntentFilter(MyFirebaseMessagingService.MESSAGE_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        // we should Unregister the Broadcast
        getContext().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_garage, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.action_settings){
            // go to the setting activity
            Intent intent = new Intent(getActivity(),Settings.class);
            intent.putExtra("token",token);
            startActivity(intent);
            return true;
        }
        if(id==R.id.refresh_garage){
            // to refresh the activity and get the updated states of the senors and display them
            GetData(MainActivity.URL+"/mobile/get.php?token="+token,"3","room");
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
        // execute AsyncTask
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


    // get the data of the sensors (Url , Id , Type)
    public void GetData(String uri,String id ,String type){
        RequestPackage p = new RequestPackage();
        p.setUri(uri);
        p.setMethod("GET");
        p.setParam("id",id);
        p.setParam("type",type);

        // execute AsyncTask
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

            // dismiss the progress dialog id still showing
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            // we will get the states and values of the sensor from json data
            // that we receive from the database server
            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONObject light_object = jsonObject.getJSONObject("0");
                light_state = light_object.getString("state");
                light_maxVal=light_object.getString("maxVal");


                JSONObject door_object = jsonObject.getJSONObject("3");
                door_state = door_object.getString("state");


                JSONObject fire_object = jsonObject.getJSONObject("1");
                fire_state = fire_object.getString("state");

                JSONObject motion_object = jsonObject.getJSONObject("2");
                motion_state = motion_object.getString("state");


                // update the states and values of the sensors

                if(light_state.equals("1")){
                    switch_light.setChecked(true);
                }else{
                    switch_light.setChecked(false);
                }



                seekBar_light_garage.setProgress(Integer.parseInt(light_maxVal));



                if(door_state.equals("1")){
                    switch_door.setChecked(true);
                }else{
                    switch_door.setChecked(false);
                }


                if(fire_state.equals("1")){
                    fire_toggle.setChecked(true);
                }else {
                    fire_toggle.setChecked(false);
                }



                if(motion_state.equals("1")){
                    motion_toggle.setChecked(true);
                }else{
                    motion_toggle.setChecked(false);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}


