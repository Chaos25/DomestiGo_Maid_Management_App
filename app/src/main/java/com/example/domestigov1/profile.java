package com.example.domestigov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {

    TextView username,email;
    ListView listviewmaids;
    ArrayList<String> maidsList;
    ArrayAdapter<String> maidsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = (TextView) findViewById(R.id.usersname);
        email = (TextView) findViewById(R.id.usersemail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data as a Map
                Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();
                if (userData != null) {
                    // Extract name and email values from the Map
                    String name = (String) userData.get("name");
                    String userEmail = (String) userData.get("email");

                    // Update the corresponding TextViews with the retrieved user data
                    username.setText(name);
                    email.setText(userEmail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
        listviewmaids= (ListView) findViewById(R.id.listviewmaids);
        maidsList = new ArrayList<>();
        maidsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, maidsList);
        listviewmaids.setAdapter(maidsAdapter);

        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments").child(uid);
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                maidsList.clear();
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    String maidUid = appointmentSnapshot.getKey();
                    DatabaseReference maidNameRef = appointmentsRef.child(maidUid).child("maidName");
                    maidNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String maidName = snapshot.getValue(String.class);
                            maidsList.add(maidName + " (" + maidUid + ")");
                            maidsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
        listviewmaids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String maidDetails = (String) parent.getItemAtPosition(position);
                String[] maidDetailsArray = maidDetails.split(" \\(");
                String maidUid = maidDetailsArray[1].replace(")", "");
                showRatingDialog(maidUid);
            }
        });

    }
    private void showRatingDialog(String maidUid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate the maid");

        // Add the input field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Add the buttons
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ratingString = input.getText().toString().trim();
                int rating = Integer.parseInt(ratingString);

                // Add/update the rating node in the maids node under the selected maid's UID
                DatabaseReference maidRatingsRef = FirebaseDatabase.getInstance().getReference("maids").child(maidUid).child("ratings");
                maidRatingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int numRatings = 0;
                        int totalRating = 0;
                        if (snapshot.exists()) {
                            numRatings = snapshot.child("numRatings").getValue(Integer.class);
                            totalRating = snapshot.child("totalRating").getValue(Integer.class);
                        }
                        numRatings++;
                        totalRating += rating;
                        double avgRating = (double) totalRating / numRatings;

                        Map<String, Object> ratingValues = new HashMap<>();
                        ratingValues.put("numRatings", numRatings);
                        ratingValues.put("totalRating", totalRating);
                        ratingValues.put("avgRating", avgRating);

                        maidRatingsRef.updateChildren(ratingValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(profile.this, "Rating submitted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profile.this, "Error submitting rating", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}