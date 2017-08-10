package com.example.android.materialdesigncodelab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class PagoActivity extends AppCompatActivity {
    private final int DURACION_SPLASH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pago);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                //Saltar a pantalla puntuar
                Intent puntuar = new Intent(PagoActivity.this,PuntuarActivity.class);
                startActivity(puntuar);
                finish();
            };
        }, DURACION_SPLASH);
    }
}