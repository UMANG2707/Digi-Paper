package com.example.app3;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QSadapter extends RecyclerView.Adapter<QSadapter.QViewHolder> {

    private ArrayList<QuestionFragment> questionFragments;

    View view;
    QViewHolder holder;
    private Context context;

    public QSadapter(ArrayList<QuestionFragment> questionFragments, Context context) {
        this.questionFragments = questionFragments;
        this.context = context;

    }


    @NonNull
    @Override
    public QViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_solution_item, parent, false);
        holder=new QViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QViewHolder holder, int position) {
        Log.i("QF Size : ",questionFragments.size()+"");
        QuestionFragment qf = questionFragments.get(position);
        holder.question.setText(qf.tv.getText().toString());
        if(qf.checkattempted())
        {
            if(qf.checkanswer())
            {
                holder.q_status.setText("Correct Answer");
                holder.q_ans.setText("");
                holder.q_correct_ans.setText(qf.get_correct_answer());
                holder.q_correct_ans.setTextColor(Color.parseColor("#006400"));
                holder.q_status.setTextColor(Color.parseColor("#006400"));

            }
            else
            {
                holder.q_status.setText("Wrong Answer");
                holder.q_status.setTextColor(Color.parseColor("#ff0000"));
                holder.q_ans.setTextColor(Color.parseColor("#ff0000"));

                holder.q_ans.setText(qf.get_current_answer());
                holder.q_correct_ans.setText(qf.get_correct_answer());
                holder.q_correct_ans.setTextColor(Color.parseColor("#006400"));

            }
        }
        else {
            holder.q_status.setText("Not Attempted");
            holder.q_ans.setText(" ");
            holder.q_correct_ans.setText(qf.get_correct_answer());
        }
    }

    @Override
    public int getItemCount() {
        return questionFragments.size();
    }

    class QViewHolder extends RecyclerView.ViewHolder{

        public TextView question,q_status,q_ans,q_correct_ans;


        public QViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.tv_question_name);
            q_status = itemView.findViewById(R.id.tv_question_status);
            q_ans = itemView.findViewById(R.id.tv_question_ans);
            q_correct_ans = itemView.findViewById(R.id.tv_question_cans);


        }
    }
}
