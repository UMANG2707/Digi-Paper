package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentResult extends AppCompatActivity {

    SharedPreferences preferences;
    String userid;
    Context context=this;
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private List<Result> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        preferences = context.getSharedPreferences("com.example.app3_login_status", Context.MODE_PRIVATE);
        if(getIntent().getStringExtra("from").equals("TeacherResult"))
        {
            userid = getIntent().getStringExtra("user_id");
        }
        else
        {
            userid = preferences.getString("user_id","CNCS1");
        }

        Log.i("User Id SR",userid);
        recyclerView = findViewById(R.id.rv_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        data = new ArrayList<>();

        DatabaseReference mydatabasereference = FirebaseDatabase.getInstance().getReference("Result").child(userid);

        mydatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    Result tmp = d.getValue(Result.class);
                    data.add(tmp);
                }
                adapter = new ResultAdapter(StudentResult.this,data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        Log.i("Hi","Hello I am from start");
        super.onStart();
    }
}
