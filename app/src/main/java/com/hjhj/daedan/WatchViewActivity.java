package com.hjhj.daedan;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WatchViewActivity extends AppCompatActivity {
    TextView tv_nameAndSchool, tv_title, tv_msg, tv_category, tv_uploadedTime, tv_timeLength;
    ImageView iv_image;
    MarkersItem item;
    ToggleButton fab;

    boolean handleUser =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_view);
        tv_nameAndSchool = findViewById(R.id.WatchView_tv_id);
        tv_title = findViewById(R.id.WatchView_tv_title);
        tv_msg = findViewById(R.id.WatchView_tv_msg);
        tv_category = findViewById(R.id.WatchView_tv_category);
        tv_uploadedTime = findViewById(R.id.WatchView_tv_uploadedTime);
        tv_timeLength = findViewById(R.id.WatchView_tv_timeLength);
        iv_image = findViewById(R.id.WatchView_iv_image);
        fab = findViewById(R.id.WatchView_toggle_fav);

        Intent intent = getIntent();
        item = new MarkersItem();
        item.userid = intent.getStringExtra("markerUserid");
        Log.d("getintent", item.userid);

//        Log.d("getintent",MarkersItem.userid );
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("markers").document(item.userid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    item.category = documentSnapshot.getString("category");
                    item.imgUrl = documentSnapshot.getString("imgUrl");
                    item.lat = documentSnapshot.getString("lat");
                    item.lon = documentSnapshot.getString("lon");
                    item.message = documentSnapshot.getString("message");
                    item.nickname = documentSnapshot.getString("nickname");
                    item.school = documentSnapshot.getString("school");
                    item.timeLength = documentSnapshot.getString("timeLength");
                    item.title = documentSnapshot.getString("title");
                    item.uploadTime = documentSnapshot.getString("uploadTime");
                    //userid는 이미 했음


                    //다 complete된 후에 화면바꿈
                    tv_nameAndSchool.setText(item.nickname + "(" + item.school + ")");
                    tv_title.setText(item.title);
                    tv_msg.setText(item.message);
                    tv_category.setText(item.category);

                    String year = item.uploadTime.substring(0, 4);
                    String month = item.uploadTime.substring(4, 6);
                    String day = item.uploadTime.substring(6, 8);
                    String hour = item.uploadTime.substring(8, 10);
                    String min = item.uploadTime.substring(10, 12);
                    tv_uploadedTime.setText(year + "년 " + month + "월" + day + "일 " + hour + ":" + min);
//                    tv_timeLength.setText("+"+ item.timeLength+" 후 삭제");

                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference imgRef = firebaseStorage.getReference().child("writeImg/" + item.uploadTime + ".png");
                    if (imgRef != null) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(iv_image);
                            }
                        });
                    }
                }
            }
        });
        //fav표시 좋아요한건지 안한건지
        String checked;
        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl("http://alexang.dothome.co.kr/");
        builder.addConverterFactory(ScalarsConverterFactory.create());
        Retrofit retrofit= builder.build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Map<String, String> dataPart = new HashMap<>();
        dataPart.put("userID", GUser.userId);
        dataPart.put("favID",item.userid);
        Call<String> call = retrofitService.checkFavor(dataPart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
                Log.d("checkfav",s);
                if(!s.equals("0")){
                    fab.setChecked(true);
                    handleUser = false;
                }else{
                    fab.setChecked(false);
                    handleUser = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("checkfav","에러"+t.getMessage());
            }
        });

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleUser = true;
                return false;
            }
        });

        //-----------oncreate()
//fav 업데이트
        fab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("tag","onCheckedchange");
//
                if (!handleUser){
                    return;
                }

                String checked = isChecked?"yes":"no";
                Retrofit.Builder builder= new Retrofit.Builder();
                builder.baseUrl("http://alexang.dothome.co.kr/");
                builder.addConverterFactory(ScalarsConverterFactory.create());
                Retrofit retrofit= builder.build();

                RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                Map<String, String> dataPart = new HashMap<>();
                dataPart.put("userID", GUser.userId);
                dataPart.put("favID",item.userid);
                dataPart.put("favor",checked);

                Call<String> call = retrofitService.updateData(dataPart);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String s = response.body();
                        Log.d("fav","ok:"+s);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("fav","failed"+t.getMessage());
                    }
                });



            }
        });




    }

    public void click_back(View view) {
        finish();
    }

    //쪽지보내기로 이동
    public void click_sendMsg(View view) {
        //먼저 채팅방이름을 설정하고, chatList에 이미 동일한 이름의 방이 있는지 점검. 있으면 걍 채팅창으로이동, 없으면 생성하고이동
        String destUser = item.userid;
        String currentUser = GUser.userId;
        String users[] = {destUser, currentUser};
        Arrays.sort(users);//오름차순으로 정리
        String chatRoomName = "";
        for (String aa : users) {
            chatRoomName += "&" + aa;
        }
        Log.d("roomname", chatRoomName);

        //todo:chatList에 동일 이름 있는지 검색....일단 챗리스트 먼저 만들고 하자
        if (true) {
            //동일이름 있으면 list에 이름추가!
        }

        //동일이름 없으면 걍 chatActivity로 이동.거기서 send하면 chatList에 추가
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("destUserId", destUser);
        intent.putExtra("chatRoomName", chatRoomName);
        startActivity(intent);
    }

}