package com.hjhj.daedan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Tab1_Map_WriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1__map__write);

        Intent intent = getIntent();
        double lat =intent.getDoubleExtra("lat",0);
        double lon = intent.getDoubleExtra("lon",0);

        Log.d("gotit","WriteActivity:  "+lat+" "+lon);
        //todo:작성화면 만들기


        TextView chooseCategory = findViewById(R.id.tab1_map_write_tv_category);


        chooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomsheetDialog bottomSheet = new BottomsheetDialog();
                bottomSheet.show(getSupportFragmentManager(),"ModalBottomSheet");

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
    }

}