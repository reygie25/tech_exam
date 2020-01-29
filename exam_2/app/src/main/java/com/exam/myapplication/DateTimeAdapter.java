package com.exam.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exam.myapplication.model.DateAndTime;

import java.util.List;

public class DateTimeAdapter extends RecyclerView.Adapter<DateTimeAdapter.ViewHolder> {

    List<DateAndTime> list;
    OnRemoveItemListener listener;

    public interface OnRemoveItemListener{
        void onRemoveItem(int position);
    }

    public DateTimeAdapter(List<DateAndTime> list, OnRemoveItemListener listener){
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_time, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        DateAndTime d = list.get(position);
        String dateTime = d.getStartDateTime() + "  -  " + d.getEndDateTime();
        vh.tvDateTime.setText(dateTime);
        vh.ivRemove.setOnClickListener(v -> listener.onRemoveItem(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateTime;
        ImageView ivRemove;
        public ViewHolder(@NonNull View v) {
            super(v);
            ivRemove = v.findViewById(R.id.iv_remove);
            tvDateTime = v.findViewById(R.id.tv_date_time);
        }
    }
}
