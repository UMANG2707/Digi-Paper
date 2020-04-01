package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextInputEditText et_id,et_password;
    Button btn_loginup;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String t_id,t_password;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_id = findViewById(R.id.et_id);
        et_password = findViewById(R.id.et_password);

        btn_loginup = findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(this);
        context = getApplicationContext();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

//         FOR STUDENT PASSWORD AND ID GENERATOR
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("User").child("Student");
//        String id = "CNCS";
//        String pass = "temps";
//        for(int i =1;i<=10;i++)
//        {
//            String nid = id+i;
//            String npass = pass+i;
//            databaseReference.child(nid).child("Password").setValue(npass);
//            databaseReference.child(nid).child("id").setValue(nid);
//
//
//        }
//
//
////         FOR TEACHER PASSWORD AND ID GENERATOR
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("User").child("Teacher");
//        id = "CNCT";
//        pass = "tempT";
//        for(int i =1;i<=10;i++)
//        {
//            String nid = id+i;
//            String npass = pass+i;
//            databaseReference.child(nid).child("Password").setValue(npass);
//            databaseReference.child(nid).child("id").setValue(nid);
//        }

        btn_loginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t_id = et_id.getText().toString();
                t_password = et_password.getText().toString();
                if(TextUtils.isEmpty(et_id.getText().toString()) || TextUtils.isEmpty(et_password.getText().toString()))
                {
                    et_id.setError("Invalid Username or Password");
                }
                else
                {
                    progressDialog.setMessage("Loging in");
                    progressDialog.show();
                    if('S'==et_id.getText().toString().charAt(3))
                    {

                        databaseReference.child("Student").orderByChild("id").equalTo(t_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if((dataSnapshot.exists()) && (t_password.equals(dataSnapshot.child(t_id).child("Password").getValue().toString())))
                                {
                                    progressDialog.dismiss();
                                    Store(true);
                                    startActivity(new Intent(getApplicationContext(),student_dashboard.class));
                                    finish();

                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    et_password.setError("Invalid Password");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else if('T'==et_id.getText().toString().charAt(3))
                    {

                        databaseReference.child("Teacher").orderByChild("id").equalTo(t_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if((dataSnapshot.exists()) && (t_password.equals(dataSnapshot.child(t_id).child("Password").getValue().toString())))
                                {
                                    progressDialog.dismiss();
                                    Store(false);
                                    startActivity(new Intent(getApplicationContext(),teacher_activity.class));
                                    finish();

                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    et_password.setError("Invalid Password");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else
                    {
                        progressDialog.dismiss();
                        et_id.setError("Enter Valid UserId and Password");
                    }
                }
            }
        });



    }

    private void Store(boolean profile)
    {
        SharedPreferences preferences = context.getSharedPreferences("com.example.app3_login_status",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login_status","on");
        editor.putString("user_id",t_id);
        if(profile)
        {
            editor.putString("Profession","Student");
        }
        else
        {
            editor.putString("Profession","Teacher");
        }
        editor.commit();



    }

    @Override
    protected void onStart() {
        checkuserstatus();
        super.onStart();
    }

    private void checkuserstatus() {
        SharedPreferences preferences = context.getSharedPreferences("com.example.app3_login_status",Context.MODE_PRIVATE);
        String profession = preferences.getString("Profession","empty");
        String check = preferences.getString("login_status","off");
        if(check.equals("on"))
        {
            if(profession.equals("Student"))
            {
                startActivity(new Intent(getApplicationContext(),student_dashboard.class));

            }
            else
            {
                startActivity(new Intent(getApplicationContext(),teacher_activity.class));

            }
            finish();
        }
    }
}
