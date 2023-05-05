package com.example.domestigov1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class forum extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView messageRecyclerView;

    private DatabaseReference messagesDatabaseRef,messagesDatabaseRef2;
    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    private ArrayList<Message> messageList = new ArrayList<>();
//    private ArrayList<Message> messageList1 = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private HashMap<String, Boolean> messageIdMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        messagesDatabaseRef = FirebaseDatabase.getInstance().getReference().child("messages").child(currentUserID);

        messageAdapter = new MessageAdapter(messageList);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (!messageText.isEmpty()) {
                    Message message = new Message(messageText, currentUserID);
                    messagesDatabaseRef.push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                messageEditText.setText("");
                            } else {
                                Toast.makeText(forum.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(forum.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
        messagesDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (!messageIdMap.containsKey(dataSnapshot.getKey())) {
                    messageList.add(message);
                    messageIdMap.put(dataSnapshot.getKey(), true);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        messagesDatabaseRef2 = FirebaseDatabase.getInstance().getReference().child("messages");

        messagesDatabaseRef2.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //messageList.clear(); // clear the list before adding the ordered messages

                // loop through each message under each user node and add it to the list
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot messageSnapshot : userSnapshot.getChildren()) {
                        Message message = messageSnapshot.getValue(Message.class);
                        if (!message.getFrom().equals(currentUserID)) {
                            if (!messageIdMap.containsKey(messageSnapshot.getKey())) {
                                messageList.add(message);
                                messageIdMap.put(messageSnapshot.getKey(), true);
                            }
                        }
                    }
                }

                // sort the list by timestamp in ascending order
                Collections.sort(messageList, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return Long.compare(o1.getTimestamp(), o2.getTimestamp());
                    }
                });

                // notify the adapter of the new ordered messages
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });






    }

}
