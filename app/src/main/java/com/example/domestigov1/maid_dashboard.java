package com.example.domestigov1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class maid_dashboard extends AppCompatActivity {
    private CardView changeLanguage,appointmentsCard,forumCard;
    private TextView ratingtext,profiletext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_dashboard);
        changeLanguage=(CardView) findViewById(R.id.userCard);
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }
        });
        appointmentsCard=(CardView) findViewById(R.id.appointmentsCard);
        appointmentsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(maid_dashboard.this,appointments.class);
                startActivity(myintent);
            }
        });
        forumCard=(CardView) findViewById(R.id.forumCard);
        forumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent1 = new Intent(maid_dashboard.this,forum.class);
                startActivity(myintent1);
            }
        });
        ratingtext = (TextView) findViewById(R.id.ratingText);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String maidUid = user.getUid();
        DatabaseReference maidRef = FirebaseDatabase.getInstance().getReference("maids").child(maidUid).child("ratings").child("avgRating");
        maidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double avgRating = dataSnapshot.getValue(Double.class);
                if (avgRating != null) {
                    // Round off the rating to 1 decimal place
                    String formattedRating = String.format("%.1f", avgRating);
                    // Set the formatted rating to the ratingText TextView
                    ratingtext.setText(formattedRating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        profiletext = (TextView) findViewById(R.id.profiletext);
        profiletext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog with EditText views for experience and services
                AlertDialog.Builder builder = new AlertDialog.Builder(maid_dashboard.this);
                builder.setTitle("Enter your experience and services");

                // Set up the LinearLayout container
                LinearLayout container = new LinearLayout(maid_dashboard.this);
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(20, 0, 20, 0);
                container.setLayoutParams(params);

                // Set up the experience EditText view
                final EditText experienceEditText = new EditText(maid_dashboard.this);
                experienceEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                experienceEditText.setHint("Enter your experience");
                container.addView(experienceEditText);

                // Set up the services EditText view
                final EditText servicesEditText = new EditText(maid_dashboard.this);
                servicesEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                servicesEditText.setHint("Enter your services");
                container.addView(servicesEditText);

                // Set up the LinearLayout container as the AlertDialog view
                builder.setView(container);

                // Set up the "Submit" button
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the values entered in the EditText views
                        String experience = experienceEditText.getText().toString();
                        String services = servicesEditText.getText().toString();

                        // Store the values in the Firebase database
                        DatabaseReference maidRef = FirebaseDatabase.getInstance().getReference("maids").child(maidUid);
                        maidRef.child("experience").setValue(experience);
                        maidRef.child("services").setValue(services);
                        Toast.makeText(maid_dashboard.this, "Profile successfully updated", Toast.LENGTH_SHORT).show();
                    }
                });

                // Set up the "Cancel" button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Cancel the dialog
                        dialogInterface.cancel();
                    }
                });

                // Show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
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
                }else if (i==2) {
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


}