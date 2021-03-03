package com.hjhj.daedan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MarkersItem> items;

    public FavAdapter(Context context, ArrayList<MarkersItem> items) {
        this.context = context;
        this.items = items;
        Log.d("favAdapter", "111");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("favAdapter", "onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.item_recycler_fav, parent, false);
        VH holder = new VH(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("favAdapter", "onBindViewHolder");


        VH vh =(VH)holder;
        MarkersItem item = items.get(position);
        vh.tvNameSchool.setText(item.nickname+"("+item.school+")"); //"("+item.school+")"
        vh.tvMsg.setText(item.title);
//        Log.d("favAdapter", "onBindViewHolder test"+item.nickname);



        if (item.category.equals("동아리 모집")) Glide.with(context).load(R.drawable.icon_club_2).into(vh.ivCategory);
        if (item.category.equals("미팅 구해요")) Glide.with(context).load(R.drawable.icon_date_2).into(vh.ivCategory);
        if (item.category.equals("스터디 모집")) Glide.with(context).load(R.drawable.icon_study_2).into(vh.ivCategory);
        if (item.category.equals("운동 모집")) Glide.with(context).load(R.drawable.icon_sports_2).into(vh.ivCategory);
        if (item.category.equals("파티 초대")) Glide.with(context).load(R.drawable.icon_party_2).into(vh.ivCategory);
        if (item.category.equals("기타")) Glide.with(context).load(R.drawable.icon_mic_2).into(vh.ivCategory);
    }

    @Override
    public int getItemCount() {
        Log.d("favAdapter", "getItemCount");
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {
        CircleImageView ivCategory;
        TextView tvNameSchool;
        TextView tvMsg;


        public VH(@NonNull View itemView) {
            super(itemView);
            Log.d("favAdapter", "VH");

            this.ivCategory = itemView.findViewById(R.id.fav_item_iv);
            this.tvNameSchool = itemView.findViewById(R.id.fav_item_nameschool);
            this.tvMsg = itemView.findViewById(R.id.fav_item_msg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
