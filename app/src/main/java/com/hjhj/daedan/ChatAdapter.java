package com.hjhj.daedan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    Context context;
    ArrayList<MessageItem> messageItems;

    public ChatAdapter(Context context, ArrayList<MessageItem> messageItems) {
        this.context = context;
        this.messageItems = messageItems;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageItem item = messageItems.get(position);
        View itemView = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        if(item.name.equals(GUser.nickname)){
            itemView = inflater.inflate(R.layout.my_messagebox,parent, false);
        }else{
            itemView = inflater.inflate(R.layout.other_messagebox, parent, false);
        }
        CircleImageView civ = itemView.findViewById(R.id.Messagebox_iv);
        TextView tvName = itemView.findViewById(R.id.Messagebox_tv_name);
        TextView tvMsg = itemView.findViewById(R.id.Messagebox_tv_msg);
        TextView tvTime = itemView.findViewById(R.id.Messagebox_tv_time);

        tvName.setText(item.name);
        tvMsg.setText(item.msg);
//        String adjustedTimeYear = item.time.substring(0,4)+"년";
        String adjustedTimeMonth = item.time.substring(4,6)+"월";
        String adjustedTimeDay = item.time.substring(6,8)+"일";
        String adjustedTimeHour = item.time.substring(8,10)+"시";
        String adjustedTimeMin = item.time.substring(10,12)+"분";
        String adjustedTime = adjustedTimeMonth+adjustedTimeDay+" "+adjustedTimeHour+adjustedTimeMin;
        tvTime.setText(adjustedTime);

        Glide.with(context).load(item.profileUrl).into(civ);

        return itemView;
    }
}
