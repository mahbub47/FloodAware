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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {

    TextView loginRedirect;
    Button signupBtn;
    EditText signupName,signupEmail,signupPhone,signupPass,signupConPass;
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences lSharedPreference;
    public static final String SHARED_PREFS = "loginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        checkUser();

        signupName = findViewById(R.id.signupNamelET);
        signupEmail = findViewById(R.id.signupEmailET);
        signupPhone = findViewById(R.id.signupPhoneET);
        signupPass = findViewById(R.id.signupPassET);
        signupConPass = findViewById(R.id.signupConPassET);

        loginRedirect = findViewById(R.id.loginRedirectTV);
        loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(Signup.this,Login.class));
            finish();
        });

        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(v -> {
            if(validateUsername() && validateEmail() && validatePhone() && validatePassword() && validateConPassword()){
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String phone = signupPhone.getText().toString();
                String pass = signupPass.getText().toString();

                Query checkuser = reference.orderByChild("phone").equalTo(phone);

                HelperClass helper = new HelperClass(name,email,phone,pass);

                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(Signup.this,"This number is already registered",Toast.LENGTH_SHORT).show();
                            signupPhone.setError("Number already registered");
                            signupPhone.requestFocus();
                        }else {
                            reference.child(phone).setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Signup.this,"Successfully account created",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Signup.this,Login.class));
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    public boolean validateEmail(){
        String email = signupEmail.getText().toString().trim();
        if(!email.isEmpty()){
            if(email.matches("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$"))
            {
                return true;
            }else {
                signupEmail.setError("invalid email");
                return false;
            }

        }else {
            signupEmail.setError("this field must be filled");
            return false;
        }
    }

    public boolean validateUsername(){
        String userName = signupName.getText().toString().trim();
        if(!userName.isEmpty()){
            signupName.setError(null);
            return true;
        }else {
            signupName.setError("this field must be filled");
            return false;
        }
    }

    public boolean validatePhone(){
        String phone = signupPhone.getText().toString();
        if(!phone.isEmpty()){
            if(phone.matches("^(\\+88|0088)?(01){1}[3456789]{1}(\\d){8}$"))
            {
                return true;
            }else {
                signupPhone.setError("invalid phone number");
                return false;
            }
        }else {
            signupPhone.setError("this field must be filled");
            return false;
        }
    }

    public boolean validatePassword(){
        String pass = signupPass.getText().toString();
        if(!pass.isEmpty()){
            if(pass.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"))
            {
                return true;
            }else {
                signupPass.setError("invalid password");
                return false;
            }
        }else {
            signupPass.setError("this field must be filled");
            return false;
        }
    }

    public boolean validateConPassword(){
        String pass = signupPass.getText().toString();
        String conPass = signupConPass.getText().toString();
        if(!pass.isEmpty()){
            if(conPass.equals(pass))
            {
                return true;
            }else {
                signupConPass.setError("password didn't match");
                return false;
            }
        }else {
            signupConPass.setError("this field must be filled");
            return false;
        }
    }

    private void checkUser() {
        lSharedPreference = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Boolean isLogedIn = lSharedPreference.getBoolean(SHARED_PREFS,false);
        if(isLogedIn){
            startActivity(new Intent(Signup.this,HomeScreen.class));
            finish();
        }
    }

}