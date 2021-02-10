package com.hjhj.daedan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WatchViewActivity extends AppCompatActivity {
    TextView tv_nameAndSchool,tv_title,tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_view);
        tv_nameAndSchool = findViewById(R.id.uploadedView_tv_id);
        tv_title = findViewById(R.id.uploadedView_tv_title);
        tv_msg = findViewById(R.id.uploadedView_tv_msg);

        Intent intent = getIntent();
        tv_nameAndSchool.setText(intent.getStringExtra("nickname")+" ("+( intent.getStringExtra("school"))+")");
        tv_title.setText(intent.getStringExtra("title"));
        tv_msg.setText(intent.getStringExtra("msg"));
        // TODO: 2021-02-10  글쓴이아이디, 카테고리, 등록유지시간, 업로드시간, 제목,내용, 이미지url, 보는아이디가 체크했는지.


    }

    public void click_back(View view) {
        finish();
    }

    public void click_sendMsg(View view) {
        startActivity(new Intent(this,ChatActivity.class));
    }
}