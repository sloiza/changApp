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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.android.materialdesigncodelab.CardContentChangaActivaFragment.ContentAdapter.LENGTH;
import static com.example.android.materialdesigncodelab.CardContentChangaActivaFragment.ContentAdapter.mIDS;

/**
 * Provides UI for the view with Cards.
 */
public class CardContentChangaActivaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView description;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int nPos = LENGTH-getAdapterPosition()-1;
                    String idChanga = mIDS[nPos];

                    Intent intent = new Intent(context, DetailActivity3.class);
                    intent.putExtra(DetailActivity3.EXTRA_POSITION, idChanga);
                    context.startActivity(intent);
                }
            });

            // Adding Snackbar to Action Button inside card
            Button button = (Button)itemView.findViewById(R.id.action_button);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Action is pressed",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Added to Favorite",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Share article",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private DatabaseReference mChangasReference;

        public static int LENGTH = 0;

        public static String[] mIDS;
        private static String[] mChangasTitle;
        private static String[] mChangasDescription;
        private static Integer[] mChangasCategory;
        private final Drawable[] mChangasPictures;

        private static String currentIdInCard;

        public ContentAdapter(Context context) {
            mChangasReference = FirebaseDatabase.getInstance().getReference("changas");
            ValueEventListener changaListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    LENGTH = 0;
                    currentIdInCard = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (DataSnapshot changaSnapshot: dataSnapshot.getChildren()) {
                        Changa changa = changaSnapshot.getValue(Changa.class);
                        if (changa.uid.equals(currentIdInCard) && changa.status.equals("activa")) {
                            LENGTH++;
                        }
                    }
                    mIDS = new String[LENGTH];
                    mChangasTitle = new String[LENGTH];
                    mChangasDescription = new String[LENGTH];
                    mChangasCategory = new Integer[LENGTH];

                    List<String> ids = new ArrayList<String>();
                    List<String> titles = new ArrayList<String>();
                    List<String> descriptions = new ArrayList<String>();
                    List<Integer> categories = new ArrayList<Integer>();

                    for (DataSnapshot changaSnapshot: dataSnapshot.getChildren()) {
                        Changa changa = changaSnapshot.getValue(Changa.class);
                        if (changa.uid.equals(currentIdInCard) && changa.status.equals("activa")) {
                            ids.add(changa.id);
                            titles.add(changa.title);
                            descriptions.add(changa.body);
                            categories.add(changa.category);
                        }
                    }
                    ids.toArray(mIDS);
                    titles.toArray(mChangasTitle);
                    descriptions.toArray(mChangasDescription);
                    categories.toArray(mChangasCategory);
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            mChangasReference.addValueEventListener(changaListener);

            Resources resources = context.getResources();
            TypedArray a = resources.obtainTypedArray(R.array.changas_imgs);
            mChangasPictures = new Drawable[a.length()];
            for (int i = 0; i < mChangasPictures.length; i++) {
                mChangasPictures[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int nPos = LENGTH-position-1;
            holder.picture.setImageDrawable(mChangasPictures[mChangasCategory[nPos]]);
            holder.name.setText(mChangasTitle[nPos]);
            holder.description.setText(mChangasDescription[nPos]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
