package com.example.domestigov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class maidsignup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signupbtn ;
    private EditText name ;
    private EditText email ;
    private EditText user ;
    private EditText pass ;
    private EditText location;
    FirebaseDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maidsignup);
        signupbtn = (Button) findViewById(R.id.signupButton);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        location =(EditText) findViewById(R.id.location);
        database = FirebaseDatabase.getInstance();


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }
    private void registerNewUser() {
        String email1, pass1,name1,location1;
        email1 = email.getText().toString();
        pass1 = pass.getText().toString();
        name1=name.getText().toString();
        location1=location.getText().toString();
        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pass1)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
//        mAuth.createUserWithEmailAndPassword(email1, pass1)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Registration successful!",
//                                    Toast.LENGTH_LONG).show();
//
//                            // if the user created intent to login activity
//                            Intent intent
//                                    = new Intent(signup.this,
//                                    MainActivity.class);
//                            startActivity(intent);
//                        } else {
//
//                            // Registration failed
//
//                            Toast.makeText(
//                                            getApplicationContext(),
//                                            "Registration failed!!"
//                                                    + " Please try again later",
//                                            Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//                    }
//                });
        mAuth.createUserWithEmailAndPassword(email1, pass1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.updateProfile(new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name1)
                                            .build())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Registration successful!",
                                                        Toast.LENGTH_LONG).show();

                                                // Save maid data to the Firebase Realtime Database
                                                DatabaseReference maidsRef = database.getReference("maids");
                                                String uid = user.getUid();
                                                HashMap<String, Object> maidMap = new HashMap<>();
                                                maidMap.put("name", name1);
                                                maidMap.put("email", email1);
                                                maidMap.put("location", location1);
                                                maidsRef.child(uid).setValue(maidMap);

                                                Intent intent = new Intent(maidsignup.this, maid_login.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                    .show();
                                            }
                                        }
                                    });
                        } else {
                            // handle error
                        }
                    }
                });
    }

}