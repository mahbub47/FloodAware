package com.example.floodaware;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SafetyList extends AppCompatActivity {

    ImageView backSafetyIV;
    Button beforeFloodBtn,duringFloodBtn,afterFloodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_safety_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backSafetyIV = findViewById(R.id.backIconSafetylist);
        backSafetyIV.setOnClickListener( v -> {
            startActivity(new Intent(SafetyList.this, HomeScreen.class));
            finish();
        });

        beforeFloodBtn = findViewById(R.id.beforeFloodBtn);
        duringFloodBtn = findViewById(R.id.duringFloodBtn);
        afterFloodBtn = findViewById(R.id.afterFloodBtn);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SafetyList.this, HomeScreen.class));
        finish();
    }
}