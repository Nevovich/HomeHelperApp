package com.example.saturdayapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfo {
    private String loggedInUserUID, username, userEmail;

    public UserInfo(String loggedInUserUID, String username, String userEmail) {
        this.loggedInUserUID = loggedInUserUID;
        this.username = username;
        this.userEmail = userEmail;
    }

    public String getLoggedInUserUID() {
        return loggedInUserUID;
    }
    public String getUsername() {
        return username;
    }
    public String getUserEmail() {
        return userEmail;
    }
}
