package com.example.domestigov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class maid_profile extends AppCompatActivity {

    String location1;
    TextView name1,exp1,services1,name2,exp2,services2;
    DatabaseReference maidsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_profile);
        name1=(TextView) findViewById(R.id.maid_name1);
        exp1=(TextView) findViewById(R.id.maid_exp1);
        services1=(TextView) findViewById(R.id.maid_services1);
        name2=(TextView) findViewById(R.id.maid_name2);
        exp2=(TextView) findViewById(R.id.maid_exp2);
        services2=(TextView) findViewById(R.id.maid_services2);

        Intent myintent1= getIntent();
        location1=myintent1.getStringExtra("city");
//        DatabaseReference maidsRef = FirebaseDatabase.getInstance().getReference("maids");
//        Query query = maidsRef.orderByChild("location").equalTo(location1);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot maidSnapshot : snapshot.getChildren()) {
//                    String name = maidSnapshot.child("name").getValue(String.class);
//                    String exp = maidSnapshot.child("experience").getValue(String.class);
//                    String services = maidSnapshot.child("services").getValue(String.class);
//
//                    // do something with the maid information, e.g. set the text of the TextViews
//                    name1.setText(name);
//                    exp1.setText(exp);
//                    services1.setText(services);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // handle any errors here
//            }
//        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("maids");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    String location = dataSnapshot.child("location").getValue(String.class);
                    if (location.equals(location1)) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String experience = dataSnapshot.child("experience").getValue(String.class);
                        String services = dataSnapshot.child("services").getValue(String.class);
                        if (i == 1) {
                            name1.setText(name);
                            exp1.setText(experience);
                            services1.setText(services);
                        } else if (i == 2) {
                            name2.setText(name);
                            exp2.setText(experience);
                            services2.setText(services);
                            break;
                        }
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MaidProfile", "Failed to read value.", error.toException());
            }
        });


    }
}