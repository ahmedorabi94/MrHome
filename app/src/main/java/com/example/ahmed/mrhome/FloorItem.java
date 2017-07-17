package com.example.ahmed.mrhome;

/**
 * Created by Ahmed Orabi on 02/05/2017.
 */


public class FloorItem {

    // name for the room
    private String name;
    //image for the room
    private int imageId;

    public FloorItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}
