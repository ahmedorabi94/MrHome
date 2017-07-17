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
import android.util.Log;
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



public class LivingRoomFragment extends Fragment {

    SeekBar seekBar_light;
    TextView light_tv;
    SeekBar seekbar_temp;
    TextView tv_temp;
    String token;
    Switch switch_light;
    String light_value;
    Switch switch_temp;
    String temp_value;
    Switch switch_tv;
    String tv_value;
    Switch switch_door;
    String door_value;
    ToggleButton toggle_fire;
    ToggleButton toggle_motion;
    String fire_state;
    String motion_state=null;
    String temp;
    String light_state;
    String light_Maxval;
    String door_state;
    String tv_state;
    String temp_state;
    String temp_pref;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    BroadcastReceiver broadcastReceiver;
    TextView tv_tempValue;


    public LivingRoomFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_living_room, container, false);
        setHasOptionsMenu(true);

        // instantiate the views
        tv_tempValue = (TextView) view.findViewById(R.id.temp_value_livingroom);
        switch_door = (Switch) view.findViewById(R.id.switch_door_livingroom);
        switch_tv= (Switch) view.findViewById(R.id.switch_tv_livingroom);
        switch_temp= (Switch) view.findViewById(R.id.switch_temp_livingroom);
        switch_light = (Switch) view.findViewById(R.id.switch_light_livingroom);
        seekBar_light = (SeekBar) view.findViewById(R.id.seekbar_light_livingroom);
        light_tv = (TextView) view.findViewById(R.id.tv_light_livingroom);
        seekbar_temp = (SeekBar) view.findViewById(R.id.seekbar_Temp_livingroom);
        tv_temp= (TextView) view.findViewById(R.id.tv_Temp_livingroom);

        sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_NAME,Context.MODE_PRIVATE);

        // get the token of the user that stored in sharedPreferences
        token = sharedPreferences.getString(MainActivity.KEY_TOKEN,"not found");

        // get the states and the values of the sensors of room 1 (Living Room) from the database
        // and update them
        GetData(MainActivity.URL+"/mobile/get.php?token="+token,"1","room");



        // set setOnSeekBarChangeListener to SeekBar of the temperature
        seekbar_temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // display the progress of SeekBar
                tv_temp.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // we use this function instead of onProgressChanged to get the final value of the SeekBar
            // when the user release the SeekBar
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // get progress of SeekBar
                int progress = seekBar.getProgress();
                if(switch_temp.isChecked()){
                    // when the switch of temp is checked send the state 1 and value of progress to the server
                    //(URL,id of the sensor temp , state ,value of the SeekBar)
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"3","1", String.valueOf(progress));
                }else {
                    // when the switch of temp is unchecked send the state 0 and value of progress to the server
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"3","0", String.valueOf(progress));
                }

            }
        });


         // SeekBar of the Light
        seekBar_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // display the progress of SeekBar
                light_tv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if(switch_light.isChecked()){
                    // when the switch of light is checked send the state 1 and value of progress to the server
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"1","1", String.valueOf(progress));
                }else {
                    // when the switch of light is unchecked send the state 0 and value of progress to the server
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"1","0", String.valueOf(progress));
                }


            }
        });






        // set setOnClickListener to the switch of the light
        switch_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if the user check the switch or not
                 boolean checked  =((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 and value of progress to the server
                    light_value="1";
                    //(URL,Id of the sensor, state of the switch,value of SeekBar)
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"1",light_value, String.valueOf(seekBar_light.getProgress()));
                }else {
                    // when the switch  is unchecked send the state 0 and value of progress to the server
                    light_value="0";
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"1",light_value, String.valueOf(seekBar_light.getProgress()));
                }
            }
        });


        // set setOnClickListener to the switch of the temp
        switch_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 and value of progress to the server
                    temp_value="1";
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"3",temp_value, String.valueOf(seekbar_temp.getProgress()));
                }else {
                    // when the switch  is unchecked send the state 0 and value of progress to the server
                    temp_value="0";
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"3",temp_value, String.valueOf(seekbar_temp.getProgress()));
                }
            }
        });


        // set setOnClickListener to the switch of the TV
        switch_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 , it has no SeekBar so the value will be 0
                    tv_value="1";
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"6",tv_value,"0");
                }else {
                    // when the switch  is unchecked send the state 0 , it has no SeekBar so the value will be 0
                    tv_value="0";
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"6",tv_value,"0");
                }
            }
        });



        // set setOnClickListener to the switch of the Door
        switch_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 , it has no SeekBar so the value will be 0
                    door_value="1";
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"5",door_value,"0");
                }else {
                    // when the switch  is unchecked send the state 0 , it has no SeekBar so the value will be 0
                    door_value="0";
                    SettData(MainActivity.URL+"/mobile/set.php?token="+token,"5",door_value,"0");
                }
            }
        });



        // toggle button to the fire
        // it will show the state only
        toggle_fire= (ToggleButton) view.findViewById(R.id.fire_toggle_livingroom);
        toggle_fire.setEnabled(false);


        // toggle button of the motion
        toggle_motion= (ToggleButton) view.findViewById(R.id.motion_toggle_livingroom);
        toggle_motion.setEnabled(false);


        // get the data from the notification and update the UI
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String jsondata = intent.getStringExtra("fcmnotification");
                Log.i("js",jsondata);
                try {
                    JSONObject jsonObject = new JSONObject(jsondata);
                    String id =  jsonObject.getString("sid");

                    if(id.equals("1")){
                        String state =  jsonObject.getString("state");
                        String val = jsonObject.getString("val");
                        seekBar_light.setProgress(Integer.parseInt(val));

                        if(state.equals("1")){
                            switch_light.setChecked(true);
                        }
                        else {
                            switch_light.setChecked(false);
                        }
                    }

                    if(id.equals("3")){
                        String state =  jsonObject.getString("state");
                        String val = jsonObject.getString("val");
                        seekbar_temp.setProgress(Integer.parseInt(val));

                        if(state.equals("1")&&!switch_temp.isChecked()){
                            switch_temp.setChecked(true);
                        }
                        if(state.equals("1")&&switch_temp.isChecked()) {

                        }
                        if(state.equals("0")&&switch_temp.isChecked()){
                            switch_temp.setChecked(false);
                        }
                        if(state.equals("0")&&!switch_temp.isChecked()){

                        }


                    }

                    if(id.equals("6")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!switch_tv.isChecked()){
                            switch_tv.setChecked(true);
                        }
                        if(state.equals("1")&&switch_tv.isChecked()){

                        }
                        if(state.equals("0")&&switch_tv.isChecked()){
                            switch_tv.setChecked(false);
                        }
                        if(state.equals("0")&&!switch_tv.isChecked()){

                        }

                    }

                    if(id.equals("5")){
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

                    if(id.equals("2")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!toggle_fire.isChecked()){
                            toggle_fire.setChecked(true);
                        }
                        if(state.equals("1")&&toggle_fire.isChecked()){

                        }
                        if(state.equals("0")&&toggle_fire.isChecked()){
                            toggle_fire.setChecked(false);
                        }
                        if(state.equals("0")&&!toggle_fire.isChecked()){

                        }

                    }

                    if(id.equals("4")){
                        String state =  jsonObject.getString("state");

                        if(state.equals("1")&&!toggle_motion.isChecked()){
                            toggle_motion.setChecked(true);
                        }
                        if(state.equals("1")&&toggle_motion.isChecked()){

                        }
                        if(state.equals("0")&&toggle_motion.isChecked()){
                            toggle_motion.setChecked(false);
                        }
                        if(state.equals("0")&&!toggle_motion.isChecked()){

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };



        return view;
    }




    @Override
    public void onResume() {
        super.onResume();
        // we should register the brodCast receiver
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter(MyFirebaseMessagingService.MESSAGE_BROADCAST));
    }


    @Override
    public void onPause() {
        super.onPause();
        // we should unregister the brodCast receiver
        getActivity().unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_livingroom, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.action_settings){
            // to go to the settings activity
            Intent intent = new Intent(getActivity(),Settings.class);
            intent.putExtra("token",token);
            startActivity(intent);
            return true;
        }
        // to refresh the activity and get the updated states of the senors and display them
        if(id==R.id.refresh_living_room){
            GetData(MainActivity.URL+"/mobile/get.php?token="+token,"1","room");
        }
        return super.onOptionsItemSelected(item);
    }



    //(URL,Id of the sensor,State Of the Sensor,Value of the SeekBar)
    private void SettData(String uri,String id,String state,String value) {
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

    public class RequestTask extends AsyncTask<RequestPackage,Void,String>{


        // this function is responsible to do background operations
        // connect to the server , send data to the server
        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = Utility.getData(params[0]);
            return content;
        }

    }




    // get the data of the sensors (Url Id , Type)
    public void GetData(String uri,String id ,String type){
        RequestPackage p = new RequestPackage();
        p.setUri(uri);
        p.setMethod("GET");
        p.setParam("id",id);
        p.setParam("type",type);

        //execute AsyncTask
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

            // dismiss the progress dialog is still showing
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }


            // we will get the states and values of the sensor from json data
            // that we receive from the database server
            try{
                JSONObject jsonObject = new JSONObject(result);


                JSONObject light_object = jsonObject.getJSONObject("0");
                light_state =light_object.getString("state");
                light_Maxval=light_object.getString("maxVal");


                JSONObject fire_object = jsonObject.getJSONObject("1");
                fire_state = fire_object.getString("state");

                JSONObject temp_object = jsonObject.getJSONObject("2");
                 temp  = temp_object.getString("curVal");
                temp_state =temp_object.getString("state");
                temp_pref =temp_object.getString("preVal");



                JSONObject motion_object = jsonObject.getJSONObject("3");
                motion_state  = motion_object.getString("state");


                JSONObject door_object=jsonObject.getJSONObject("4");
                door_state =door_object.getString("state");

                JSONObject tv_object=jsonObject.getJSONObject("5");
                tv_state =tv_object.getString("state");


                // update the states and values of the sensors
                if(motion_state.equals("1")){
                    toggle_motion.setChecked(true);
                }else{
                    toggle_motion.setChecked(false);
                }


                if(fire_state.equals("1")){
                    toggle_fire.setChecked(true);
                }else {
                    toggle_fire.setChecked(false);
                }


                if(tv_state.equals("1")){
                    switch_tv.setChecked(true);
                }else{
                    switch_tv.setChecked(false);
                }


                if(door_state.equals("1")){
                    switch_door.setChecked(true);
                }else {
                    switch_door.setChecked(false);
                }


                if (temp_state.equals("1")){
                    switch_temp.setChecked(true);

                }else {
                    switch_temp.setChecked(false);
                }
                seekbar_temp.setProgress(Integer.parseInt(temp_pref));

                tv_tempValue.setText(temp+"C");



                if(light_state.equals("1")){
                    switch_light.setChecked(true);
                }else {
                    switch_light.setChecked(false);
                }
                seekBar_light.setProgress(Integer.parseInt(light_Maxval));




            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }


}
