package com.example.android.materialdesigncodelab;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.materialdesigncodelab.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PerfilPostulanteActivity extends AppCompatActivity {
    private UserModel userM;
    private static Context context;
    private static UserModel currentUser;
    private static Changa currentChanga;

    public static final String ID_USER   = "arg1";
    public static final String ID_CHANGA = "arg2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PerfilPostulanteActivity.context = getApplicationContext();
        setContentView(R.layout.activity_perfil_postulante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String idChanga = getIntent().getStringExtra(ID_CHANGA);
        final String idUser   = getIntent().getStringExtra(ID_USER);

        Button selectChanguero = (Button) findViewById(R.id.button2);
        selectChanguero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Inscribir postulante a la changa y pasarla a enProceso
                Toast.makeText(PerfilPostulanteActivity.this, idUser+" se inscribe a "+idChanga,
                        Toast.LENGTH_SHORT).show();

                // GET USER BY ID.
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(idUser);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(UserModel.class);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        System.out.println("!!!!!!!! Failed to read user:"+ error.toException());
                    }
                };
                database.addValueEventListener(eventListener);

                // GET CHANGA BY ID.
                database = FirebaseDatabase.getInstance().getReference("changas").child(idChanga);
                eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentChanga = dataSnapshot.getValue(Changa.class);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        System.out.println("!!!!!!!! Failed to read changa:"+ error.toException());
                    }
                };
                database.addValueEventListener(eventListener);

                if ( currentUser!=null && currentChanga!=null) {
                    // TODO: Pasar la changa a "enProceso" y definir el user como changuero seleccionado.
                    FirebaseDatabase.getInstance().getReference("changas").child(idChanga).child("status").setValue("enProceso");
                    // Agregar changuero a la changa
                } else {
                    System.out.println("FAILED READING USER AND CHANGA");
                }

            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(idUser);
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userM = dataSnapshot.getValue(UserModel.class);
                if (userM.photo != null && userM.photo != "") {
                    ImageView picture = (ImageView) findViewById(R.id.imageperson);
                    Picasso.with(PerfilPostulanteActivity.getAppContext()).load(userM.photo).into(picture); //si no hay photo de perfil default
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
        return PerfilPostulanteActivity.context;
    }
}
