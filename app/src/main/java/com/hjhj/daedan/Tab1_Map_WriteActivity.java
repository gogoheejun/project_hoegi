package com.hjhj.daedan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Tab1_Map_WriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1__map__write);

        Intent intent = getIntent();
        double lat =intent.getDoubleExtra("lat",0);
        double lon = intent.getDoubleExtra("lon",0);

        Log.d("gotit",lat+" "+lon);




    }
}