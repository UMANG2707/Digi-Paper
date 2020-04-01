package com.example.app3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;


public class QuestionFragment extends Fragment {
    TextView tv;

    private String correctanswer;
    private Mquestion mquestion;
    Boolean flag=false;
    Context context=getContext();
    int score,correctanswer_id;
    View view;

    RadioButton opA,opB,opC,opD;
    RadioGroup optionsGroup;




    public QuestionFragment()
    {

    }

    public QuestionFragment(Mquestion mquestion)
    {
        this.mquestion = mquestion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_question, container, false);

        tv = view.findViewById(R.id.txt_display);
        optionsGroup = view.findViewById(R.id.options);
        Log.i("testtest", "group: " + (optionsGroup == null));
        opA = view.findViewById(R.id.optionA);
        opB = view.findViewById(R.id.optionB);
        opC = view.findViewById(R.id.optionC);
        opD = view.findViewById(R.id.optionD);
        tv.setText(mquestion.getQuestion());
        correctanswer = mquestion.getCorrectanswer();
        opA.setText(mquestion.getOp1());
        opB.setText(mquestion.getOp2());
        opC.setText(mquestion.getOp3());
        opD.setText(mquestion.getOp4());
        opA.setTextColor(Color.WHITE);
        opB.setTextColor(Color.WHITE);
        opC.setTextColor(Color.WHITE);
        opD.setTextColor(Color.WHITE);



        for(int i=0;i<4;i++)
        {
            optionsGroup.getChildAt(i).setEnabled(true);
        }

        if(correctanswer.equals(opA.getText()))
        {
            correctanswer_id = opA.getId();
        }
        else if(correctanswer.equals(opB.getText()))
        {
            correctanswer_id = opB.getId();
        }
        else if(correctanswer.equals(opC.getText()))
        {
            correctanswer_id = opC.getId();
        }
        else
        {
            correctanswer_id = opD.getId();
        }
//        opA.setChecked(true);

        optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                SharedPreferences preferences = getActivity().getSharedPreferences("com.example.app3_login_status", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                score = preferences.getInt("Score",0);
                if((opA.getId() == i && correctanswer.equals(opA.getText())) ||
                        (opB.getId() == i && correctanswer.equals(opB.getText())) ||
                        (opC.getId() == i && correctanswer.equals(opC.getText())) ||
                        (opD.getId() == i && correctanswer.equals(opD.getText())))
                {
                    if(!flag)
                    {
                        flag=true;
                       score=score+1;
                       editor.putInt("Score",score);

                        Toast.makeText(getContext(), "Score : "+score, Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(getContext(), "Correct Choice Ans : A", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    if(flag)
                    {
                        score=score-1;
                        flag=false;
                        editor.putInt("Score",score);

                        Toast.makeText(getContext(), "Score : "+score, Toast.LENGTH_SHORT).show();
                    }

//                    Toast.makeText(getContext(), "Wrong Choice", Toast.LENGTH_SHORT).show();
                }
                editor.commit();

            }
        });

//        tv.setText(getArguments().getString("message"));
        return view;
    }

    public boolean checkattempted()
    {
            if(optionsGroup.getCheckedRadioButtonId() == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean checkanswer()
    {
        if(optionsGroup.getCheckedRadioButtonId() == correctanswer_id)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String get_current_answer()
    {
        RadioButton rd = view.findViewById(optionsGroup.getCheckedRadioButtonId());
        return rd.getText().toString();
    }

    public String get_correct_answer()
    {
        RadioButton rd = view.findViewById(correctanswer_id);
        return rd.getText().toString();
    }


}
