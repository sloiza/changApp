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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.materialdesigncodelab.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailActivity3 extends AppCompatActivity {

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
    private Spinner postulantes;

    public static String[] namesPostulantes;
    public static String[] idsPostulantes;
    public static List<String> namesP;
    public static List<String> idsP;
    public static int LENGTH = 0;


    // private Button mPostularButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_3);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_p));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar   = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_p);
        placeLocation       = (TextView)  findViewById(R.id.place_location_p);
        placeDetail         = (TextView)  findViewById(R.id.place_detail_p);
        placeImage          = (ImageView) findViewById(R.id.image_p);
        category            = (TextView)  findViewById(R.id.categoria_changa_p);
        postulantes         = (Spinner)   findViewById(R.id.place_postulante_p);

        final String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String idChanga = getIntent().getStringExtra(EXTRA_POSITION);

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

                        namesP = new ArrayList<String>();
                        idsP   = new ArrayList<String>();

                        final ArrayList<UserModel> arrayPostulantes = new ArrayList<>();
                        arrayPostulantes.add(new UserModel("","Seleccionar...","","","",""));

                        for(final DataSnapshot postulante : changaSnapshot.child("postulantes").getChildren()) {
                            LENGTH++;
                            String postulanteId = postulante.getKey();
                            System.out.println(postulanteId);
                            mUsersReference.child(postulanteId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    System.out.println(dataSnapshot.getValue(UserModel.class).name);
                                    System.out.println(dataSnapshot.getValue(UserModel.class).uid);
                                    arrayPostulantes.add(dataSnapshot.getValue(UserModel.class));
                                    namesP.add(dataSnapshot.getValue(UserModel.class).name);
                                    idsP.add(dataSnapshot.getValue(UserModel.class).uid);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                }
                            });
                        }
                        System.out.println(LENGTH);

                        namesPostulantes    = new String[LENGTH];
                        idsPostulantes      = new String[LENGTH];

                        namesP.toArray(namesPostulantes);
                        idsP.toArray(idsPostulantes);

                        ArrayAdapter<UserModel> adapter = new ArrayAdapter<UserModel>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayPostulantes);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        postulantes.setAdapter(adapter);
                        postulantes.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
                                if ( (pos!=0) && (id!=0)) {
                                    Object item = arg0.getItemAtPosition(pos);
                                    String idSelected   = ((UserModel) item).uid;
                                    String nameSelected = ((UserModel) item).name;
                                    //Toast.makeText(getBaseContext(), nameSelected, Toast.LENGTH_SHORT).show();
                                    Intent verPerfilPostulante = new Intent(DetailActivity3.this,PerfilPostulanteActivity.class);
                                    verPerfilPostulante.putExtra(PerfilPostulanteActivity.ID_USER, idSelected);
                                    verPerfilPostulante.putExtra(PerfilPostulanteActivity.ID_CHANGA, idChanga);
                                    startActivity(verPerfilPostulante);
                                    finish();
                                    // Toast.makeText(getBaseContext(), nameSelected, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });

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
