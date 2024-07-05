package com.example.floodaware;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EmergencyFragment extends Fragment {

    Button floodhubBtn, emergencyCallBtn,emergencyModeBtn;
    String phone_number = "16163";

    public EmergencyFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        floodhubBtn = view.findViewById(R.id.floodhubBtn);
        floodhubBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(),FloodhubActivity.class);
            this.getActivity().startActivity(intent);
        });

        emergencyCallBtn = view.findViewById(R.id.callEmergencyBtn);
        emergencyCallBtn.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_number));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        emergencyModeBtn = view.findViewById(R.id.emergencyModeBtn);
        emergencyModeBtn.setOnClickListener(v -> {
            this.getActivity().startActivity(new Intent(this.getContext(),EmergencySteps.class));
            this.getActivity().finish();
        });

        return view;
    }

}