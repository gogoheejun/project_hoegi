package com.hjhj.daedan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tab2Fragment extends Fragment {
    ArrayList<MarkersItem> items = new ArrayList<MarkersItem>();
    RecyclerView recyclerView;
    Tab2_Chatlist_RecyclerAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    String chatRoomId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //db에서 채팅받아오기
        Log.d("roomname","111");
        loadData();
    }

    private void loadData() {
        String currentUser = GUser.userId;
        //document의 이름(id)가져와서 currentUser이름이 들어있는지 비교
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Log.d("roomname","222");
        firestore.collection("chats").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        Log.d("roomname","333");
                        for (int i=0; i<snapshots.size();i++){
                            chatRoomId =snapshots.getDocuments().get(i).getId();
                            Log.d("roomname",chatRoomId);
                        }
                    }
                });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.tab2Fragment_recyclerview);

        adapter = new Tab2_Chatlist_RecyclerAdapter(getActivity(),items);
        recyclerView.setAdapter(adapter);

        refreshLayout= view.findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                loadData();
                refreshLayout.setRefreshing(false);
            }
        });
    }


}
