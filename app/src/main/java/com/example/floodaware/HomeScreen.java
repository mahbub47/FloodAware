package com.example.floodaware;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.floodaware.databinding.ActivityHomeScreenBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.File;
import java.io.IOException;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;

    private final String SHARED_PREFS_02 = "location";
    public static final String SHARED_PREFS = "loginPrefs";
    SharedPreferences sSharedPreference;
    SharedPreferences wSharedPreference;
    public static final String SHARED_PREFS_04 = "userphone";
    SharedPreferences pSharedPreference;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap imageDB;


    BottomNavigationView bnv;

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

        getImageFromDB();


        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadfragment(new HomeFragment(), 1);

        binding.bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.news:
                    loadfragment(new HomeFragment(),0);
                    break;
                case R.id.alert:
                    loadfragment(new EmergencyFragment(),0);
                    break;
                case R.id.safety:
                    loadfragment(new SafetylistFragment(),0);
                    break;
                case R.id.weather:
                    loadfragment(new WeatherFragment(),0);
                    break;
                case R.id.setting:
                    loadfragment(new SettingFragment(),0);
                    break;


            }

            return true;
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

    public void getImageFromDB(){
        pSharedPreference = getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
        String userPhone = pSharedPreference.getString(SHARED_PREFS_04,"User phone");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("image/" + userPhone);

        try {
            File localFIle = File.createTempFile("tempfile" ,".jpg");
            storageReference.getFile(localFIle).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageDB = BitmapFactory.decodeFile(localFIle.getAbsolutePath());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageDB = null;
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Bitmap passImageFromHome(){
        return imageDB;
    }
}