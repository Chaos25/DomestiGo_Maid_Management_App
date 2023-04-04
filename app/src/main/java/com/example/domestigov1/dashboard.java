package com.example.domestigov1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public class dashboard extends AppCompatActivity {

    CardView cleaningCard;
    CardView assessCard,maidCard,aboutusCard;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        assessCard= (CardView) findViewById(R.id.electronicCard);
        maidCard=(CardView) findViewById(R.id.maidsCard);
        aboutusCard=(CardView) findViewById(R.id.aboutusCard);
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

    }
}