package com.example.app3;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Mquestion> mquestions;
    private int no_of_questions;

    public ArrayList<QuestionFragment> questionFragments;

    public ViewPagerAdapter(FragmentManager fm,ArrayList<Mquestion> mquestionlist,int no_of_questions,ArrayList<QuestionFragment> questionFragments) {
        super(fm);
        this.mquestions = mquestionlist;
        //mquestionlist;

//        for (int i = 0; i < no_of_questions; i++){
//            this.mquestions.add(mquestionlist.get(i));
//        }

        this.no_of_questions = no_of_questions;
        this.questionFragments =  questionFragments;

    }

    @Override
    public Fragment getItem(int position) {
//        QuestionFragment df = new QuestionFragment(mquestions.get(position));
//        questionFragments.add(df);
//        Bundle B = new Bundle();
//        B.putString("message","Q:"+position);
//        df.setArguments(B);
        return questionFragments.get(position);
    }

    public QuestionFragment getQF(int position)
    {
        return questionFragments.get(position);
    }
    public void setQF(int pos, QuestionFragment q){
        questionFragments.set(pos, q);
    }




    @Override
    public int getCount() {
        Log.i("testtest", "getCount Size: " + mquestions.size());
        return questionFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position = position+1;
        return "Q("+position+")";
    }
}
