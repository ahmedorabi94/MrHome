package com.example.ahmed.mrhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by Ahmed Orabi on 02/05/2017.
 */

public class FloorCustomAdapter extends ArrayAdapter<FloorItem> {

    private Context context;


    public FloorCustomAdapter( Context context, List<FloorItem> floorItemList) {
        super(context, 0,floorItemList);
        this.context=context;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        // get item from the list
        // each has name and image to room
        FloorItem item = getItem(position);

        // we get the lauout of list item to set data to it
        convertView = LayoutInflater.from(context).inflate(R.layout.room_list_item,parent,false);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_list_room);
        TextView textView = (TextView) convertView.findViewById(R.id.list_text_room);

        // set the name of the room
        textView.setText(item.getName());
        // set the image to the room
        // we use glide library because it is faster
        Glide.with(getContext()).load(item.getImageId()).into(imageView);

        return convertView;
    }
}
