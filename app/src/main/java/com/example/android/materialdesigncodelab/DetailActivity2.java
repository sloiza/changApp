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

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
public class DetailActivity2 extends AppCompatActivity {

    public static final String EXTRA_POSITION = "";
    private DatabaseReference mChangasReference;

    private CollapsingToolbarLayout collapsingToolbar;
    private TextView placeLocation;
    private TextView placeDetail;
    private Integer changaCategory;
    private Drawable[] changasImgs;
    private ImageView placeImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        placeLocation =  (TextView) findViewById(R.id.place_location);
        placeDetail = (TextView) findViewById(R.id.place_detail);
        placeImage = (ImageView) findViewById(R.id.image);

        Resources resources = getResources();
        TypedArray a = resources.obtainTypedArray(R.array.changas_imgs);
        changasImgs = new Drawable[a.length()];
        for (int i = 0; i < changasImgs.length; i++) {
            changasImgs[i] = a.getDrawable(i);
        }
        a.recycle();

        final String idChanga = getIntent().getStringExtra(EXTRA_POSITION);

        mChangasReference = FirebaseDatabase.getInstance().getReference("changas");
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


        //placePicutre.setImageDrawable(placePictures.getDrawable(postion % placePictures.length()));
        //placePictures.recycle();
    }
}
