package com.hjhj.daedan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Tab1_Map_WriteActivity extends AppCompatActivity {

    TextView chooseCategory, chooseTimeLength;
    BottomSheetDialog bottomSheetDialog;
    RadioButton rb;
    String string_category, string_timeLength;
    View sheetview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1__map__write);

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lon = intent.getDoubleExtra("lon", 0);

        Log.d("gotit", "WriteActivity:  " + lat + " " + lon);
        //todo:작성화면 만들기

        btsCategory();
        btsTimeLength();

    }

    public void btsTimeLength(){
        chooseTimeLength = findViewById(R.id.tab1_map_write_tv_timeLength);
        chooseTimeLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(Tab1_Map_WriteActivity.this, R.style.BottomSheetTheme);
                sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_bottomsheet_timelength, findViewById(R.id.bts_time_layout));
                RadioGroup rg = sheetview.findViewById(R.id.bts_time_rg);

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        rb = sheetview.findViewById(checkedId);
                        string_timeLength = rb.getText().toString();
                        chooseTimeLength.setText(string_timeLength);

                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();



            }
        });
    }

    public void btsCategory(){
        chooseCategory = findViewById(R.id.tab1_map_write_tv_category);
        chooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(Tab1_Map_WriteActivity.this, R.style.BottomSheetTheme);
                sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_bottomsheet_category, findViewById(R.id.bottom_sheet));
                RadioGroup rg = sheetview.findViewById(R.id.bottomsheet_rg);

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        rb = sheetview.findViewById(checkedId);
                        string_category = rb.getText().toString();
                        chooseCategory.setText(string_category);

                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();



            }
        });
    }


}