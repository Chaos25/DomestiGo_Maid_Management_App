package com.example.domestigov1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.PopupWindow;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class maid_profile extends AppCompatActivity {

    String location1;
    TextView name1,exp1,services1,name2,exp2,services2;
    Button hire1,hire2;
    DatabaseReference maidsRef;
    private String maidUid;
    private String maidName;
    private DataSnapshot maid1Snapshot = null;
   private  DataSnapshot maid2Snapshot = null;

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
        hire1 = (Button) findViewById(R.id.button1);
        hire2 = (Button) findViewById(R.id.button2);

        Intent myintent1= getIntent();
        location1=myintent1.getStringExtra("city");


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
                            maid1Snapshot = dataSnapshot;
                        } else if (i == 2) {
                            name2.setText(name);
                            exp2.setText(experience);
                            services2.setText(services);
                            maid2Snapshot = dataSnapshot;
                            break;
                        }
                        i++;

                    }
                }
                hire1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maidUid = maid1Snapshot.getKey();
                        maidName = name1.getText().toString();
                        showPopup(maidUid,maidName);
                    }
                });

                hire2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        maidUid = maid2Snapshot.getKey();
                        maidName = name2.getText().toString();
                        showPopup(maidUid,maidName);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MaidProfile", "Failed to read value.", error.toException());
            }
        });


    }

private void showPopup(String maidUid, String maidName) {
    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    View popupView = inflater.inflate(R.layout.popup_layout, null);

    // create the popup window
    int width = LinearLayout.LayoutParams.MATCH_PARENT;
    int height = LinearLayout.LayoutParams.MATCH_PARENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

    // show the popup window
    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    // handle the submit button click
    Button submitButton = popupView.findViewById(R.id.submit_button);
    submitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioGroup frequencyGroup = popupView.findViewById(R.id.frequency_group);
            DatePicker datePicker = popupView.findViewById(R.id.date_picker);
            TimePicker timePicker = popupView.findViewById(R.id.time_picker);
            timePicker.setIs24HourView(true); // set whether to use 24-hour mode
            timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS); // block focus to subviews

            timePicker.setPadding(0, 0, 0, 0); // set padding to 0
            int selectedId = frequencyGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton radioButton = popupView.findViewById(selectedId);
                String frequency = radioButton.getText().toString();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                String time = hour + ":" + minute;
                saveAppointment(maidUid, maidName, frequency, date,time);
                popupWindow.dismiss();
            } else {
                Toast.makeText(maid_profile.this, "Please select a frequency", Toast.LENGTH_SHORT).show();
            }
        }
    });
}

//    private void saveAppointment(String frequency) {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            String uid = currentUser.getUid();
//            DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments").child(uid);
//            String appointmentId = appointmentsRef.push().getKey();
//            Map<String, Object> appointmentData = new HashMap<>();
//            appointmentData.put("maidUid", maidUid);
//            appointmentData.put("maidName", maidName);
//            appointmentData.put("frequency", frequency);
//            appointmentData.put("location", location1);
//            appointmentsRef.child(appointmentId).setValue(appointmentData, new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                    if (error == null) {
//                        Toast.makeText(maid_profile.this, "Appointment created", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Log.e("MaidProfile", "Failed to create appointment.", error.toException());
//                        Toast.makeText(maid_profile.this, "Failed to create appointment.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
private void saveAppointment(String maidUid, String maidName, String frequency, String date,String time) {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
        String uid = currentUser.getUid();
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments").child(uid);
        String appointmentId = appointmentsRef.push().getKey();
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("maidUid", maidUid);
        appointmentData.put("maidName", maidName);
        appointmentData.put("frequency", frequency);
        appointmentData.put("date", date);
        appointmentData.put("time", time);
        appointmentsRef.child(maidUid).setValue(appointmentData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(maid_profile.this, "Appointment created", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("MaidProfile", "Failed to create appointment.", error.toException());
                    Toast.makeText(maid_profile.this, "Failed to create appointment.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


}