package com.hjhj.daedan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

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
        View itemview = inflater.inflate(R.layout.item_recycler_chatlist, parent, false);
        VH holder = new VH(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        VH vh = (VH) holder;
        MessageItem item = items.get(position);
        vh.tvNickname.setText(item.name); //"("+item.school+")"
        vh.tvMsg.setText(item.msg);


        String year =item.time.substring(0,4);
        String month = item.time.substring(4,6);
        String day = item.time.substring(6,8);
        String hour = item.time.substring(8,10);
        String min = item.time.substring(10,12);
        vh.tvTime.setText(year+"년 "+month+"월"+day+"일 "+hour+":"+min);
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
        String chatRoomName;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.ivProfile = itemView.findViewById(R.id.chatlist_ivProfile);
            this.tvNickname = itemView.findViewById(R.id.chatlist_tvNickname);
            this.tvMsg = itemView.findViewById(R.id.chatlist_tvMsg);
            this.tvTime = itemView.findViewById(R.id.chatlist_tvTime);

//            String users[] = {destUserId, GUser.userId};
//            Arrays.sort(users);//오름차순으로 정리
//            chatRoomName = "";
//            for (String aa : users) {
//                chatRoomName += "&" + aa;
//            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);


                    String users[] = {destUserId, GUser.userId};
                    Arrays.sort(users);//오름차순으로 정리
                    chatRoomName = "";
                    for (String aa : users) {
                        chatRoomName += "&" + aa;
                    }
                    // TODO: 2021-02-19 글올린 지도 위치로 옮기는거 하고싶긴 한데 나중에..
                    intent.putExtra("destUserId", destUserId);
                    intent.putExtra("chatRoomName", chatRoomName);
                    context.startActivity(intent);
                    Log.d("toChat", "챗리스트에서 챗으로 이동성공....chatRoomName: " + chatRoomName);
                    Log.d("toChat", "챗리스트에서 챗으로 이동성공....destUserId: " + destUserId);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //데이터에서 삭제!
                    new AlertDialog.Builder(context).setTitle("이 대화에서 나가시겠습니까?(나와 상대방 모두에게서 삭제됩니다)")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String users[] = {destUserId, GUser.userId};
                                    Arrays.sort(users);//오름차순으로 정리
                                    chatRoomName = "";
                                    for (String aa : users) {
                                        chatRoomName += "&" + aa;
                                    }
                                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                    firestore.collection("chats").document(chatRoomName)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "안지워짐;;", Toast.LENGTH_SHORT).show();     
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    return true;
                }
            });
        }
    }
}
