package com.example.floodaware;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

public class HomeScreen extends AppCompatActivity {

    ImageView settingIV;
    ImageView weatherIV;
    ImageView safetyIV;
    ImageView floodhubIV;
    ImageView homeIV;

    private final String SHARED_PREFS_02 = "location";
    public static final String SHARED_PREFS = "loginPrefs";
    SharedPreferences sSharedPreference;
    SharedPreferences wSharedPreference;

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

        homeIV = findViewById(R.id.homeIcontIV);
        loadfragment(new HomeFragment(), 1);

        settingIV = findViewById(R.id.gearIconIV);
        settingIV.setOnClickListener(v -> {
            loadfragment(new SettingFragment(), 0);
        });

        homeIV.setOnClickListener(v -> {
            loadfragment(new HomeFragment(), 0);
        });

        weatherIV = findViewById(R.id.weatherIconIV);
        weatherIV.setOnClickListener(v -> {
            loadfragment(new WeatherFragment(), 0);
        });

        safetyIV = findViewById(R.id.safetyIconIV);
        safetyIV.setOnClickListener(v -> {
            loadfragment(new SafetylistFragment(),0);
        });

        floodhubIV = findViewById(R.id.floodhubIconIV);
        floodhubIV.setOnClickListener(v -> {
            loadfragment(new EmergencyFragment(), 0);
        });

    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    public void loadfragment(Fragment fragment, int flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(flag == 1)
            ft.add(R.id.container, fragment);
        else
            ft.replace(R.id.container, fragment);

        ft.commit();
    }

    public void getLocationDialog() {
        Dialog dialog = new Dialog(HomeScreen.this,R.style.DialogStyle);
        dialog.setContentView(R.layout.setlocation_popup_screen);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_back_shape);
        dialog.show();
        ImageView cancelIV = dialog.findViewById(R.id.cancelIV);
        cancelIV.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Button setLocationBtn = dialog.findViewById(R.id.setLocationBtn);
        EditText setLocationET = dialog.findViewById(R.id.setLocationET);
        setLocationBtn.setOnClickListener(v -> {
            String city = setLocationET.getText().toString();
            wSharedPreference = getSharedPreferences(SHARED_PREFS_02, MODE_PRIVATE);
            SharedPreferences.Editor editor = wSharedPreference.edit();
            editor.putString(SHARED_PREFS_02, city);
            editor.apply();

            FragmentManager fm = getSupportFragmentManager();

            WeatherFragment fragment = (WeatherFragment) fm.findFragmentById(R.id.container);
            fragment.weatherUpdate();

            dialog.dismiss();
        });
    }

    public void logout(){
        sSharedPreference = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sSharedPreference.edit();
        editor.putBoolean(SHARED_PREFS,false);
        editor.apply();

        startActivity(new Intent(HomeScreen.this,Login.class));
        finish();
    }

}