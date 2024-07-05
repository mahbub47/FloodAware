package com.example.floodaware;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SafetylistFragment extends Fragment {

    Button beforeFloodBtn,duringFloodBtn,afterFloodBtn;
    public SafetylistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safetylist, container, false);

        beforeFloodBtn = view.findViewById(R.id.beforeFloodBtn);
        beforeFloodBtn.setOnClickListener(v -> {
            this.getActivity().startActivity(new Intent(this.getContext(),BeforeFloodScreen.class));
            this.getActivity().finish();
        });

        duringFloodBtn = view.findViewById(R.id.duringFloodBtn);
        duringFloodBtn.setOnClickListener(v -> {
            this.getActivity().startActivity(new Intent(this.getContext(),DuringFloodScreen.class));
            this.getActivity().finish();
        });

        afterFloodBtn = view.findViewById(R.id.afterFloodBtn);
        afterFloodBtn.setOnClickListener(v -> {
            this.getActivity().startActivity(new Intent(this.getContext(),AfterFloodScreen.class));
            this.getActivity().finish();
        });

        return view;
    }
}