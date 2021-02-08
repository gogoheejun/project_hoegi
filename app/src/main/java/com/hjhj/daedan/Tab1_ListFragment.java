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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Tab1_ListFragment extends Fragment implements View.OnClickListener {
    ArrayList<Tab1_List_RecyclerItem> items = new ArrayList<Tab1_List_RecyclerItem>();
    RecyclerView recyclerView;
    Tab1_List_RecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo: 데이터에서 받아와야 함. 아이템 이미지url 스트링으로 뉴 해야(지금은 임시로 인트)
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_list_tab1,container,false);
        ImageView filter = view.findViewById(R.id.tab1MapPage_filter);
        filter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.tab1ListPage_recycler);
        adapter = new Tab1_List_RecyclerAdapter(getActivity(),items);
        recyclerView.setAdapter(adapter);

    }

    void loadData(){
        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo, "스터디 모집",
                "홍길동","한국외국어대학교","스터디모집해요","토익 스터디하고싶어요~ 토익만점가즈아~~~ 헬로 봉주르 니하오!","3일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
                "아이린","서울시립대학교","파티모집해요","외국인 교환학생과 와인파티합니다 어서서오세요. 참가비 만원~ 기린포차로 오세요","1일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"운동 모집",
                "수지","경희대학교","1축구할 사람!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo,"스터디 모집",
                "aaa","한국외국어대학교","스터디모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","3일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"운동 모집",
                "bbb","서울시립대학교","파티모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","1일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"운동 모집",
                "ccc","경희대학교","2축구할 사람!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo,"스터디 모집",
                "가가가","한국외국어대학교","스터디모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","3일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
                "나나나","서울시립대학교","파티모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","1일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
                "다다다","경희대학교","3파티해요!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.loginactivity_logo,"파티 초대",
                "라라라","한국외국어대학교","스터디모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","3일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"파티 초대",
                "마마마","서울시립대학교","파티모집해요","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","1일 후 삭제 "));
        items.add(new Tab1_List_RecyclerItem(R.drawable.menu_bnv_my,"스터디 모집",
                "바바바","경희대학교","4스터디모집!!","내일 1시 학교 운동장에서 할거에요. 포지션 상관없이 한명 구해요. 끝나고 고기도 먹어요! 연락주세요","2시간 후 삭제 "));

    }



    CheckBox chb_school1, chb_school2, chb_school3, chb_schoolall;
    CheckBox chb_category1,chb_category2,chb_category3,chb_category4,chb_category5,chb_category6,chb_categoryall;
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



                builder.setView(layout);
                builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo: 학교필터
                       filter_school();

                       //todo: 카테고리필터
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
