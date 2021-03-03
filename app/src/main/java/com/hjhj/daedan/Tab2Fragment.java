package com.hjhj.daedan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tab2Fragment extends Fragment {
    ArrayList<MessageItem> messageItems = new ArrayList<>();
    RecyclerView recyclerView;
    Tab2_Chatlist_RecyclerAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    FirebaseFirestore firestore;

    String roomname;
    String friendID, friendName;
    String friendProfileUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //db에서 채팅받아오기
        Log.d("roomname","onCreate");
        loadData();
        Log.w("order","111");
    }

    private void loadData() {
        String currentUser = GUser.userId;
        //document의 이름(id)가져와서 currentUser이름이 들어있는 애들꺼만 내용 가져옴
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //Guser와의 대화상대만 고름
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Log.d("roomname","전체방들: "+documentSnapshot.getId());
                        roomname = documentSnapshot.getId();
                        if(roomname.contains(currentUser)){
                            Log.d("roomname","포함되는지 확인: "+currentUser+ "====>"+documentSnapshot.getId());
                            //대화상대들 골랐다면 데이터 가져옴
                            getinto(roomname);
                        }
                    }
                }
            }
        });
//        Log.d("roomname",firestore.collection("chats").get().toString());
        Log.w("order","222");
    }
    int i = -1;
    int j = 0;
    ArrayList<String> friendIdList = new ArrayList<>();
//    ArrayList<String> friendIdList = new ArrayList<>();
//    ArrayList<String> friendIdList = new ArrayList<>();
    private void getinto(String roomname) {
        Query query = firestore.getInstance().collection("chats").document(roomname)
                .collection(roomname).orderBy("time",Query.Direction.DESCENDING).limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
//                    Log.d("roomname", task.getResult().getDocuments().toString());
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for(DocumentSnapshot document : documents){
                        MessageItem item = document.toObject(MessageItem.class);
                        Log.d("roomname",roomname+"과 일치하는 방 최신글의 작성자이름: "+item.name);

                        //채팅방이름(&이름&이름 형식)에서 내 이름 아닌 애를 추출.=>리스트에 보이는 프로필,닉네임을 바꿔줌
                        String[] names = roomname.split("&");
                        if(names[1].equals(GUser.userId)) {
                            friendID = names[2];  //대화상대id(Guser랑 대화하는)
                        }else {
                            friendID = names[1];
                        }
                        Log.e("order", "첫 friendid: "+friendID);
                        if((item.userId).equals(GUser.userId)){
                            i++;
                            //최신글이 내가 쓴것이라면!!
                            //상대방닉네임이랑 프로필가져옴
                            friendIdList.add(friendID);
                            Log.w("order","friendlist("+i+"):  "+friendIdList.get(i));//하 이거 힘들었따. 쓰레드가 자동으로 되는건지 실행순서가 좀 다르게되서ㅠ
                            firestore.collection("users").document(friendIdList.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        friendName = documentSnapshot.getString("nickname");
                                        friendProfileUrl = documentSnapshot.getString("profile");
                                        item.name = friendName;
                                        item.profileUrl = friendProfileUrl;

                                        item.userId=friendIdList.get(j); //이건 여기서는 필요없지만 채팅방으로 이동할때 상대방의 userid필요해서 어댑터를 위해 한것임
                                        Log.w("order","777"+friendName+" & "+item.name+" & "+item.userId+" & firendid="+friendIdList.get(j));

                                        messageItems.add(item);
                                        Collections.sort(messageItems,sortByTime);
                                        Collections.reverse(messageItems);
                                        //여기함수가 젤 늦게끝나니까 여기서 셋어뎁터해줌
                                        adapter.notifyDataSetChanged();
                                        j++;

                                    }
                                }
                            });
                        }else{ //최신글을 내가 쓴것이 아니라면!!
                            Log.w("order","888"+friendName+" & "+item.name+" & "+item.userId);
                            messageItems.add(item); //업데이트됐으면 그걸로 추가, 안됏으면 그대로 추가됨
                            Collections.sort(messageItems,sortByTime);
                            Collections.reverse(messageItems);
                            //여기함수가 젤 늦게끝나니까 여기서 셋어뎁터해줌
                            adapter.notifyDataSetChanged();
//                            recyclerView.setAdapter(adapter);
                        }


                    }
//                    Log.d("roomname", "일치하는 방들 중 첫번째 방의 작성자"+messageItems.get(0).name);
//                    Log.d("roomname","일치하는 방 개수"+messageItems.size()+"");
                    Log.w("order","333..getinto()함수 한번끝");

                }
            }
        });
    }
    private final static Comparator<MessageItem>sortByTime = new Comparator<MessageItem>() {
        @Override
        public int compare(MessageItem o1, MessageItem o2) {
            return Collator.getInstance().compare(o1.time, o2.time);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2,container,false);
        Log.w("order","444");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.tab2Fragment_recyclerview);

        adapter = new Tab2_Chatlist_RecyclerAdapter(getActivity(),messageItems);
        Log.w("order","555");
        recyclerView.setAdapter(adapter);
        Log.w("order","666");
        refreshLayout= view.findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                messageItems.clear();
                loadData();
                refreshLayout.setRefreshing(false);
            }
        });
    }


}
