package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Mcq_Quiz extends AppCompatActivity {

    private Toolbar T;
    private ViewPager V;
    private ViewPagerAdapter PA;
    private TabLayout TL;
    ArrayList<Mquestion> mquestions = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String Subject_Name;
    int no_of_questions;
    Context context = this;
    Button btn_quiz_submit;
    public static ArrayList<QuestionFragment> questionFragments = new ArrayList<QuestionFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq__quiz);
//        T = findViewById(R.id.toolbar);
//        setSupportActionBar(T);
        Intent i = getIntent();
        Subject_Name = i.getStringExtra("Subject_Name");
        no_of_questions = i.getIntExtra("No_of_questions", 6);
        V = findViewById(R.id.Pager);
        btn_quiz_submit = findViewById(R.id.btn_quiz_submit);
        SharedPreferences preferences = context.getSharedPreferences("com.example.app3_login_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("No_of_questions", no_of_questions);
        editor.putInt("Score", 0);
        editor.putString("Subject", Subject_Name);
        editor.commit();
        TL = findViewById(R.id.tabs);


        btn_quiz_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int na = 0;
                SharedPreferences preferences = context.getSharedPreferences("com.example.app3_login_status", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                for (int i = 0; i < PA.getCount(); i++) {
                    QuestionFragment qf = PA.getQF(i);
                    if (!qf.checkattempted()) {
                        na++;
                    }
                }
                final Dialog dialog = new Dialog(context);

                firebaseDatabase = FirebaseDatabase.getInstance();
                String userid = preferences.getString("user_id","CNCS1");
                databaseReference = firebaseDatabase.getReference("Result").child(userid);
                String id = databaseReference.push().getKey();
                String date = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));
                Result result = new Result(date,Subject_Name,no_of_questions,preferences.getInt("Score", 0));
                databaseReference.child(id).setValue(result);


                dialog.setContentView(R.layout.result_quiz);
                dialog.setTitle("Result");
                TextView tv_ca, tv_wa, tv_na;
                tv_ca = dialog.findViewById(R.id.tv_correct_answer);
                tv_na = dialog.findViewById(R.id.tv_na_answer);
                tv_wa = dialog.findViewById(R.id.tv_wrong_answer);
                tv_ca.setText("Correct Answers : " + preferences.getInt("Score", 0));
                tv_na.setText("Not Attempted : " + na);
                tv_wa.setText("Wrong Answer : " + (preferences.getInt("No_of_questions", 0) - na - preferences.getInt("Score", 0)));
                Button btn_view_solution = dialog.findViewById(R.id.btn_quiz_solution);

                btn_view_solution.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Solution Code
                        dialog.dismiss();
                        Intent i=new Intent(getApplicationContext(),Quiz_solution.class);
//                        Bundle args = new Bundle();
//                        args.putSerializable("Question_fragments", questionFragments);
//                        i.putExtra("Question_fragments_Bundle",args);
                        startActivity(i);


//                        Toast.makeText(Mcq_Quiz.this, " "+PA.getCount(), Toast.LENGTH_SHORT).show();
////                        Set_solution();
//
//                        for(int i=0;i<PA.getCount();i++)
//                        {
//                            Log.i("PA : ",""+PA.getCount());
////                            QuestionFragment qf = ;
//                            if(PA.questionFragments.get(i).checkattempted())
//                            {
//                                if(PA.questionFragments.get(i).checkanswer())
//                                {
////                                    TL.getTabAt(i).getCustomView().setBackgroundColor(Color.GREEN);
//                                    Log.i("COlor : ","Green");
//                                }
//                                else
//                                {
////                                    TL.getTabAt(i).getCustomView().setBackgroundColor(Color.RED);
//                                    Log.i("COlor : ","Red");
//
//                                }
//                            }
//                            PA.questionFragments.get(i).set_solution();
//                            questionFragments.set(i,PA.questionFragments.get(i));
//                        }

//                        PA = new ViewPagerAdapter(getSupportFragmentManager(),mquestions,no_of_questions,questionFragments); //
//                        V.setAdapter(PA);
//                        TL.setupWithViewPager(V);
//                        V.setCurrentItem(0);
//                        TL.getTabAt(0).select();
                    }
                });
//                Toast.makeText(context, "Not Attempted MCQquestion : "+na, Toast.LENGTH_SHORT).show();
                dialog.show();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Questions").child("MCQ").child(Subject_Name);


    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mquestions.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Mquestion q = ds.getValue(Mquestion.class);
                    mquestions.add(q);
//                    questionFragments.add(new QuestionFragment(q));
                }
                Collections.shuffle(mquestions);
                questionFragments.clear();
                for(int i=0;i<no_of_questions;i++)
                {
                    Mquestion Q = mquestions.get(i);
                    questionFragments.add(i,new QuestionFragment(Q));
                }
                PA = new ViewPagerAdapter(getSupportFragmentManager(), mquestions, no_of_questions, questionFragments); //
                V.setAdapter(PA);
                TL.setupWithViewPager(V);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        V.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (position == no_of_questions - 1) {
                    btn_quiz_submit.setVisibility(View.VISIBLE);
                } else {
                    btn_quiz_submit.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        super.onStart();
    }


//    public void Set_solution() {
////        questionFragments.clear();
//
//        for (int i = 0; i < questionFragments.size(); i++) {
//            Log.i("COlor", "" + i);
//            QuestionFragment qf = questionFragments.get(i);
//
//            if (qf.checkattempted()) {
//                try {
//
//                    if (qf.checkanswer()) {
//                        Log.i("COlor : ", "Green");
//                        TL.getTabAt(i).getCustomView().setBackgroundColor(Color.GREEN);
//                    } else {
//                        Log.i("COlor : ", "Red");
//                        TL.getTabAt(i).getCustomView().setBackgroundColor(Color.RED);
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//            qf.set_solution();
//            questionFragments.set(i, qf);
//            PA.setQF(i, qf);
//            PA.notifyDataSetChanged();
//
////                questionFragments.add(qf);
//        }
////        PA.updatedata(mquestions,no_of_questions,questionFragments); //
////        V.setAdapter(PA);
//
//        TL.getTabAt(0).select();
//
//
//    }


}
