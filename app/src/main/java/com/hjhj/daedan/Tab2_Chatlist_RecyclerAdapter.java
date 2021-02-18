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

public class Tab2_Chatlist_RecyclerAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageItem> items;

    public Tab2_Chatlist_RecyclerAdapter(Context context, ArrayList<MessageItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.item_recycler_chatlist,parent,false);
        VH holder = new VH(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        VH vh = (VH)holder;
        MessageItem item = items.get(position);
        vh.tvNickname.setText(item.name); //"("+item.school+")"
        vh.tvMsg.setText(item.msg);
        vh.tvTime.setText(item.time);

        Glide.with(context).load(item.profileUrl).into(vh.ivProfile);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        TextView tvNickname;
        TextView tvMsg;
        TextView tvTime;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.ivProfile = itemView.findViewById(R.id.chatlist_ivProfile);
            this.tvNickname =  itemView.findViewById(R.id.chatlist_tvNickname);
            this.tvMsg = itemView.findViewById(R.id.chatlist_tvMsg);
            this.tvTime = itemView.findViewById(R.id.chatlist_tvTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ChatActivity.class);
//                    intent.putExtra("markerUserid",userid);
                    context.startActivity(intent);
                }
            });
        }
    }
}
