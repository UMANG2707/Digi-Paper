package com.example.app3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.itemViewHolder> {
    private Context c;
    private List<Result> data;

    public ResultAdapter(Context c, List<Result> tempdt) {
        this.c = c;
        this.data = tempdt;
    }

    @NonNull
    @Override
    public ResultAdapter.itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card_layout,parent,false);
        return new itemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.itemViewHolder holder, int position) {

        Result temp = data.get(position);
        holder.subject.setText(temp.getSubject());
        holder.marks.setText(String.valueOf(temp.getScored_marks()));
        holder.total.setText(String.valueOf(temp.getTotal_marks()));
        holder.date.setText(temp.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder {

        public TextView subject;
        public TextView total;
        public TextView marks;
        public TextView date;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            total = itemView.findViewById(R.id.total);
            marks = itemView.findViewById(R.id.marks);
            date = itemView.findViewById(R.id.date);
        }
    }
}
