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
import android.icu.util.BuddhistCalendar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.android.materialdesigncodelab.ChangasPorCategoria.ContentAdapter.LENGTH;
import static com.example.android.materialdesigncodelab.ChangasPorCategoria.ContentAdapter.mIDS;

/**
 * Provides UI for the view with Cards.
 */
public class ChangasPorCategoria extends Fragment {

    public static Integer idCategoria = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle args = getArguments();
        idCategoria = args.getInt("category_id",0);
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
                public void onClick(final View v) {
                    final Context context = v.getContext();
                    int nPos = LENGTH-getAdapterPosition()-1;
                    final String idChanga = mIDS[nPos];
                    DatabaseReference changa = FirebaseDatabase.getInstance().getReference("changas/"+idChanga+"/uid");

                    ValueEventListener myListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (dataSnapshot.getValue().toString().equals(userId)) {
                                Snackbar.make(v, "No puedes postularte a tu propia changa!",
                                        Snackbar.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(context, DetailActivity2.class);
                                intent.putExtra(DetailActivity2.EXTRA_POSITION, idChanga);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    changa.addListenerForSingleValueEvent(myListener);
                }
            });

            // Adding Snackbar to Action Button inside card
            Button button = (Button)itemView.findViewById(R.id.action_button);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    final Context context = v.getContext();
                    int nPos = LENGTH-getAdapterPosition()-1;
                    final String idChanga = mIDS[nPos];

                    DatabaseReference changa = FirebaseDatabase.getInstance().getReference("changas/"+idChanga+"/uid");

                    ValueEventListener myListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (dataSnapshot.getValue().toString().equals(userId)) {
                                Snackbar.make(v, "No puedes postularte a tu propia changa!",
                                        Snackbar.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(context, DetailActivity2.class);
                                intent.putExtra(DetailActivity2.EXTRA_POSITION, idChanga);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    changa.addListenerForSingleValueEvent(myListener);
                }
            });

            ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Agregado a mis Changas Favoritas!",
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

        public static  String[] mIDS;
        private static String[] mChangasTitle;
        private static String[] mChangasDescription;
        private final Drawable[] mChangasPictures;

        public ContentAdapter(Context context) {
            mChangasReference = FirebaseDatabase.getInstance().getReference("changas");
            ValueEventListener changaListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> ids = new ArrayList<String>();
                    List<String> titles = new ArrayList<String>();
                    List<String> descriptions = new ArrayList<String>();

                    LENGTH = 0;
                    for (DataSnapshot changaSnapshot: dataSnapshot.getChildren()) {
                        Changa changa = changaSnapshot.getValue(Changa.class);
                        System.out.println("idCategoria "+idCategoria);
                        System.out.println("categoria changa: "+changa.category);

                        if(changa.category == idCategoria) {
                            System.out.println("ENTRO");
                            ids.add(changa.id);
                            titles.add(changa.title);
                            System.out.println("titles0 size: "+titles.size());
                            descriptions.add(changa.body);
                            LENGTH++;
                        }
                    }
                    mIDS = new String[LENGTH];
                    mChangasTitle = new String[LENGTH];
                    mChangasDescription = new String[LENGTH];

                    ids.toArray(mIDS);
                    System.out.println("titles1 size: "+titles.size());
                    titles.toArray(mChangasTitle);
                    System.out.println("mChangasTitle size: "+mChangasTitle.length);
                    descriptions.toArray(mChangasDescription);
                    System.out.println("mChangasDescription size: "+mChangasDescription.length);
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
            holder.picture.setImageDrawable(mChangasPictures[idCategoria]);
            holder.name.setText(mChangasTitle[nPos]);
            holder.description.setText(mChangasDescription[nPos]);

            System.out.println("mChangasTitle size2: "+mChangasTitle.length);
            System.out.println("mChangasDescription size2: "+mChangasDescription.length);

        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
