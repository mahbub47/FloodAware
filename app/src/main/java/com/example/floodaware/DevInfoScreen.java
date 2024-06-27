package com.example.floodaware;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DevInfoScreen extends AppCompatActivity {

    ImageView backDevinfo;
    ImageView instaIV,linkedinIV,githubIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dev_info_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backDevinfo = findViewById(R.id.backIconDevInfo);
        backDevinfo.setOnClickListener(v -> {
            startActivity(new Intent(DevInfoScreen.this,SettingScreen.class));
            finish();
        });

        instaIV = findViewById(R.id.instaIV);
        linkedinIV = findViewById(R.id.linkedinIV);
        githubIV = findViewById(R.id.githubIV);

        instaIV.setOnClickListener(v -> {
            gotoUrl("https://www.instagram.com/am.ashikk/");
        });

        linkedinIV.setOnClickListener(v -> {
            gotoUrl("https://www.linkedin.com/in/mahbub-ahmed-ashik/");
        });

        githubIV.setOnClickListener(v -> {
            gotoUrl("https://github.com/mahbub47");
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DevInfoScreen.this, SettingScreen.class));
        finish();
    }
}