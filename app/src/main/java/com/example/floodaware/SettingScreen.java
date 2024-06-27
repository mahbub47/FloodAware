package com.example.floodaware;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingScreen extends AppCompatActivity {

    ImageView backSetting;
    Button logoutBtn;
    SharedPreferences sSharedPreference;
    TextView devInfoRedirect;
    TextView userNameTV,userPhoneTV;
    public static final String SHARED_PREFS_03 = "username";
    public static final String SHARED_PREFS_04 = "userphone";
    SharedPreferences nSharedPreference,pSharedPreference;

    public static final String SHARED_PREFS = "loginPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backSetting = findViewById(R.id.backIconSetting);
        backSetting.setOnClickListener(v -> {
            startActivity(new Intent(SettingScreen.this,HomeScreen.class));
            finish();
        });

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {

            sSharedPreference = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editor = sSharedPreference.edit();
            editor.putBoolean(SHARED_PREFS,false);
            editor.apply();

            startActivity(new Intent(SettingScreen.this,Login.class));
            finish();
        });

        devInfoRedirect = findViewById(R.id.devsInfoTV);
        devInfoRedirect.setOnClickListener(v -> {
            startActivity(new Intent(SettingScreen.this,DevInfoScreen.class));
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
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingScreen.this, HomeScreen.class));
        finish();
    }
}