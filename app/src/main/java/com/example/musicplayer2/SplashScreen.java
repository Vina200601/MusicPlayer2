package com.example.musicplayer2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView animas = findViewById(R.id.splash);

        Glide.with(this).load(R.drawable.ter).into(animas);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(6000);//detik
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }

}



