package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Quiz_solution extends AppCompatActivity {

    RecyclerView menuRecyclerView;
//    Cadapter cadapter;
    QSadapter qSadapter;
    ArrayList<QuestionFragment> questionFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_solution);
//        Intent i = getIntent();
//        Bundle args = i.getBundleExtra("Question_fragments_Bundle");
//        questionFragments = (ArrayList<QuestionFragment>) i.getSerializableExtra("Question_fragments");
//        questionFragments = (ArrayList<QuestionFragment>) args.getSerializable("Question_fragments");
        questionFragments = Mcq_Quiz.questionFragments;
        menuRecyclerView = findViewById(R.id.rv_question_solution);
        LinearLayoutManager manager = new LinearLayoutManager(Quiz_solution.this);
        menuRecyclerView.setLayoutManager(manager);



    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Welcome to Quiz Solution", Toast.LENGTH_SHORT).show();
        qSadapter = new QSadapter(questionFragments,Quiz_solution.this);
        menuRecyclerView.setAdapter(qSadapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
