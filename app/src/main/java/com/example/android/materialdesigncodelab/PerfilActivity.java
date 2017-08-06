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
import com.squareup.picasso.Picasso;

import java.net.URI;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        String name = "";
        String email = "";
        String description = "";
        String phone =  "";
        String photo = "";
*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(" ======= CURRENT USER ======");
        System.out.println(user.getEmail());
        System.out.println(user.getDisplayName());
        System.out.println(user.getPhotoUrl());
        /*UserModel currentUser = (new UserModel()).getUserById(user.getUid()); //TODO traer de bdd con toda la data del autenticado
        System.out.println("============ Current user =========");

        FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        UserModel user = new UserModel( userFirebase.getEmail(),
                userFirebase.getDisplayName(),
                userFirebase.getEmail(),
                "",
                userFirebase.getPhoneNumber(),
                userFirebase.getPhotoUrl().toString());

        System.out.println(currentUser.toString());*/
        /*if (currentUser != null) {
            System.out.println("============ Hay currentUser ==========");
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
            return;
            //name = R.string.firebase_user_management;
            //email = R.string.emailpassword_status_fmt;
        }*/
System.out.println("============ Cargar perfil =========");
        System.out.println(user.getDisplayName());
        System.out.println(user.getEmail());
        System.out.println(user.getPhotoUrl());
        System.out.println("============ Current user =========");
        ImageView picture = (ImageView) findViewById(R.id.imageperson);
        Picasso.with(this).load(user.getPhotoUrl()).into(picture); //Todo si no hay photo de perfil default
        TextView textName = (TextView) findViewById(R.id.perfil_name);
        textName.setText(user.getDisplayName());
        TextView textDescription= (TextView) findViewById(R.id.perfil_descripcion);
        textDescription.setText("lala"); //// TODO: sacar de base d datos
        TextView textEmail = (TextView) findViewById(R.id.perfil_email);
        textEmail.setText(user.getEmail());

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
