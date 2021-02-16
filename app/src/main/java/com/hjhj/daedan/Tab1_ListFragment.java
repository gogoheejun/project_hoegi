package com.hjhj.daedan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Tab1_ListFragment extends Fragment implements View.OnClickListener {
    ArrayList<MarkersItem> items = new ArrayList<MarkersItem>();
    RecyclerView recyclerView;
    Tab1_List_RecyclerAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo: 데이터에서 받아와야 함. 아이템 이미지url 스트링으로 뉴 해야(지금은 임시로 인트)
        Log.d("TAG", "list_onCreate");
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "list_onCreatView");
        View view = inflater.inflate(R.layout.page_list_tab1,container,false);
        ImageView filter = view.findViewById(R.id.tab1MapPage_filter);
        filter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "list_oncViewCreated");
        recyclerView = view.findViewById(R.id.tab1ListPage_recycler);
        refreshLayout= view.findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                loadData();
                refreshLayout.setRefreshing(false);
            }
        });
        //원래는 여기에
//        adapter = new Tab1_List_RecyclerAdapter(getActivity(),items);
//        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "list_onResume");

    }

    CheckBox chb_school1, chb_school2, chb_school3, chb_schoolall;
    CheckBox chb_category1,chb_category2,chb_category3,chb_category4,chb_category5,chb_category6,chb_categoryall;
    Switch favSwtich;
    String filteredSchool1, filteredSchool2,filteredSchool3;
    String filteredCategory1,filteredCategory2,filteredCategory3,filteredCategory4,filteredCategory5,filteredCategory6;
    ArrayList<String> filters = new ArrayList<>();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1MapPage_filter:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_filter_tab1,null);

                chb_school1 = layout.findViewById(R.id.tab1_dialog_filter_kyunghee);
                chb_school1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_schoolall.setChecked(false);
                    }
                });
                chb_school2 = layout.findViewById(R.id.tab1_dialog_filter_uos);
                chb_school2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_schoolall.setChecked(false);
                    }
                });
                chb_school3 = layout.findViewById(R.id.tab1_dialog_filter_hufs);
                chb_school3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_schoolall.setChecked(false);
                    }
                });
                chb_schoolall =  layout.findViewById(R.id.tab1_dialog_filter_allschool);

                chb_category1 = layout.findViewById(R.id.tab1_dialog_filter_category_club);
                chb_category1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_categoryall.setChecked(false);
                    }
                });
                chb_category2 = layout.findViewById(R.id.tab1_dialog_filter_category_meeting);
                chb_category2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_categoryall.setChecked(false);
                    }
                });
                chb_category3 = layout.findViewById(R.id.tab1_dialog_filter_category_study);
                chb_category3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_categoryall.setChecked(false);
                    }
                });
                chb_category4 = layout.findViewById(R.id.tab1_dialog_filter_category_sports);
                chb_category4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_categoryall.setChecked(false);
                    }
                });
                chb_category5 = layout.findViewById(R.id.tab1_dialog_filter_category_party);
                chb_category5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_categoryall.setChecked(false);
                    }
                });
                chb_category6 = layout.findViewById(R.id.tab1_dialog_filter_category_etc);
                chb_category6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) chb_categoryall.setChecked(false);
                    }
                });
                chb_categoryall = layout.findViewById(R.id.tab1_dialog_filter_category_all);

                favSwtich = layout.findViewById(R.id.tab1_dialog_filter_Switch);


                builder.setView(layout);

                builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO: 필터따라서 데이터 로드하기

                        filteredSchool1= null; //처음 널로 해줘야지 다시 필터링 또할때 초기화되서 다시 비교해줌
                        filteredSchool2 = null;
                        filteredSchool3 = null;
                        filteredCategory1 =null;
                        filteredCategory2 =null;
                        filteredCategory3 =null;
                        filteredCategory4 =null;
                        filteredCategory5 =null;
                        filteredCategory6 =null;
                        //학교필터
                        if (chb_school1.isChecked() || chb_schoolall.isChecked()) filteredSchool1 = chb_school1.getText().toString();
                        if (chb_school2.isChecked() || chb_schoolall.isChecked()) filteredSchool2 = chb_school2.getText().toString();
                        if (chb_school3.isChecked() || chb_schoolall.isChecked()) filteredSchool3 = chb_school3.getText().toString();
                        //테고리필터
                        if (chb_category1.isChecked() || chb_categoryall.isChecked()) filteredCategory1 = chb_category1.getText().toString();
                        if (chb_category2.isChecked() || chb_categoryall.isChecked()) filteredCategory2 = chb_category2.getText().toString();
                        if (chb_category3.isChecked() || chb_categoryall.isChecked()) filteredCategory3 = chb_category3.getText().toString();
                        if (chb_category4.isChecked() || chb_categoryall.isChecked()) filteredCategory4 = chb_category4.getText().toString();
                        if (chb_category5.isChecked() || chb_categoryall.isChecked()) filteredCategory5 = chb_category5.getText().toString();
                        if (chb_category6.isChecked() || chb_categoryall.isChecked()) filteredCategory6 = chb_category6.getText().toString();

//                        gMap.clear(); 이거 대신 리사이클러뷰 다 클리어시켜야 함
                        items.clear();
                        loadDataWithFilters();



//                        //학교필터
//                       filter_school();
//
//                       //테고리필터
//                        filter_category();


                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();



                break;
        }
    }//onclick();필터링버튼..

    public void loadDataWithFilters(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("markers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Map<String,Object>  marker= document.getData();
                    Log.d("TAg","tab1 list loadDAta with filters");
                        MarkersItem_static.lat = marker.get("lat").toString();
                        MarkersItem_static.lon = marker.get("lon").toString();
                        MarkersItem_static.title = marker.get("title").toString();
                        MarkersItem_static.category = marker.get("category").toString();
                        MarkersItem_static.uploadTime = marker.get("uploadTime").toString();
                        MarkersItem_static.school = marker.get("school").toString();
                        MarkersItem_static.message = marker.get("message").toString();
                        MarkersItem_static.timeLength = marker.get("timeLength").toString();
                        MarkersItem_static.userid = marker.get("userid").toString();
                        MarkersItem_static.imgUrl = marker.get("imgUrl").toString();
                        MarkersItem_static.nickname = marker.get("nickname").toString();


                        if( !MarkersItem_static.school.equals(filteredSchool1)  && !MarkersItem_static.school.equals(filteredSchool2) && !MarkersItem_static.school.equals(filteredSchool3)){
//                            Log.d("TAG","##List##111 afterFilter "+ MarkersItem_static.school+ "&&"+filteredSchool1+filteredSchool2+filteredSchool3 );
                            continue;
                        }
//                        Log.d("TAG","##List##100??? ");
//                        Log.d("TAG","##List##100 afterFilter "+ MarkersItem_static.school+ "&&"+filteredSchool1+filteredSchool2+filteredSchool3 );

                        if(!MarkersItem_static.category.equals(filteredCategory1) && !MarkersItem_static.category.equals(filteredCategory2) && !MarkersItem_static.category.equals(filteredCategory3)
                                &&!MarkersItem_static.category.equals(filteredCategory4)&& !MarkersItem_static.category.equals(filteredCategory5)&&!MarkersItem_static.category.equals(filteredCategory6)) {
                            Log.d("TAG","afterFilter 2.8  "+ MarkersItem_static.category+ "&&"+filteredCategory1+filteredCategory2+filteredCategory3+ filteredCategory4+filteredCategory5+filteredCategory6);
                            continue;
                        }
                        items.add( new MarkersItem(marker.get("school").toString(),marker.get("nickname").toString(),marker.get("userid").toString(),marker.get("category").toString(),
                                marker.get("uploadTime").toString(), marker.get("timeLength").toString(),marker.get("title").toString(),marker.get("message").toString(), marker.get("imgUrl").toString(),
                                marker.get("lat").toString(), marker.get("lon").toString()));
//                        Log.d("TAG","##List##222 afterFilter "+ items.get(0).nickname );

                    }

                    adapter = new Tab1_List_RecyclerAdapter(getActivity(),items);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    //db에서 데이터 가져오기....onCreate에서 쓰임
    void loadData(){
        Log.d("TAG", "list_loadData");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("markers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //일부러 MarkersItem을 MarkersItem_static이랑 따로만듦. 스태틱으로만 하면 item안에 있는 요소들 다 똑같아짐
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> marker = document.getData();

                        items.add( new MarkersItem(marker.get("school").toString(),marker.get("nickname").toString(),marker.get("userid").toString(),marker.get("category").toString(),
                                marker.get("uploadTime").toString(), marker.get("timeLength").toString(),marker.get("title").toString(),marker.get("message").toString(), marker.get("imgUrl").toString(),
                                marker.get("lat").toString(), marker.get("lon").toString()));


                    }
                    Log.d("markeritem",items.get(0).nickname+"\n"+items.get(1).nickname+"\n"+items.get(2).nickname);

                    //리사이클러뷰에 어댑터랑 items를 결합!...원래는 onViewCreated에서 했었는데 여기선 데이터를 다 받아온 다음에 해야하니까
//                    여기서 함
                    adapter = new Tab1_List_RecyclerAdapter(getActivity(),items);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    void filter_school(){
        if(chb_school1.isChecked()) filters.add(chb_school1.getText().toString());
        if(chb_school2.isChecked()) filters.add(chb_school2.getText().toString());
        if(chb_school3.isChecked()) filters.add(chb_school3.getText().toString());
        if(chb_schoolall.isChecked()){
            filters.add(chb_school1.getText().toString());
            filters.add(chb_school2.getText().toString());
            filters.add(chb_school3.getText().toString());
        }
        //---
        if(chb_category1.isChecked()) filters.add(chb_category1.getText().toString());
        if(chb_category2.isChecked()) filters.add(chb_category2.getText().toString());
        if(chb_category3.isChecked()) filters.add(chb_category3.getText().toString());
        if(chb_category4.isChecked()) filters.add(chb_category4.getText().toString());
        if(chb_category5.isChecked()) filters.add(chb_category5.getText().toString());
        if(chb_category6.isChecked()) filters.add(chb_category6.getText().toString());
        if(chb_categoryall.isChecked()){
            filters.add(chb_category1.getText().toString());
            filters.add(chb_category2.getText().toString());
            filters.add(chb_category3.getText().toString());
            filters.add(chb_category4.getText().toString());
            filters.add(chb_category5.getText().toString());
            filters.add(chb_category6.getText().toString());
        }


        items.clear();  // 몇번 반복했을수있으니까 다 없애고 다시시작
        adapter.notifyDataSetChanged();
        loadData();
        adapter.notifyDataSetChanged();
        for(int i=items.size()-1; i>=0; i--){

            if( filters.size()>0 && !((filters.contains(items.get(i).school))&&filters.contains(items.get(i).category))) {
                Log.e("TAG",items.get(i).school+" "+items.get(i).category);
                items.remove(i);
                adapter.notifyItemRemoved(i);
            }
            if( filters.size()==0) {
                items.clear();
                adapter.notifyDataSetChanged();
                loadData();
                adapter.notifyDataSetChanged();
            }
        }
        filters.clear();
    }//filter_school();

    void filter_category(){


    }
}
