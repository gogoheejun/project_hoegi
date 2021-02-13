package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Tab1_Map_WriteActivity extends AppCompatActivity {

    TextView tv_chooseCategory, tv_chooseTimeLength, tv_chooseImage;
    EditText et_title, et_msg;
    BottomSheetDialog bottomSheetDialog;
    RadioButton rb;
    String string_category, string_timeLength, title, msg, school, schoolname, nickname;
    View sheetview;
    Uri imgUri;

    //db--
    String userid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference markersRef;
    String uploadtime;
    String writeimgUrl, userEmail;
    double lat, lon;
    FirebaseUser user;
    MarkersItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1__map__write);
        tv_chooseImage = findViewById(R.id.tab1_map_write_tv_imageUpload);
        tv_chooseTimeLength = findViewById(R.id.tab1_map_write_tv_timeLength);
        tv_chooseCategory = findViewById(R.id.tab1_map_write_tv_category);
        et_title = findViewById(R.id.map_write_et_title);
        et_msg = findViewById(R.id.map_write_et_msg);


        //현재시간
        TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREAN);
        dateFormat.setTimeZone(zone);
        uploadtime = dateFormat.format(new Date());


        firebaseDatabase = FirebaseDatabase.getInstance();
        markersRef = firebaseDatabase.getReference("markers");

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userEmail = user.getEmail();
            if (userEmail != null) {
                int idx = userEmail.indexOf("@");
                school = userEmail.substring(idx + 1, idx + 4);
                switch (school) {
                    case "huf":
                        schoolname = "한국외국어대학교";
                        break;
                    case "uos":
                        schoolname = "서울시립대학교";
                        break;
                    case "khu":
                        schoolname = "경희대학교";
                        break;
                }
            }
        }

        Log.d("AA", schoolname + "");


        userid = user.getUid();
        if (userid != null) {
            Log.d("user", userid.toString());
        } else {
            Log.d("user", "no user");
        }


        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 0);

        Log.d("gotit", "WriteActivity:  " + lat + " " + lon);


        btsCategory();
        btsTimeLength();
        chooseImage();

    }

    public void click_writeOk(View view) {
//        nickname;userid;category;uploadTime;timeLength;title;message;imgUrl;lat;lon;
//        user = mAuth.getCurrentUser();
//        userEmail = user.getEmail();


        title = et_title.getText().toString();
        msg = et_msg.getText().toString();

        //이미지는 먼저 sgtorage에 저장
        if (imgUri != null) {
            String fileName = uploadtime + ".png";
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference imgRef = firebaseStorage.getReference("writeImg/" + fileName);

            UploadTask uploadTask = imgRef.putFile(imgUri);//저아래 onActivityResult에서 가져옴
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            writeimgUrl = uri.toString();  //이제 이 writeimgUrl을 db에 다른 정보랑 같이 한방에 저장
//                            Toast.makeText(Tab1_Map_WriteActivity.this, "write이미지 firestorage에 저장완료\n"+writeimgUrl, Toast.LENGTH_SHORT).show();


                            //업로드!
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                            //닉네임가져옴
                            DocumentReference docref = firestore.collection("users").document(userid);
                            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        nickname = documentSnapshot.getString("nickname");


                                        MarkersItem item = new MarkersItem(school, nickname, userid, string_category, uploadtime, string_timeLength, title, msg, "noImage", lat + "", lon + "");

                                        uploadToFirestore(firestore, item);

                                    }
                                }
                            });

                        }
                    });
                }
            });
        } else {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            //닉네임가져옴
            DocumentReference docref = firestore.collection("users").document(userid);
            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Log.d("TAG", "exists!" + documentSnapshot.getString("nickname"));
                            nickname = documentSnapshot.getString("nickname");
                            item = new MarkersItem(school, nickname, userid, string_category, uploadtime, string_timeLength, title, msg, "noImage", lat + "", lon + "");

                            uploadToFirestore(firestore, item);

                        } else {
                            Log.d("TAG", "no such docs");
                        }
                    } else {
                        Log.d("TAG", "failed!" + task.getException());
                    }
                }
            });
        }
    }

    public void uploadToFirestore(FirebaseFirestore firestore, MarkersItem item) {
        firestore.collection("markers").document(userid).set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Tab1_Map_WriteActivity.this, "게시글 업로드 성공", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "성공");

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Tab1_Map_WriteActivity.this, "정보등록 실패ㅠ", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void chooseImage() {
//        chooseImage= findViewById(R.id.tab1_map_write_tv_imageUpload);
        tv_chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
    }

    public void btsTimeLength() {
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

    public void btsCategory() {
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
        Log.d("getImage", "method on");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK) {
            imgUri = data.getData();
            tv_chooseImage.setText("이미지 선택완료!");
//            Picasso.get().load(imgUri).into(iv_profile);

        }
    }


}