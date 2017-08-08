package com.example.android.materialdesigncodelab;

import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class PerfilActivity extends AppCompatActivity {
    private UserModel userM;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PerfilActivity.context = getApplicationContext();
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userM = dataSnapshot.getValue(UserModel.class);
                if (userM.photo != null && userM.photo != "") {
                    ImageView picture = (ImageView) findViewById(R.id.imageperson);
                    Picasso.with(PerfilActivity.getAppContext()).load(userM.photo).into(picture); //si no hay photo de perfil default
                }
                TextView textName = (TextView) findViewById(R.id.perfil_name);
                textName.setText(userM.name);
                TextView textDescription= (TextView) findViewById(R.id.perfil_descripcion);
                textDescription.setText(userM.description);
                TextView textEmail = (TextView) findViewById(R.id.perfil_email);
                textEmail.setText(userM.email);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("!!!!!!!! Failed to read user:"+ error.toException());
            }
        };
        database.addValueEventListener(eventListener);
    }

    public static Context getAppContext() {
        return PerfilActivity.context;
    }
}
