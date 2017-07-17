package com.example.ahmed.mrhome;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by Ahmed Orabi on 18/06/2017.
 */

public class UserCustomAdapter extends ArrayAdapter<UserData> {

    ArrayList<UserData> list;
    Context context;
    private TextView tv_username;
    private TextView tv_email;
    private CheckBox chk_isAdmin;
    private CheckBox chk_isApprved;
    private Button btn_delete;
    String token;




    public UserCustomAdapter(Context context, ArrayList<UserData> userlist ,String token) {
        super(context, 0,userlist);
        this.context=context;
        this.list=userlist;
        this.token=token;
    }



    @Override
    public View getView(int position,  View convertView,ViewGroup parent) {

         UserData userData = getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.user_list_item_gridlayout,parent,false);

        tv_username = (TextView) convertView.findViewById(R.id.usernmae_user);
        tv_email = (TextView) convertView.findViewById(R.id.email_user);
        chk_isAdmin = (CheckBox) convertView.findViewById(R.id.chc_isAdmin);
        chk_isApprved = (CheckBox) convertView.findViewById(R.id.chc_isApproved);
        btn_delete = (Button) convertView.findViewById(R.id.delete_user);


        // set username of the user
        tv_username.setText(userData.getUsername());
        // set email of the user
        tv_email.setText(userData.getEmail());
        String is_admin = userData.getIsAdmin();
        String is_approved= userData.getIsApproved();
        final String id = userData.getId();


        if(is_admin.equals("1")){
            chk_isAdmin.setChecked(true);
        }else {
            chk_isAdmin.setChecked(false);
        }


        if(is_approved.equals("1")){
            chk_isApprved.setChecked(true);
        }else {
            chk_isApprved.setChecked(false);
        }



        chk_isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // if the CheckBox is checked set the user an admin
                    setUser(MainActivity.URL+"/mobile/admin.php?token="+token,"setadmin",id);
                }else {
                    // if the CheckBox is unchecked unset the user an admin
                    setUser(MainActivity.URL+"/mobile/admin.php?token="+token,"unsetadmin",id);
                }
            }
        });



        chk_isApprved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // if the CheckBox is checked approve the user
                    setUser(MainActivity.URL+"/mobile/admin.php?token="+token,"approve",id);
                }else {
                    // if the CheckBox is unchecked
                    Toast.makeText(getContext(),"user data may be will be deleted",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete the user data when click on button delete
                setUser(MainActivity.URL+"/mobile/admin.php?token="+token,"delete",id);
                notifyDataSetChanged();

            }
        });


        return convertView;
    }

    //(URL,(operation > delete , approve , setAdmin , unsetAdmin) , id of the user)
    private void setUser(String uri, String op, String id) {
        RequestPackage p = new RequestPackage();
        p.setMethod("GET");
        p.setUri(uri);
        p.setParam("op",op);
        p.setParam("id",id);

        //execute AsyncTask
        setUserTask s = new setUserTask();
        s.execute(p);

    }

    // class AsyncTask is used to perform background operations and publish results on the UI thread
    // operations like connect to the server or download files
    private class setUserTask extends AsyncTask<RequestPackage,Void,String>{


        // this function is responsible to do background operations
        // connect to the server , send data and receive results
        @Override
        protected String doInBackground(RequestPackage... params) {

            String data = Utility.getData(params[0]);
            return data;
        }


    }


}
