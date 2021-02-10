package com.hjhj.daedan;

import android.content.Context;
import android.content.Intent;
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
    ArrayList<Tab1_List_RecyclerItem> items;

    public Tab1_List_RecyclerAdapter(Context context, ArrayList<Tab1_List_RecyclerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.item_recycler_list_tab1, parent, false);
        VH holder = new VH(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh =(VH)holder;
        Tab1_List_RecyclerItem item = items.get(position);
        vh.tvNameSchool.setText(item.nickname+"("+item.school+")"); //"("+item.school+")"
        vh.tvMsg.setText(item.title);
        vh.tvLefttime.setText(item.duration);

        Glide.with(context).load(item.imgUrl).into(vh.ivCategory);
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
            this.ivCategory = itemView.findViewById(R.id.tab1_recycler_item_iv);
            this.tvNameSchool =  itemView.findViewById(R.id.tab1_recycler_item_nameschool);
            this.tvMsg = itemView.findViewById(R.id.tab1_recycler_item_msg);
            this.tvLefttime = itemView.findViewById(R.id.tab1_recycler_item_lefttime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getLayoutPosition();
                    String nickname = items.get(position).nickname;
                    String school = items.get(position).school;
                    String title = items.get(position).title;
                    String msg = items.get(position).msg;

                    Intent intent = new Intent(context, WatchViewActivity.class);
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("school",school);
                    intent.putExtra("title",title);
                    intent.putExtra("msg",msg);
                    context.startActivity(intent);



                }
            });
        }
    }

}
