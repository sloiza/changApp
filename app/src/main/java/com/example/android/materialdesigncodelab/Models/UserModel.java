package com.example.android.materialdesigncodelab.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {

    // Write a message to the database
    private FirebaseDatabase database;
    private DatabaseReference usersReference;

    public String uid;
    public String name;
    public String email;
    public String description;
    public String phone;
    public String photo;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }

    public UserModel(String uid, String name, String email, String description, String phone, String photo) {
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");

        this.uid = uid;
        this.name = name;
        this.email = email;
        this.description = description;
        this.phone = phone;
        this.photo = photo;
    }

    public void writeNewUser(UserModel user) {
        //UserModel user = new UserModel(uid, name, email, description, phone, photo);

        usersReference.child("users").child(uid).setValue(user);
    }

}