package com.example.ahmed.mrhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Ahmed Orabi on 16/06/2017.
 */

public class SettingCustomAdapter extends ArrayAdapter<String> {

    private Context context;


    public SettingCustomAdapter(Context context, String[] stringList) {
        super(context, 0,stringList);
        this.context=context;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        String item = getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.setting_list_item,parent,false);

        TextView tv = (TextView) convertView.findViewById(R.id.setting_list_text);
        tv.setText(item);

        return convertView;
    }
}
