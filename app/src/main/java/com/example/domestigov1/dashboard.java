package com.example.domestigov1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity {

    CardView cleaningCard;
    CardView assessCard,maidCard,aboutusCard,userCard;
    TextView dometigoname;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        assessCard= (CardView) findViewById(R.id.electronicCard);
        maidCard=(CardView) findViewById(R.id.maidsCard);
        aboutusCard=(CardView) findViewById(R.id.aboutusCard);
        userCard = (CardView) findViewById(R.id.userCard);
        dometigoname=(TextView) findViewById(R.id.DomestiGoName) ;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("name");
        userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                // Do something with the user's name
                dometigoname.setText("Hello "+name+"!");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        assessCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent1 = new Intent(dashboard.this,checkbox.class);
                startActivity(myintent1);
            }
        });
        maidCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent2 = new Intent(dashboard.this,MapsActivity.class);
                startActivity(myintent2);
            }
        });
        aboutusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent3 = new Intent(dashboard.this,aboutus.class);
                startActivity(myintent3);
            }
        });
        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent4 = new Intent(dashboard.this,profile.class);
                startActivity(myintent4);
            }
        });

    }
}