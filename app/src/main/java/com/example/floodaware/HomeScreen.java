package com.example.floodaware;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {

    ImageView settingIV;
    ImageView weatherIV;
    TextView userNameTV,userPhoneTV;
    public static final String SHARED_PREFS_03 = "username";
    public static final String SHARED_PREFS_04 = "userphone";
    SharedPreferences nSharedPreference,pSharedPreference;
    ImageView safetyIV;
    ImageView floodhubIV;
    ImageView profileIV;

    CardView card01,card02;
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

        userNameTV = findViewById(R.id.userNameTV);
        userPhoneTV = findViewById(R.id.userPhoneTV);
        nSharedPreference = getSharedPreferences(SHARED_PREFS_03,MODE_PRIVATE);
        String userName = nSharedPreference.getString(SHARED_PREFS_03,"Username");
        userNameTV.setText(userName);

        pSharedPreference = getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
        String userPhone = pSharedPreference.getString(SHARED_PREFS_04,"User phone");
        userPhoneTV.setText(userPhone);

        card01 = findViewById(R.id.cardView01);
        card01.setOnClickListener(v -> {
            gotoUrl("https://www.nssl.noaa.gov/education/svrwx101/floods/#:~:text=Floods%20can%20happen%20during%20heavy,days%2C%20weeks%2C%20or%20longer.");
        });

        card02 = findViewById(R.id.card02);
        card02.setOnClickListener(v -> {
            gotoUrl("https://www.who.int/health-topics/floods#tab=tab_1");
        });

        safetyIV = findViewById(R.id.safetyIconIV);
        safetyIV.setOnClickListener(v -> {
            gotoUrl("https://www.mass.gov/info-details/flood-safety-tips");
        });

        floodhubIV = findViewById(R.id.floodhubIconIV);
        floodhubIV.setOnClickListener(v -> {
            gotoUrl("https://sites.research.google/floods/l/13.981345397534573/35.05681284530171/3.2105");
        });

        profileIV = findViewById(R.id.profileIV);
        profileIV.setOnClickListener(v -> {
            startActivity(new Intent(HomeScreen.this,SettingScreen.class));
            finish();
        });

        userNameTV.setOnClickListener(v -> {
            startActivity(new Intent(HomeScreen.this,SettingScreen.class));
            finish();
        });

        userPhoneTV.setOnClickListener(v -> {
            startActivity(new Intent(HomeScreen.this,SettingScreen.class));
            finish();
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}