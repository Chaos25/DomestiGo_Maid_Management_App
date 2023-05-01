package com.example.domestigov1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.app.AlertDialog;
import android.graphics.Color;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Questionnaire extends AppCompatActivity implements View.OnClickListener {
    TextView questionT;
    Button A,B,C,D;
    Button submitBtn;
    int curr=0;
    String ans="";
    String fin="";
    ArrayList<String> choices2 = new ArrayList<String>();
    FirebaseUser mUser;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        questionT=findViewById(R.id.question);
        A=findViewById(R.id.A);
        B=findViewById(R.id.B);
        C=findViewById(R.id.C);
        D=findViewById(R.id.D);
        submitBtn=findViewById(R.id.b1);
        A.setOnClickListener(this);
        B.setOnClickListener(this);
        C.setOnClickListener(this);
        D.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        loadNewQuestion();



        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onClick(View view) {
        A.setBackgroundColor(Color.rgb(251,251,255));
        B.setBackgroundColor(Color.rgb(251,251,255));
        C.setBackgroundColor(Color.rgb(251,251,255));
        D.setBackgroundColor(Color.rgb(251,251,255));
        Button clicked=(Button) view;
        if(clicked.getId()==R.id.b1){
            fin+=ans;
            choices2.add(ans);
            curr++;
            loadNewQuestion();
        }
        else{
            ans=clicked.getText().toString();
            clicked.setBackgroundColor(Color.rgb(125,185,182));
        }
    }
    void loadNewQuestion(){
        if(curr==3){
            finishQuiz();
            return;
        }
        questionT.setText(Question_ans.question[curr]);
        A.setText(Question_ans.choices[curr][0]);
        B.setText(Question_ans.choices[curr][1]);
        C.setText(Question_ans.choices[curr][2]);
        D.setText(Question_ans.choices[curr][3]);
    }
    void finishQuiz(){
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        String uid = mUser.getUid();
        String time = choices2.get(0);
        String locality = choices2.get(1); // Replace with the user's selected locality
        String frequency = choices2.get(2); // Replace with the user's selected frequency
        mDatabase.child("questionnaire").child(uid).child("choices2").child("time").setValue(time);
        mDatabase.child("questionnaire").child(uid).child("choices2").child("locality").setValue(locality);
        mDatabase.child("questionnaire").child(uid).child("choices2").child("frequency").setValue(frequency);

        new AlertDialog.Builder(this)
                .setTitle("Application status")
                .setMessage("Your request has been submitted successfully"+fin)
                .setCancelable(true)
                .show();


    }
}