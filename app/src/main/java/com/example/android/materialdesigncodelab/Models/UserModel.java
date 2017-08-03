package com.example.android.materialdesigncodelab.Models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    private String auxId;
    private static UserModel[] users;
    private static int countUsers;


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
        DatabaseReference dataUsers = usersReference.child("users");

        FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        uid = userFirebase.getUid();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean result = false;

                List<UserModel> usersModels = new ArrayList<UserModel>();
                countUsers = (int)dataSnapshot.getChildrenCount();
                System.out.println(countUsers);
                users = new UserModel[countUsers];

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //System.out.println(userSnapshot);
                    usersModels.add(userSnapshot.getValue(UserModel.class));
                    if (userSnapshot.getKey().equals(uid)) {
                        result = true;
                    }
                }
                usersModels.toArray(users);
                if (!result) {
                    //System.out.println("Add user");
                    usersReference.child("users").child(uid).setValue(new UserModel(uid,name,email,description,phone,photo));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        };
        dataUsers.addListenerForSingleValueEvent(userListener);

    }

    public UserModel getUserById(String userId) {
        for (int i = 0; i < countUsers; i++) {
            if (users[i].uid.equals(userId)) {
                return users[i];
            }
        }
        return null;
    }

}