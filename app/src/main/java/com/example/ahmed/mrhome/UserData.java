package com.example.ahmed.mrhome;

/**
 * Created by Ahmed Orabi on 18/06/2017.
 */

public class UserData {

    // id of the user
    String id;
    //username of the user
    String username;
    //email of the user
    String email;
    // if admin IsAdmin =1 if not IsAdmin=0
    String IsAdmin;
    //if the user is approved isApproved = 1 if not isApproved =0
    String IsApproved;

    public UserData(String id, String username, String email , String isAdmin ,String isApproved) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.IsAdmin=isAdmin;
        this.IsApproved =isApproved;
    }


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getIsAdmin() {
        return IsAdmin;
    }

    public String getIsApproved() {
        return IsApproved;
    }


}
