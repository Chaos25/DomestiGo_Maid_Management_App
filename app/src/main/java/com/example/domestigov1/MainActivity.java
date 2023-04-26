package com.example.domestigov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView emailTextView ;

    private TextView passwordTextView ;

    private Button loginbtn;
    TextView changeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        emailTextView =(TextView) findViewById(R.id.username);

        passwordTextView =(TextView) findViewById(R.id.password);
        loginbtn= (Button) findViewById(R.id.loginButton);

        changeLanguage=findViewById(R.id.changeLanguage);
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }
        });

        //admin and admin
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
            }
        });

        TextView signuptext = (TextView) findViewById(R.id.signupText);
        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(MainActivity.this,signup.class);
                startActivity(myintent);
            }
        });
        TextView maidlogintext1 =(TextView) findViewById(R.id.maidlogintext);
        maidlogintext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent1 = new Intent(MainActivity.this, maid_login.class);
                startActivity(myintent1);
            }
        });

    }
    private void changeLanguage() {
        final String languages[]={"English","Hindi","Kannada"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    setLocale("");
                    recreate();
                }
                else if(i==1){
                    setLocale("hi");
                    recreate();
                } else if (i==2) {
                    setLocale("kn");
                    recreate();

                }

            }
        });
        mBuilder.create();
        mBuilder.show();
    }

    private void setLocale(String langauge) {
        Locale locale=new Locale(langauge);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
    }
    private void loginUserAccount(){
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();


                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(MainActivity.this,
                                            dashboard.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                }
                            }
                        });
    }

}