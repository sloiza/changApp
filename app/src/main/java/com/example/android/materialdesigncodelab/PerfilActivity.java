package com.example.android.materialdesigncodelab;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.materialdesigncodelab.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = "";
        String email = "";
        String description = "";
        String phone =  "";
        String photo = "";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserModel currentUser = (new UserModel()).getUserById(user.getUid());

        if (currentUser != null) {
            System.out.println("Hay currentUser");
            // Name, email address, and profile photo Url
            String uid = currentUser.uid;
            name = currentUser.name;
            email = currentUser.email;
            description = currentUser.description;
            phone = currentUser.phone;
            photo = currentUser.photo;

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            //          uid = user.getUid();
        } else {
            System.out.println("No hay currentUser");
            //name = R.string.firebase_user_management;
            //email = R.string.emailpassword_status_fmt;
        }

        //ImageView picture = (ImageView) findViewById(R.id.app_bar);
        //picture.setBackground(R.drawable.garden);
        TextView textName = (TextView) findViewById(R.id.perfil_name);
        textName.setText(name);
        TextView textDescription= (TextView) findViewById(R.id.perfil_descripcion);
        textDescription.setText(description);
        TextView textEmail = (TextView) findViewById(R.id.perfil_email);
        textEmail.setText(email);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
