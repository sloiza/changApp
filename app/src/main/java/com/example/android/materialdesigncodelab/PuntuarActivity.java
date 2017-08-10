package com.example.android.materialdesigncodelab;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class PuntuarActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private Spinner mCalification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuar);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Califica al changuero");

        mCalification = (Spinner)findViewById(R.id.calification);
        mCalification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
                TypedArray a = getResources().obtainTypedArray(R.array.array_calification);
                //mbuttonLoadImage.setImageDrawable(a.getDrawable(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        Button sendCalification = (Button) findViewById(R.id.send_calification);
        sendCalification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PuntuarActivity.this, "¡Gracias por calificar al changuero!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
