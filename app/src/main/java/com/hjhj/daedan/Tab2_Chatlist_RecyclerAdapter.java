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
import java.util.Arrays;

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
        vh.destUserId = item.userId;

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
        String destUserId; //대화상대방.. GUser아님

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


                    String users[] = {destUserId,GUser.userId};
                    Arrays.sort(users);//오름차순으로 정리
                    String chatRoomName="";
                    for(String aa: users){
                        chatRoomName+="&"+aa;
                    }
                    // TODO: 2021-02-19 글올린 지도 위치로 옮기는거 하고싶긴 한데 나중에..
                    intent.putExtra("destUserId", destUserId);
                    intent.putExtra("chatRoomName",chatRoomName);
                    context.startActivity(intent);
                    Log.d("toChat","챗리스트에서 챗으로 이동성공....chatRoomName: "+chatRoomName);
                    Log.d("toChat","챗리스트에서 챗으로 이동성공....destUserId: "+destUserId);
                }
            });
        }
    }
}
