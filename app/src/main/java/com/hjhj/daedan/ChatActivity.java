package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {

    EditText et_textmsg;
    String destUser;
    String chatRoomName;
    String currentUser;
    String textmsg;
    FloatingActionButton fab;

    ListView listView;
    ChatAdapter chatAdapter;
    ArrayList<MessageItem> messageItems = new ArrayList<>();

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        et_textmsg = findViewById(R.id.chatActivity_et);


        //툴바 조정하기.
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbarlayout);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        //툴바타이틀 사라지게.
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange ==-1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange+verticalOffset ==0){
                    collapsingToolbarLayout.setTitle("this is title");
                    isShow = true;
                }else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        listView = findViewById(R.id.chatActivity_listview);
        chatAdapter = new ChatAdapter(this, messageItems);



        //-----------------아래부터 내용관련
        Intent intent = getIntent();
        destUser = intent.getStringExtra("destUserId");
        chatRoomName = intent.getStringExtra("chatRoomName");
        currentUser = GUser.userId;

        //채팅내용 불러오기
        firestore = FirebaseFirestore.getInstance();

        //##교수님이랑 한것임. 나중에 참고
//        firestore.collectionGroup(chatRoomName).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        for(int i=0; i<queryDocumentSnapshots.size();i++){
//                            Map<String, Object> data =queryDocumentSnapshots.getDocuments().get(i).getData();
//                            String msg = data.get("msg").toString();
//                            String name = data.get("name").toString();
//                            String profileUrl = data.get("profileUrl").toString();
//                            String time = data.get("time").toString();
//                            String userId = data.get("userId").toString();
//                            messageItems.add(new MessageItem(userId,name,msg,profileUrl,time));
//                        }
//                    }
//                });
        //처음뷰
        Log.d("why","1111");//1
        firestore.collectionGroup(chatRoomName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    messageItems.clear();
                    Log.d("why","2222");//4
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for(DocumentSnapshot document : documents){
                        MessageItem item = document.toObject(MessageItem.class);
                        messageItems.add(item);
                        Log.d("why","3333");
                    }
//                    Collections.reverse(messageItems);
                    listView.setAdapter(chatAdapter);
                    Log.d("why","4444");//5
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        listView.setNestedScrollingEnabled(true);
                    }
                    listView.setSelection(messageItems.size()-1);
//                    listView.smoothScrollToPosition(messageItems.size()-1);
                }
            }
        });
        //추가될때
        Log.d("why","5555");//2
        firestore.collection("chats").document(chatRoomName).collection(chatRoomName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if(error !=null){
                            return;
                        }
                        Log.d("why","6666");//3
                        for(DocumentChange dc :snapshots.getDocumentChanges()){
                            switch(dc.getType()){
                                case ADDED:
                                    MessageItem item = dc.getDocument().toObject(MessageItem.class);
                                    Log.d("query", item.msg );
                                    messageItems.add(item);
                                    Log.d("why","7777");
                                    break;
                            }
//                            MessageItem item = doc.toObject(MessageItem.class);

                            chatAdapter.notifyDataSetChanged();
                            Log.d("why","8888");
                            listView.setSelection(messageItems.size()-1);
//                            listView.smoothScrollToPosition(messageItems.size()-1);
                        }
                    }
                });


    }

    public void clickSend(View view) {
        textmsg = et_textmsg.getText().toString();
        if(textmsg!=""){

            //처음 보내는 거라면 보낼때 챗룸리스트에 이름추가
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            UserForChatRoomList userForChatRoomList = new UserForChatRoomList(currentUser,destUser);
            firestore.collection("chatRoomNameList").document(chatRoomName).set(userForChatRoomList)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("chatRoomList","222");

                        }
                    });
            //처음보내는게 아니라면, ...음일단나중에
            Log.d("chatRoomList","111");

            // 대화내용도 추가;
            TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS", Locale.KOREAN);
            dateFormat.setTimeZone(zone);
            String uploadtime = dateFormat.format(new Date());

            MessageItem item = new MessageItem(GUser.userId,GUser.nickname,textmsg,GUser.profileUrl,uploadtime);
            firestore.collection("chats").document(chatRoomName).collection(chatRoomName).document(uploadtime).set(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("chatRoomList","444");
                        }
                    });

            firestore.collection("chats").document(chatRoomName).set(new Nothing("nothing"));

            et_textmsg.setText("");
        }
        Log.d("chatRoomList","333");
    }

    public void click_fab(View view) {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("destUser",destUser);
        startActivity(intent);
    }

    //의미없는클래스임...채팅방이름리스트 가져오려는데 필드값이 없으면 안돼서ㅠㅠㅠ어쩔수없이..
    public class Nothing{
        public String value;

        public Nothing(){
        }
        public Nothing(String value){
            this.value = value;
        }
    }

//서버에 채팅방리스트에 넣을거 만듦
    public class UserForChatRoomList {
        public String currentUser;
        public String destUser;

        public UserForChatRoomList() {
        }

        public UserForChatRoomList(String currentUser, String destUser) {
            this.currentUser = currentUser;
            this.destUser = destUser;
        }
    }

}
