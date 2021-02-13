package com.hjhj.daedan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
        String aa = intent.getStringExtra("markerUserid");
        Log.d("getintent",aa);

        tv_nameAndSchool.setText(MarkersItem.nickname + "("+MarkersItem.school+")");
        tv_title.setText(MarkersItem.title);
        tv_msg.setText(MarkersItem.message);
        tv_category.setText(MarkersItem.category);
        tv_uploadedTime.setText(MarkersItem.uploadTime);
        tv_timeLength.setText("+"+MarkersItem.timeLength+" 후 삭제");

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgRef = firebaseStorage.getReference().child("writingImg/"+MarkersItem.uploadTime);
        if(imgRef != null){
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(iv_image);
                }
            });
        }
        // TODO: 2021-02-10  글쓴이아이디, 카테고리, 등록유지시간, 업로드시간, 제목,내용, 이미지url, 보는아이디가 체크했는지.


    }

    public void click_back(View view) {
        finish();
    }

    public void click_sendMsg(View view) {
        startActivity(new Intent(this,ChatActivity.class));
    }
}