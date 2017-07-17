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


public class BedRoom1Fragment extends Fragment {

    String token;
    SeekBar seekBar_light_bedroom1;
    SeekBar seekBar_temp_bedroom1;
    TextView tv_light_bedroom1;
    TextView tv_temp_bedroom1;
    Switch switch_light;
    String light_value;
    Switch switch_temp;
    String temp_value;
    Switch switch_tv;
    String tv_value;
    ToggleButton fire_toggle;
    String light_state;
    String light_maxVal;
    String temp_state;
    String temp_val;
    String tv_state;
    String fire_state;
    String temp_pref;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    BroadcastReceiver broadcastReceiver;
    TextView tv_tempvalue;

    public BedRoom1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bed_room1, container, false);

        setHasOptionsMenu(true);

        // instantiate the views
        tv_tempvalue = (TextView) view.findViewById(R.id.temp_value_bedroom1);
        seekBar_light_bedroom1= (SeekBar) view.findViewById(R.id.seekbar_light_bedroom1);
        seekBar_temp_bedroom1= (SeekBar) view.findViewById(R.id.seekbar_Temp_bedroom1);
        tv_light_bedroom1= (TextView) view.findViewById(R.id.tv_light_bedroom1);
        tv_temp_bedroom1= (TextView) view.findViewById(R.id.tv_Temp_bedroom1);
        switch_light= (Switch) view.findViewById(R.id.switch_light_bedroom1);
        switch_temp= (Switch) view.findViewById(R.id.switch_temp_bedroom1);
        switch_tv = (Switch) view.findViewById(R.id.switch_tv_bedroom1);

        sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_NAME,Context.MODE_PRIVATE);

        // get the token of the user that stored in sharedPreferences
        token = sharedPreferences.getString(MainActivity.KEY_TOKEN,"not found");

        // get the states and the values of the sensors of room 4 (Bedroom 1) from the database
        // and update them
        GetData(MainActivity.URL+"/mobile/get.php?token="+token,"4","room");


        // get the data from the notification and update the UI
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String jsondata = intent.getStringExtra("fcmnotification");

                try {
                    JSONObject jsonObject = new JSONObject(jsondata);
                    String id =  jsonObject.getString("sid");

                    if(id.equals("16")){
                        String state =  jsonObject.getString("state");
                        String val = jsonObject.getString("val");
                        seekBar_light_bedroom1.setProgress(Integer.parseInt(val));

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

                    if(id.equals("18")){
                        String state =  jsonObject.getString("state");
                        String val = jsonObject.getString("val");
                        seekBar_temp_bedroom1.setProgress(Integer.parseInt(val));

                        if(state.equals("1")&&!switch_temp.isChecked()){
                            switch_temp.setChecked(true);
                        }
                        if(state.equals("1")&&switch_temp.isChecked()){

                        }
                        if(state.equals("0")&&switch_temp.isChecked()){
                            switch_temp.setChecked(false);
                        }
                        if(state.equals("0")&&!switch_temp.isChecked()){

                        }

                    }

                    if(id.equals("19")){
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

                    if(id.equals("17")){
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




        seekBar_light_bedroom1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_light_bedroom1.setText(String.valueOf(progress));
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
                    //(URL,id of the sensor light , state ,value of the SeekBar)
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"16","1", String.valueOf(progress));
                }else {
                    // when the switch of light is unchecked send the state 0 and value of progress to the server
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"16","0", String.valueOf(progress));
                }

            }
        });

        seekBar_temp_bedroom1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_temp_bedroom1.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress =seekBar.getProgress();
                if (switch_temp.isChecked()){
                    // when the switch of temp is checked send the state 1 and value of progress to the server
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"18","1", String.valueOf(progress));
                }else {
                    // when the switch of temp is unchecked send the state 0 and value of progress to the server
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"18","0", String.valueOf(progress));
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
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"16",light_value, String.valueOf(seekBar_light_bedroom1.getProgress()));
                }else {
                    // when the switch  is unchecked send the state 0 and value of progress to the server
                    light_value="0";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"16",light_value, String.valueOf(seekBar_light_bedroom1.getProgress()));
                }
            }
        });


        switch_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 and value of progress to the server
                    temp_value="1";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"18",temp_value, String.valueOf(seekBar_temp_bedroom1.getProgress()));
                }else {
                    // when the switch  is unchecked send the state 0 and value of progress to the server
                    temp_value="0";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"18",temp_value, String.valueOf(seekBar_temp_bedroom1.getProgress()));
                }
            }
        });





        switch_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch)v).isChecked();
                if(checked){
                    // when the switch  is checked send the state 1 , it has no SeekBar so the value will be 0
                    tv_value="1";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"19",tv_value,"0");
                }else {
                    // when the switch  is unchecked send the state 0 , it has no SeekBar so the value will be 0
                    tv_value="0";
                    SetData(MainActivity.URL+"/mobile/set.php?token="+token,"19",tv_value,"0");
                }
            }
        });


        // toggle for fire
        fire_toggle= (ToggleButton) view.findViewById(R.id.fire_toggle_bedroom1);
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
        inflater.inflate(R.menu.menu_bedroom1, menu);
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
        if(id==R.id.refresh_bedroom1){
            // to refresh the activity and get the updated states of the senors and display them
            GetData(MainActivity.URL+"/mobile/get.php?token="+token,"4","room");
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
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            // we will get the states and values of the sensor from json data
            // that we receive from the database server
            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONObject light_object = jsonObject.getJSONObject("0");
                light_state = light_object.getString("state");
                light_maxVal = light_object.getString("maxVal");


                JSONObject temp_object = jsonObject.getJSONObject("2");
                temp_state = temp_object.getString("state");
                temp_val =temp_object.getString("curVal");
                temp_pref = temp_object.getString("preVal");

                JSONObject tv_object = jsonObject.getJSONObject("3");
                tv_state = tv_object.getString("state");

                JSONObject fire_object = jsonObject.getJSONObject("1");
                fire_state = fire_object.getString("state");


                // update the states and values of the sensors

                if(light_state.equals("1")){
                    switch_light.setChecked(true);
                }else {
                    switch_light.setChecked(false);
                }
                seekBar_light_bedroom1.setProgress(Integer.parseInt(light_maxVal));


                if(temp_state.equals("1")){
                    switch_temp.setChecked(true);
                }else {
                    switch_temp.setChecked(false);
                }
                seekBar_temp_bedroom1.setProgress(Integer.parseInt(temp_pref));
                tv_tempvalue.setText(temp_val+"C");


                if(tv_state.equals("1")){
                    switch_tv.setChecked(true);
                }else {
                    switch_tv.setChecked(false);
                }


                if(fire_state.equals("1")){
                    fire_toggle.setChecked(true);
                }
                else {
                    fire_toggle.setChecked(false);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
