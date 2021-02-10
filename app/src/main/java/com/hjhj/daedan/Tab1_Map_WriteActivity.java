package com.hjhj.daedan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Tab1_Map_WriteActivity extends AppCompatActivity {

    TextView tv_chooseCategory, tv_chooseTimeLength, tv_chooseImage;
    BottomSheetDialog bottomSheetDialog;
    RadioButton rb;
    String string_category, string_timeLength;
    View sheetview;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1__map__write);
        tv_chooseImage = findViewById(R.id.tab1_map_write_tv_imageUpload);
        tv_chooseTimeLength = findViewById(R.id.tab1_map_write_tv_timeLength);
        tv_chooseCategory = findViewById(R.id.tab1_map_write_tv_category);


        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lon = intent.getDoubleExtra("lon", 0);

        Log.d("gotit", "WriteActivity:  " + lat + " " + lon);


        btsCategory();
        btsTimeLength();
        chooseImage();

    }

    public void click_writeOk(View view) {
        //todo: 작성된 글을 게시판 db에 업로드 : 글쓴이아이디, 카테고리, 등록유지시간, 업로드시간, 제목,내용, 이미지url,위치 올려야 함.

    }

    public void chooseImage(){
//        chooseImage= findViewById(R.id.tab1_map_write_tv_imageUpload);
        tv_chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
    }

    public void btsTimeLength(){
//        chooseTimeLength = findViewById(R.id.tab1_map_write_tv_timeLength);
        tv_chooseTimeLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(Tab1_Map_WriteActivity.this, R.style.BottomSheetTheme);
                sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_bottomsheet_time, findViewById(R.id.bts_time_layout));
                RadioGroup rg = sheetview.findViewById(R.id.bts_time_rg);

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        rb = sheetview.findViewById(checkedId);
                        string_timeLength = rb.getText().toString();
                        tv_chooseTimeLength.setText(string_timeLength);
                        tv_chooseTimeLength.setTypeface(tv_chooseTimeLength.getTypeface(), Typeface.BOLD);

                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();



            }
        });
    }

    public void btsCategory(){
//        chooseCategory = findViewById(R.id.tab1_map_write_tv_category);
        tv_chooseCategory.setOnClickListener(new View.OnClickListener() {
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
                        tv_chooseCategory.setText(string_category);
                        tv_chooseCategory.setTypeface(tv_chooseCategory.getTypeface(), Typeface.BOLD);

                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();



            }
        });
    }


    public void getImage() {
        Log.d("getImage","method on");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20 && resultCode==RESULT_OK){
            imgUri = data.getData();
            tv_chooseImage.setText("이미지 선택완료!");
//            Picasso.get().load(imgUri).into(iv_profile);

        }
    }


}