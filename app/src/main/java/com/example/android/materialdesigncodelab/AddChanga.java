package com.example.android.materialdesigncodelab;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddChanga extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_changa);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agregar Changa");

        ImageButton buttonLoadImage = (ImageButton) findViewById(R.id.img_changa);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button add_changa_btn = (Button) findViewById(R.id.ok_add_changa);
        add_changa_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewChanga();
                Toast.makeText(AddChanga.this, "¡Changa creada correctamente!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void writeNewChanga() {
        AutoCompleteTextView add_title = (AutoCompleteTextView) findViewById(R.id.add_changa_title);
        String title = add_title.getText().toString();

        EditText add_description = (EditText) findViewById(R.id.add_changa_description);
        String description = add_description.getText().toString();

        EditText add_cost = (EditText) findViewById(R.id.add_changa_costo);
        String price = add_cost.getText().toString();

        Spinner add_category = (Spinner)findViewById(R.id.add_changa_category);
        String category = add_category.getSelectedItem().toString();

        ImageButton add_image = (ImageButton)findViewById(R.id.img_changa);
        Bitmap image = ((BitmapDrawable)add_image.getDrawable()).getBitmap();

        String key = mDatabase.child("Changas").push().getKey();
        Changa changa = new Changa(key, "uid", "nicolas", title, description, price, "today", "nullPicture", category);
        Map<String, Object> changaValues = changa.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/changas/" + key, changaValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageButton buttonLoadImage = (ImageButton) findViewById(R.id.img_changa);
            buttonLoadImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
}
