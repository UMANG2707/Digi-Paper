package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class teacher_profile extends AppCompatActivity {

    SharedPreferences preferences;
    String userid;
    Context context=this;
    TextView t_name;
    TextView t_contact;
    TextView t_mail;
    Button edit_profile;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        preferences = context.getSharedPreferences("com.example.app3_login_status", Context.MODE_PRIVATE);
        userid = preferences.getString("user_id","CNCT1");
        t_name = findViewById(R.id.teacher_name);
        t_contact = findViewById(R.id.teacher_phone);
        t_mail = findViewById(R.id.teacher_email);
        edit_profile = findViewById(R.id.btn_edit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User").child("Teacher").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Name = (String) dataSnapshot.child("Name").getValue(String.class);
                String Contact = dataSnapshot.child("Contact").getValue(String.class);
                String Email = (String) dataSnapshot.child("Mail").getValue(String.class);

                t_name.setText(Name);
                t_contact.setText(Contact);
                t_mail.setText(Email);
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

    private void openDialog()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(teacher_profile.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_edit_teacher_profile,null);

        final EditText name_default = (EditText)mView.findViewById(R.id.edit_name);
        final EditText mail_default = (EditText)mView.findViewById(R.id.edit_mail);
        final EditText contact_default = (EditText)mView.findViewById(R.id.edit_contact);
        Button btn_save = (Button)mView.findViewById(R.id.save_changes);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel_changes);

        name_default.setText(t_name.getText());
        mail_default.setText(t_mail.getText());
        contact_default.setText(t_contact.getText());

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Name").setValue(name_default.getText().toString());
                databaseReference.child("Contact").setValue(contact_default.getText().toString());
                databaseReference.child("Mail").setValue(mail_default.getText().toString());

                alertDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }
}
