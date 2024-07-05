package com.example.floodaware;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class EditprofileScreen extends AppCompatActivity {

    ImageView back;

    public static final String SHARED_PREFS_03 = "username";
    public static final String SHARED_PREFS_04 = "userphone";
    public  static final String SHARED_PREFS_O5 = "useremail";
    public  static final String SHARED_PREFS_O6 = "userpass";
    SharedPreferences nSharedPreference,eSharedPreference,pSharedPreference,passSharedPreference;

    EditText nameET,emailET;
    Button saveBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView profileIV;
    FloatingActionButton floatBtn;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editprofile_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setImagePro();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profileIV = findViewById(R.id.editdprofilePicIV);
        floatBtn = findViewById(R.id.floatingActionButton);

        floatBtn.setOnClickListener(v -> {

            ImagePicker.with(EditprofileScreen.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

        back = findViewById(R.id.backIconEditprofile);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(EditprofileScreen.this,HomeScreen.class);
            startActivity(intent);
            finish();
        });

        nameET = findViewById(R.id.nameEditET);
        emailET = findViewById(R.id.emailEditET);

        nSharedPreference = getSharedPreferences(SHARED_PREFS_03,MODE_PRIVATE);
        String userName = nSharedPreference.getString(SHARED_PREFS_03,"Username");
        nameET.setText(userName);

        eSharedPreference = getSharedPreferences(SHARED_PREFS_O5,MODE_PRIVATE);
        String userPhone = eSharedPreference.getString(SHARED_PREFS_O5,"User phone");
        emailET.setText(userPhone);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {
            if(validateUsername() && validateEmail()){
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = nameET.getText().toString();
                String email = emailET.getText().toString();

                pSharedPreference = getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
                String phone = pSharedPreference.getString(SHARED_PREFS_04,"User phone");

                passSharedPreference = getSharedPreferences(SHARED_PREFS_O6,MODE_PRIVATE);
                String pass = passSharedPreference.getString(SHARED_PREFS_O6,"User pass");

                Query checkuser = reference.orderByChild("email").equalTo(email);

                HelperClass helper = new HelperClass(name,email,phone,pass);

                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(EditprofileScreen.this,"This email is already registered",Toast.LENGTH_SHORT).show();
                            emailET.setError("email already registered");
                            emailET.requestFocus();
                        }else {
                            reference.child(phone).setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    nSharedPreference = getSharedPreferences(SHARED_PREFS_03,MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = nSharedPreference.edit();
                                    editor1.putString(SHARED_PREFS_03,name);
                                    editor1.apply();

                                    pSharedPreference = getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = pSharedPreference.edit();
                                    editor2.putString(SHARED_PREFS_04,phone);
                                    editor2.apply();

                                    eSharedPreference = getSharedPreferences(SHARED_PREFS_O5,MODE_PRIVATE);
                                    SharedPreferences.Editor editor3 = eSharedPreference.edit();
                                    editor3.putString(SHARED_PREFS_O5,email);
                                    editor3.apply();

                                    passSharedPreference = getSharedPreferences(SHARED_PREFS_O6,MODE_PRIVATE);
                                    SharedPreferences.Editor editor4 = passSharedPreference.edit();
                                    editor4.putString(SHARED_PREFS_O6,pass);
                                    editor4.apply();
                                    Toast.makeText(EditprofileScreen.this,"Profile updated",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditprofileScreen.this,HomeScreen.class));
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

    private void setImagePro() {

        pSharedPreference = getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
        String userPhone = pSharedPreference.getString(SHARED_PREFS_04,"User phone");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("image/" + userPhone);

        try {
            File localFIle = File.createTempFile("tempfile" ,".jpg");
            storageReference.getFile(localFIle).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap = BitmapFactory.decodeFile(localFIle.getAbsolutePath());
                    profileIV.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                        profileIV.setImageResource(R.drawable.profileicon);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean validateUsername(){
        String userName = nameET.getText().toString().trim();
        if(!userName.isEmpty()){
            nameET.setError(null);
            return true;
        }else {
            nameET.setError("this field must be filled");
            return false;
        }
    }

    public boolean validateEmail(){
        String email = emailET.getText().toString().trim();
        if(!email.isEmpty()){
            if(email.matches("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$"))
            {
                return true;
            }else {
                emailET.setError("invalid email");
                return false;
            }

        }else {
            emailET.setError("this field must be filled");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditprofileScreen.this,HomeScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        profileIV.setImageURI(imageUri);
        uploadImage();
    }

    private void uploadImage() {

        pSharedPreference = getSharedPreferences(SHARED_PREFS_04,MODE_PRIVATE);
        String userPhone = pSharedPreference.getString(SHARED_PREFS_04,"User phone");

        final ProgressDialog pd = new ProgressDialog(EditprofileScreen.this);
        pd.setTitle("Uploading image...");
        pd.show();


        StorageReference mountainsRef = storageReference.child("image/" + userPhone);

        mountainsRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(EditprofileScreen.this,"Profile picture uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(EditprofileScreen.this,"failed to upload profile picture",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int)progressPercent + "%");
            }
        });

    }
}