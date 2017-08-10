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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
public class DetailPostulacionActivaActivity extends AppCompatActivity {

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

    // private Button mPostularButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_postulacion_activa);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_pp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar   = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_pp);
        placeLocation       = (TextView)  findViewById(R.id.place_location_pp);
        placeDetail         = (TextView)  findViewById(R.id.place_detail_pp);
        placeImage          = (ImageView) findViewById(R.id.image_pp);
        category            = (TextView)  findViewById(R.id.categoria_changa_pp);

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
