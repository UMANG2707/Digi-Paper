package com.example.app3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TeacherResultAdapter extends RecyclerView.Adapter<TeacherResultAdapter.itemViewHolder> {
    private Context c;
    private ArrayList<String> L;
    private SharedPreferences sharedPreferences;

    public TeacherResultAdapter(Context c, ArrayList<String> L) {
        this.c = c;
        this.L = L;
    }

    @NonNull
    @Override
    public TeacherResultAdapter.itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card,parent,false);
        return new itemViewHolder(v);
//
//        cviewholder.item_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(c.getApplicationContext(),StudentResult.class).putExtra("from",2);
//                c.startActivity(intent);
//            }
//        });
//
//        return cviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherResultAdapter.itemViewHolder holder, final int position) {

        holder.userid.setText(L.get(position));
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c.getApplicationContext(), StudentResult.class);
                intent.putExtra("from","TeacherResult");
                intent.putExtra("user_id",L.get(position));
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return L.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder {

        public TextView userid;
        public LinearLayout item_layout;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = (LinearLayout)itemView.findViewById(R.id.can_item_id);
            userid = itemView.findViewById(R.id.user_id);
        }
    }
}