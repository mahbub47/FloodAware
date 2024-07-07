package com.example.floodaware;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class SettingFragment extends Fragment {

    TextView logoutTV;
    TextView devRedirect;
    TextView userNameTV,userPhoneTV;
    public static final String SHARED_PREFS_03 = "username";
    public static final String SHARED_PREFS_04 = "userphone";
    SharedPreferences nSharedPreference,pSharedPreference;
    ImageView editprofileIV,profileIV;
    Context ctx;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageUri;
    Bitmap profImg;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ctx = this.getActivity().getApplicationContext();

        profImg = ((HomeScreen)getActivity()).passImageFromHome();

        profileIV = view.findViewById(R.id.porifileIV);

        setImagePro();

        userNameTV = view.findViewById(R.id.userNameTV);
        userPhoneTV = view.findViewById(R.id.userPhoneTV);
        nSharedPreference = this.getActivity().getSharedPreferences(SHARED_PREFS_03,MODE_PRIVATE);
        String userName = nSharedPreference.getString(SHARED_PREFS_03,"Username");
        userNameTV.setText(userName);

        pSharedPreference = this.getActivity().getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
        String userPhone = pSharedPreference.getString(SHARED_PREFS_04,"User phone");
        userPhoneTV.setText(userPhone);

        logoutTV = view.findViewById(R.id.logoutTV);
        logoutTV.setOnClickListener(v -> {
            ((HomeScreen)getActivity()).logout();
        });

        devRedirect = view.findViewById(R.id.devsInfoTV);
        devRedirect.setOnClickListener(v -> {
            startActivity(new Intent(ctx,DevInfoScreen.class));
            this.getActivity().finish();
        });

        editprofileIV = view.findViewById(R.id.editprofileIV);
        editprofileIV.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), EditprofileScreen.class);
            this.getActivity().startActivity(intent);
            this.getActivity().finish();
        });

        int i = 1;
        System.out.println(i);

        return view;
    }

    private void setImagePro() {

        if (profImg != null){
            profileIV.setImageBitmap(profImg);
        }else {
            profileIV.setImageResource(R.drawable.profileicon);
        }

    }
}