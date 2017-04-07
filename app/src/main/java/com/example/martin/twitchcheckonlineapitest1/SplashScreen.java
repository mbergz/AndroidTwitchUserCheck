package com.example.martin.twitchcheckonlineapitest1;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(SplashScreen.this,MainScreen.class);
            startActivity(i);
            finish();
            }
        },1000);
    }

}
