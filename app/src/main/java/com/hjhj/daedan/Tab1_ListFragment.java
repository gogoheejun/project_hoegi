package com.hjhj.daedan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo: 데이터에서 받아와야 함. 아이템 이미지url 스트링으로 뉴 해야(지금은 임시로 인트)
        Log.d("markeritem", "list_onCreate");
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("markeritem", "list_onCreatView");
        View view = inflater.inflate(R.layout.page_list_tab1,container,false);
        ImageView filter = view.findViewById(R.id.tab1MapPage_filter);
        filter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("markeritem", "list_oncViewCreated");
        recyclerView = view.findViewById(R.id.tab1ListPage_recycler);
//        adapter = new Tab1_List_RecyclerAdapter(getActivity(),items);
//        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("markeritem", "list_onResume");
//
//        adapter = new Tab1_List_RecyclerAdapter(getActivity(),items);
//        recyclerView.setAdapter(adapter);
    }

    void loadData(){
        Log.d("markeritem", "list_loadData");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("markers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> marker = document.getData();
//                        MarkersItem markersItem = new MarkersItem();
//
//                        markersItem.lat = marker.get("lat").toString();
//                        markersItem.lon = marker.get("lon").toString();
//                        markersItem.title = marker.get("title").toString();
//                        markersItem.category = marker.get("category").toString();
//                        markersItem.uploadTime = marker.get("uploadTime").toString();
//                        markersItem.school = marker.get("school").toString();
//                        markersItem.message = marker.get("message").toString();
//                        markersItem.timeLength = marker.get("timeLength").toString();
//                        markersItem.userid = marker.get("userid").toString();
//                        markersItem.imgUrl = marker.get("imgUrl").toString();
//                        markersItem.nickname = marker.get("nickname").toString();

                        items.add( new MarkersItem(marker.get("school").toString(),marker.get("nickname").toString(),marker.get("userid").toString(),marker.get("category").toString(),
                                marker.get("uploadTime").toString(), marker.get("timeLength").toString(),marker.get("title").toString(),marker.get("message").toString(), marker.get("imgUrl").toString(),
                                marker.get("lat").toString(), marker.get("lon").toString()));

                        Log.d("markeritem", marker.get("school").toString());
                        Log.d("markeritem", marker.get("nickname").toString());
                        Log.d("markeritem",marker.get("userid").toString());
                        Log.d("markeritem", marker.get("category").toString());
                        Log.d("markeritem", marker.get("uploadTime").toString());
                        Log.d("markeritem",  marker.get("timeLength").toString());

                    }
                    Log.d("markeritem",items.get(0).nickname+"\n"+items.get(1).nickname+"\n"+items.get(2).nickname);
                    adapter = new Tab1_List_RecyclerAdapter(getActivity(),items);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
//        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo, "스터디 모집",
//                "홍길동","한국외국어대학교","스터디모집해요","토익 스터디하고싶어요~ 토익만점가즈아~~~ 헬로 봉주르 니하오!","3일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
//                "아이린","서울시립대학교","파티모집해요","외국인 교환학생과 와인파티합니다 어서서오세요. 참가비 만원~ 기린포차로 오세요","1일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"운동 모집",
//                "수지","경희대학교","1축구할 사람!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo,"스터디 모집",
//                "aaa","한국외국어대학교","스터디모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","3일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"운동 모집",
//                "bbb","서울시립대학교","파티모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","1일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"운동 모집",
//                "ccc","경희대학교","2축구할 사람!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo,"스터디 모집",
//                "가가가","한국외국어대학교","스터디모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","3일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
//                "나나나","서울시립대학교","파티모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","1일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
//                "다다다","경희대학교","3파티해요!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo,"파티 초대",
//                "라라라","한국외국어대학교","스터디모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","3일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
//                "마마마","서울시립대학교","파티모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","1일 후 삭제 "));
//        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"스터디 모집",
//                "바바바","경희대학교","4스터디모집!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));

    }

// TODO: 2021-02-14 이제 필터링해야함!! 

    CheckBox chb_school1, chb_school2, chb_school3, chb_schoolall;
    CheckBox chb_category1,chb_category2,chb_category3,chb_category4,chb_category5,chb_category6,chb_categoryall;
    Switch favSwtich;
    ArrayList<String> filters = new ArrayList<>();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1MapPage_filter:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_filter_tab1,null);

                chb_school1 = layout.findViewById(R.id.tab1_dialog_filter_kyunghee);
                chb_school2 = layout.findViewById(R.id.tab1_dialog_filter_uos);
                chb_school3 = layout.findViewById(R.id.tab1_dialog_filter_hufs);
                chb_schoolall =  layout.findViewById(R.id.tab1_dialog_filter_allschool);

                chb_category1 = layout.findViewById(R.id.tab1_dialog_filter_category_club);
                chb_category2 = layout.findViewById(R.id.tab1_dialog_filter_category_meeting);
                chb_category3 = layout.findViewById(R.id.tab1_dialog_filter_category_study);
                chb_category4 = layout.findViewById(R.id.tab1_dialog_filter_category_sports);
                chb_category5 = layout.findViewById(R.id.tab1_dialog_filter_category_party);
                chb_category6 = layout.findViewById(R.id.tab1_dialog_filter_category_etc);
                chb_categoryall = layout.findViewById(R.id.tab1_dialog_filter_category_all);

                favSwtich = layout.findViewById(R.id.tab1_dialog_filter_Switch);


                builder.setView(layout);

                builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO: db에서 하트 체크된것먼저 다 가져오기..굳이 여기서 할필요있나

                        //학교필터
                       filter_school();

                       //테고리필터
                        filter_category();


                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();



                break;
        }
    }//onclick();

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
