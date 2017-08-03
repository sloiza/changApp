package com.example.android.materialdesigncodelab;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class AddChanga extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private DatabaseReference mDatabase;
    //private String mpathToImage;
    private Spinner mAddCategory;

    private ImageButton mbuttonLoadImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_changa);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agregar Changa");

        mbuttonLoadImage = (ImageButton) findViewById(R.id.img_changa);
        mbuttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mAddCategory = (Spinner)findViewById(R.id.add_changa_category);

        mAddCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
                TypedArray a = getResources().obtainTypedArray(R.array.changas_imgs);
                mbuttonLoadImage.setImageDrawable(a.getDrawable(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mbuttonLoadImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private void writeNewChanga() {
        AutoCompleteTextView add_title = (AutoCompleteTextView) findViewById(R.id.add_changa_title);
        String title = add_title.getText().toString();

        EditText add_description = (EditText) findViewById(R.id.add_changa_description);
        String description = add_description.getText().toString();

        EditText add_cost = (EditText) findViewById(R.id.add_changa_costo);
        String price = add_cost.getText().toString();

        /*
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println("DOWNLOAD;"+downloadUrl);
            }
        });

        ImageButton add_image = (ImageButton)findViewById(R.id.img_changa);
        Bitmap image = ((BitmapDrawable)add_image.getDrawable()).getBitmap();
        */

        String key = mDatabase.child("Changas").push().getKey();
        Changa changa = new Changa(key, "uid", "nicolas", title, description, price, "today", "jardineria", mAddCategory.getSelectedItemPosition());
        Map<String, Object> changaValues = changa.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/changas/" + key, changaValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

}
