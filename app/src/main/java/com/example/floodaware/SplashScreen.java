package com.example.floodaware;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSharedPreference = getSharedPreferences("mySp",MODE_PRIVATE);
                boolean isFirstTime = mSharedPreference.getBoolean("firsttime",true);

                if(isFirstTime){
                    SharedPreferences.Editor editor = mSharedPreference.edit();
                    editor.putBoolean("firsttime",false);
                    editor.commit();
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, Intro.class));
                    SplashScreen.this.finish();
                }else {
                    startActivity(new Intent(SplashScreen.this,Signup.class));
                    finish();
                }


            }
        },3000);
    }
}