package com.erolakgul.stockhaus.controller.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.erolakgul.stockhaus.R;
import com.erolakgul.stockhaus.models.helpers.helpersForApp;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

public class MainActivity extends AppCompatActivity {

    servicePoints point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // while opening page gets instance from service class
        point = servicePoints.getInstance();

        // animation
        final View view = findViewById(R.id.image_logo);
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_for_initial_image);
        // animation

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // anim klasöründeki animasyon başlatılsın
                        view.startAnimation(anim);

                        //Login Ekranına gönder
                        ConnectLoginScreen();
                    }
                }, 500); /*EAKGUL 23122019 LOGIN İŞLEMLERİ SONRASI 2000 YE ÇIKARILACAK*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ConnectLoginScreen();
    }

    // go login screen
    private void ConnectLoginScreen() {
        // login olurken kendimizi kaydetiyoruz db ye
        String _rslt = helpersForApp.InitialUser(getApplicationContext(),point);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
