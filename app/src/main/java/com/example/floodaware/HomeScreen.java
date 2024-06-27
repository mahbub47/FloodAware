package com.example.floodaware;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {

    ImageView settingIV;
    ImageView weatherIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settingIV = findViewById(R.id.gearIconIV);
        settingIV.setOnClickListener(v -> {
            startActivity(new Intent(HomeScreen.this,SettingScreen.class));
            finish();
        });

        weatherIV = findViewById(R.id.weatherIconIV);
        weatherIV.setOnClickListener(v -> {
            startActivity(new Intent(HomeScreen.this,WeatherUpdate.class));
            finish();
        });
    }
}