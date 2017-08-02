package com.example.android.materialdesigncodelab;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String uid;
    public String name;
    public String email;
    public String description;
    public String phone;
    public String photo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String name, String email, String description, String phone, String photo) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.description = description;
        this.phone = phone;
        this.photo = photo;
    }

}