package com.hjhj.daedan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class Tab2Fragment extends Fragment {
    ArrayList<MarkersItem> items = new ArrayList<MarkersItem>();
    RecyclerView recyclerView;
    Tab2_Chatlist_RecyclerAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //db에서 채팅받아오기
        loadData();
    }

    private void loadData() {
        items.add(new MarkersItem("schoolaa","nameaa","aa","aa","201002161408","33aa","titleaa","msgaa","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolbb","namebb","aa","aa","201002160101","33aa","titlebb","msgbb","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolcc","namecc","aa","aa","201002111111","33aa","titlecc","msgbb","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolaa","nameaa","aa","aa","201002161408","33aa","titleaa","msgaa","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolbb","namebb","aa","aa","201002160101","33aa","titlebb","msgbb","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolcc","namecc","aa","aa","201002111111","33aa","titlecc","msgbb","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolaa","nameaa","aa","aa","201002161408","33aa","titleaa","msgaa","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolbb","namebb","aa","aa","201002160101","33aa","titlebb","msgbb","imgurlaa","aa","aa"));
        items.add(new MarkersItem("schoolcc","namecc","aa","aa","201002111111","33aa","titlecc","msgbb","imgurlaa","aa","aa"));
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
