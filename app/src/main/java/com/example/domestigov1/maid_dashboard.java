package com.example.domestigov1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class maid_dashboard extends AppCompatActivity {
    private CardView changeLanguage;
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