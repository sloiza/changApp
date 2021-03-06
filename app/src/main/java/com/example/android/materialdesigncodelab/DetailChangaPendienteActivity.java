/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.materialdesigncodelab;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.materialdesigncodelab.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailChangaPendienteActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "";
    private DatabaseReference mChangasReference;
    private DatabaseReference mUsersReference;
    //private DatabaseReference mChangasReference2;

    private CollapsingToolbarLayout collapsingToolbar;
    private TextView placeLocation;
    private TextView placeDetail;
    private Integer changaCategory;
    private Drawable[] changasImgs;
    private ImageView placeImage;
    private TextView category;
    private TextView changuero;

    private Button finalizarChangaButton;

    // private Button mPostularButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_changa_pendiente);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_cp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar     = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_cp);
        placeLocation         = (TextView)  findViewById(R.id.place_location_cp);
        placeDetail           = (TextView)  findViewById(R.id.place_detail_cp);
        placeImage            = (ImageView) findViewById(R.id.image_cp);
        category              = (TextView)  findViewById(R.id.categoria_changa_cp);
        changuero             = (TextView)  findViewById(R.id.place_changuero_cp);

        finalizarChangaButton = (Button)  findViewById(R.id.ok_finnaly_changa);

        FloatingActionButton enviarMensaje = (FloatingActionButton) findViewById(R.id.messageChangero);
        enviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enviarMensajeAChanguero = new Intent(DetailChangaPendienteActivity.this,MensajesActivity.class);
                startActivity(enviarMensajeAChanguero);
            }
        });


        final String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String idChanga = getIntent().getStringExtra(EXTRA_POSITION);

        finalizarChangaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("changas").child(idChanga).child("status").setValue("finalizada");
                //Saltar a pantalla pago (splash)
                Intent pago = new Intent(DetailChangaPendienteActivity.this,PagoActivity.class);
                startActivity(pago);
                finish();
            }
        });

        Resources resources = getResources();
        TypedArray a = resources.obtainTypedArray(R.array.changas_imgs);
        changasImgs = new Drawable[a.length()];
        for (int i = 0; i < changasImgs.length; i++) {
            changasImgs[i] = a.getDrawable(i);
        }
        a.recycle();

        mUsersReference     = FirebaseDatabase.getInstance().getReference("users");
        mChangasReference   = FirebaseDatabase.getInstance().getReference("changas");
        ValueEventListener changaListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot changaSnapshot: dataSnapshot.getChildren()) {

                    if(changaSnapshot.getKey().equals(idChanga)) {

                        collapsingToolbar.setTitle(changaSnapshot.getValue(Changa.class).title);
                        placeDetail.setText(changaSnapshot.getValue(Changa.class).body);
                        placeLocation.setText(changaSnapshot.getValue(Changa.class).author);


                        String idChanguero = changaSnapshot.child("changuero").getValue(String.class);

                        DatabaseReference database = mUsersReference.child(idChanguero);
                        ValueEventListener eventListener = new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                changuero.setText(dataSnapshot.getValue(UserModel.class).name);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                System.out.println("!!!!!!!! Failed to read user:"+ error.toException());
                            }
                        };
                        database.addValueEventListener(eventListener);


                        changaCategory = changaSnapshot.getValue(Changa.class).category;
                        placeImage.setImageDrawable(changasImgs[changaCategory]);

                        Resources resources = getResources();
                        category.setText((resources.getStringArray(R.array.array_categories))[changaCategory].toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mChangasReference.addValueEventListener(changaListener);
    }
}
