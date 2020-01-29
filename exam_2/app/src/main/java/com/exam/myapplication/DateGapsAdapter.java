package com.exam.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exam.myapplication.model.DateAndGaps;

import java.util.List;

public class DateGapsAdapter extends RecyclerView.Adapter<DateGapsAdapter.ViewHolder> {

    private List<DateAndGaps> list;

    public DateGapsAdapter(List<DateAndGaps> list){
        this.list  = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_gaps, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        DateAndGaps d = list.get(position);
        vh.tvDate.setText("Date :" + d.getDate());

        if(d.getGaps() != null && d.getGaps().size() > 1 ) {
            StringBuilder sb = new StringBuilder();
            for (String gap : d.getGaps()) {
                sb.append(gap)
                        .append("\n");
            }
            vh.tvGaps.setText("Gaps:");
            vh.tvGaps.setText(sb.toString());
        }else
            vh.tvGaps.setText("None");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvGaps;
        public ViewHolder(@NonNull View v) {
            super(v);
            tvDate = v.findViewById(R.id.tv_date);
            tvGaps = v.findViewById(R.id.tv_gaps);
        }
    }
}
