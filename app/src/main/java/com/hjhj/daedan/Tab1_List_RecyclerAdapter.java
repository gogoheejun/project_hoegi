package com.hjhj.daedan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Tab1_List_RecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MarkersItem> items;

    public Tab1_List_RecyclerAdapter(Context context, ArrayList<MarkersItem> items) {
        Log.d("markeritem", "111");

        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("markeritem", "222");

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.item_recycler_list_tab1, parent, false);
        VH holder = new VH(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("markeritem", "333");

        VH vh =(VH)holder;
        MarkersItem item = items.get(position);
        vh.tvNameSchool.setText(item.nickname+"("+item.school+")"); //"("+item.school+")"
        vh.tvMsg.setText(item.title);
        vh.tvLefttime.setText(item.timeLength);

        if (item.category.equals("동아리 모집")) Glide.with(context).load(R.drawable.icon_club_2).into(vh.ivCategory);
        if (item.category.equals("미팅 구해요")) Glide.with(context).load(R.drawable.icon_date_2).into(vh.ivCategory);
        if (item.category.equals("스터디 모집")) Glide.with(context).load(R.drawable.icon_study_2).into(vh.ivCategory);
        if (item.category.equals("운동 모집")) Glide.with(context).load(R.drawable.icon_sports_2).into(vh.ivCategory);
        if (item.category.equals("파티 초대")) Glide.with(context).load(R.drawable.icon_party_2).into(vh.ivCategory);
        if (item.category.equals("기타")) Glide.with(context).load(R.drawable.icon_mic_2).into(vh.ivCategory);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivCategory;
        TextView tvNameSchool;
        TextView tvMsg;
        TextView tvLefttime;

        public VH(@NonNull View itemView) {
            super(itemView);
            Log.d("markeritem", "444");

            this.ivCategory = itemView.findViewById(R.id.tab1_recycler_item_iv);
            this.tvNameSchool =  itemView.findViewById(R.id.tab1_recycler_item_nameschool);
            this.tvMsg = itemView.findViewById(R.id.tab1_recycler_item_msg);
            this.tvLefttime = itemView.findViewById(R.id.tab1_recycler_item_lefttime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("markeritem", "555");

                    int position = getLayoutPosition();
                    String userid = items.get(position).userid;

                    Intent intent = new Intent(context,WatchViewActivity.class);
                    intent.putExtra("markerUserid",userid);
                    context.startActivity(intent);


                }
            });
        }
    }

}
