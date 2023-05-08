package com.example.domestigov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class appointments extends AppCompatActivity {

    private TextView date1TextView, time1TextView, location1TextView,
            date2TextView, time2TextView, location2TextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        // Initialize the TextViews
        date1TextView = (TextView) findViewById(R.id.date1);
        time1TextView = findViewById(R.id.time1);
        location1TextView = findViewById(R.id.location1);
        date2TextView = findViewById(R.id.date2);
        time2TextView = findViewById(R.id.time2);
        location2TextView = findViewById(R.id.location2);

        // Get the current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String maidUid = firebaseUser.getUid();


        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("appointments");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                Log.d("appointments", "Number of appointments: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String uid = dataSnapshot1.getKey();
                        String muid = dataSnapshot1.child("maidUid").getValue(String.class);
                        Log.d("appointments", "Maid UID: " + muid);
                        Log.d("appointments", "Maid1 UID: " + maidUid);

                        if (muid.equals(maidUid)) {
                            String date = dataSnapshot1.child("date").getValue(String.class);
                            String time = dataSnapshot1.child("time").getValue(String.class);
                            Log.d("appointments", "Appointment " + i + " date: " + date);
                            Log.d("appointments", "Appointment " + i + " time: " + time);
                            if (i == 1) {
                                date1TextView.setText(date);
                                time1TextView.setText(time);
                            } else if (i == 2) {
                                date2TextView.setText(date);
                                time2TextView.setText(time);
                                break;
                            }
                            i++;


                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MaidProfile", "Failed to read value.", error.toException());
            }
        });
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("appointments");
//        Query query = database.orderByChild("maidUid").equalTo(maidUid);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int i = 1;
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String date = dataSnapshot.child("date").getValue(String.class);
//                    String time = dataSnapshot.child("time").getValue(String.class);
//                    if (i == 1) {
//                        date1TextView.setText(date);
//                        time1TextView.setText(time);
//                    } else if (i == 2) {
//                        date2TextView.setText(date);
//                        time2TextView.setText(time);
//                        break;
//                    }
//                    i++;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("MaidProfile", "Failed to read value.", error.toException());
//            }
//        });






    }
}