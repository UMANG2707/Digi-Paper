package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.jar.Attributes;

public class Student_profile extends AppCompatActivity {


    SharedPreferences preferences;
    String userid;
    Context context=this;
    TextView s_name;
    TextView s_contact;
    TextView s_school;
    TextView s_mail;
    TextView s_standard;
    Button edit_profile;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        preferences = context.getSharedPreferences("com.example.app3_login_status", Context.MODE_PRIVATE);
        userid = preferences.getString("user_id","CNCS1");

        s_name = findViewById(R.id.student_name);
        s_contact = findViewById(R.id.student_phone);
        s_school = findViewById(R.id.student_school);
        s_mail = findViewById(R.id.student_email);
        s_standard = findViewById(R.id.student_std);
        edit_profile = findViewById(R.id.btn_edit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User").child("Student").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Name = (String) dataSnapshot.child("Name").getValue(String.class);
                String Contact =  dataSnapshot.child("Contact").getValue(String.class);
                String Email = (String) dataSnapshot.child("Mail").getValue(String.class);
                String Standard =  dataSnapshot.child("Standerd").getValue(String.class);
                String School = (String) dataSnapshot.child("School").getValue(String.class);

                s_name.setText(Name);
                s_contact.setText(Contact);
                s_mail.setText(Email);
                s_school.setText(School);
                s_standard.setText(Standard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(Student_profile.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_edit_student_profile,null);

        final EditText name_default = (EditText)mView.findViewById(R.id.edit_name);
        final EditText contact_default =(EditText)mView.findViewById(R.id.edit_contact);
        final EditText mail_default = (EditText)mView.findViewById(R.id.edit_mail);
        final EditText school_default = (EditText)mView.findViewById(R.id.edit_school);
        final EditText standerd_default = (EditText)mView.findViewById(R.id.edit_standerd);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel_changes);
        Button btn_save = (Button)mView.findViewById(R.id.save_changes);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        name_default.setText(s_name.getText());
        contact_default.setText(s_contact.getText());
        mail_default.setText(s_mail.getText());
        school_default.setText(s_school.getText());
        standerd_default.setText(s_standard.getText());

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Name").setValue(name_default.getText().toString());
                databaseReference.child("Contact").setValue(contact_default.getText().toString());
                databaseReference.child("Mail").setValue(mail_default.getText().toString());
                databaseReference.child("School").setValue(school_default.getText().toString());
                databaseReference.child("Standerd").setValue(standerd_default.getText().toString());
                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }
}
