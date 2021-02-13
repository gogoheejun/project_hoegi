package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class WatchViewActivity extends AppCompatActivity {
    TextView tv_nameAndSchool,tv_title,tv_msg, tv_category,tv_uploadedTime, tv_timeLength;
    ImageView iv_image;

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


        Intent intent = getIntent();
        MarkersItem_static.userid = intent.getStringExtra("markerUserid");
//        Log.d("getintent",MarkersItem.userid );
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("markers").document(MarkersItem_static.userid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    MarkersItem_static.category = documentSnapshot.getString("category");
                    MarkersItem_static.imgUrl = documentSnapshot.getString("imgUrl");
                    MarkersItem_static.lat = documentSnapshot.getString("lat");
                    MarkersItem_static.lon = documentSnapshot.getString("lon");
                    MarkersItem_static.message = documentSnapshot.getString("message");
                    MarkersItem_static.nickname = documentSnapshot.getString("nickname");
                    MarkersItem_static.school = documentSnapshot.getString("school");
                    MarkersItem_static.timeLength = documentSnapshot.getString("timeLength");
                    MarkersItem_static.title = documentSnapshot.getString("title");
                    MarkersItem_static.uploadTime = documentSnapshot.getString("uploadTime");
                    //userid는 이미 했음


                    //다 complete된 후에 화면바꿈
                    tv_nameAndSchool.setText(MarkersItem_static.nickname + "("+ MarkersItem_static.school+")");
                    tv_title.setText(MarkersItem_static.title);
                    tv_msg.setText(MarkersItem_static.message);
                    tv_category.setText(MarkersItem_static.category);
                    tv_uploadedTime.setText(MarkersItem_static.uploadTime);
                    tv_timeLength.setText("+"+ MarkersItem_static.timeLength+" 후 삭제");

                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference imgRef = firebaseStorage.getReference().child("writeImg/"+ MarkersItem_static.uploadTime+".png");
                    if(imgRef != null){
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




        // TODO: 2021-02-10  글쓴이아이디, 카테고리, 등록유지시간, 업로드시간, 제목,내용, 이미지url, 보는아이디가 체크했는지.


    }

    public void click_back(View view) {
        finish();
    }

    public void click_sendMsg(View view) {
        startActivity(new Intent(this,ChatActivity.class));
    }
}