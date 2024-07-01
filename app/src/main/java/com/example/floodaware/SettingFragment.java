package com.example.floodaware;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingFragment extends Fragment {

    Button logoutBtn;
    TextView devRedirect;
    TextView userNameTV,userPhoneTV;
    public static final String SHARED_PREFS_03 = "username";
    public static final String SHARED_PREFS_04 = "userphone";
    SharedPreferences nSharedPreference,pSharedPreference;

    Context ctx;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ctx = this.getActivity().getApplicationContext();

        userNameTV = view.findViewById(R.id.userNameTV);
        userPhoneTV = view.findViewById(R.id.userPhoneTV);
        nSharedPreference = this.getActivity().getSharedPreferences(SHARED_PREFS_03,MODE_PRIVATE);
        String userName = nSharedPreference.getString(SHARED_PREFS_03,"Username");
        userNameTV.setText(userName);

        pSharedPreference = this.getActivity().getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
        String userPhone = pSharedPreference.getString(SHARED_PREFS_04,"User phone");
        userPhoneTV.setText(userPhone);

        logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            ((HomeScreen)getActivity()).logout();
        });

        devRedirect = view.findViewById(R.id.devsInfoTV);
        devRedirect.setOnClickListener(v -> {
            startActivity(new Intent(ctx,DevInfoScreen.class));
            this.getActivity().finish();
        });

        return view;
    }
}