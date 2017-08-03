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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // TODO: Name currentUser
        // TODO: Description currentUser
        // TODO: Email currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            //description = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        } else {
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
