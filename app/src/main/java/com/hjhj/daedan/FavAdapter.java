package com.hjhj.daedan;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MarkersItem> items;

    public FavAdapter(Context context, ArrayList<MarkersItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        ImageView ivProfile;
        ImageView ivMsg;
        TextView tvWriternickname;
        TextView tvMsg;
        TextView tvlikes;
        TextView tvdates;
        TextView tvCommentsNum;
        ToggleButton tbFavor;
    }
}
