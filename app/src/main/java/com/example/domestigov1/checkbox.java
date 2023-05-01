package com.example.domestigov1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class checkbox extends AppCompatActivity implements View.OnClickListener {
    CheckBox a,b,c,d;
    ArrayList<String> selectedCheckboxes = new ArrayList<String>();
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);
        Button mybtn2=(Button) findViewById(R.id.b1);
        a=findViewById(R.id.options);
        b=findViewById(R.id.options2);
        c=findViewById(R.id.options3);
        d=findViewById(R.id.options4);
        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedCheckboxes.add(a.getText().toString());
                } else {
                    selectedCheckboxes.remove(a.getText().toString());
                }
            }
        });
        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedCheckboxes.add(b.getText().toString());
                } else {
                    selectedCheckboxes.remove(b.getText().toString());
                }
            }
        });
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedCheckboxes.add(c.getText().toString());
                } else {
                    selectedCheckboxes.remove(c.getText().toString());
                }
            }
        });
        d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedCheckboxes.add(d.getText().toString());
                } else {
                    selectedCheckboxes.remove(d.getText().toString());
                }
            }
        });
        mybtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference questionnaireRef = FirebaseDatabase.getInstance().getReference("questionnaire");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    DatabaseReference userChoicesRef = questionnaireRef.child(user.getUid()).child("choices");
                    userChoicesRef.setValue(selectedCheckboxes);
                }
                Intent intn=new Intent(checkbox.this,Questionnaire.class);
                startActivity(intn);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Button clicked=(Button) view;
        if(clicked.getId()==R.id.b1){
            b1.setBackgroundColor(Color.rgb(33,145,251));
        }

    }
}