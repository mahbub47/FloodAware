package com.example.floodaware;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextView signupRedirect;
    EditText loginPhone, loginPass;
    Button loginBtn;
    DatabaseReference reference;

    SharedPreferences lSharedPreference;
    public static final String SHARED_PREFS = "loginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkUser();

        loginPhone = findViewById(R.id.loginPhoneET);
        loginPass = findViewById(R.id.loginPassET);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {

            if(validatePhone() && validatePassword()) {

                reference = FirebaseDatabase.getInstance().getReference("users");

                String phone = loginPhone.getText().toString();
                String pass = loginPass.getText().toString();

                Query checkuser = reference.orderByChild("phone").equalTo(phone);
                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String dbpass = snapshot.child(phone).child("pass").getValue(String.class);
                            if (pass.equals(dbpass)) {
                                lSharedPreference = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = lSharedPreference.edit();
                                editor.putBoolean(SHARED_PREFS, true);
                                editor.apply();

                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, HomeScreen.class));
                                finish();
                            } else {
                                loginPass.setError("Incorrect password");
                                loginPass.requestFocus();
                            }
                        } else {
                            Toast.makeText(Login.this, "User does not exist", Toast.LENGTH_SHORT).show();
                            loginPhone.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        signupRedirect = findViewById(R.id.signupRedirectTV);
        signupRedirect.setOnClickListener(v -> {
            startActivity(new Intent(Login.this,Signup.class));
            finish();
        });
    }

    private void checkUser() {
        lSharedPreference = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        boolean isLogedIn = lSharedPreference.getBoolean(SHARED_PREFS,false);
        if(isLogedIn){
            startActivity(new Intent(Login.this,HomeScreen.class));
            finish();
        }
    }

    public boolean validatePhone(){
        String phone = loginPhone.getText().toString();
        if(!phone.isEmpty()){
            if(phone.matches("^(\\+88|0088)?(01){1}[3456789]{1}(\\d){8}$"))
            {
                return true;
            }else {
                loginPhone.setError("invalid phone number");
                return false;
            }
        }else {
            loginPhone.setError("this field must be filled");
            return false;
        }
    }

    public boolean validatePassword(){
        String pass = loginPass.getText().toString();
        if(!pass.isEmpty()){
            if(pass.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"))
            {
                return true;
            }else {
                loginPass.setError("invalid password");
                return false;
            }
        }else {
            loginPass.setError("this field must be filled");
            return false;
        }
    }
}