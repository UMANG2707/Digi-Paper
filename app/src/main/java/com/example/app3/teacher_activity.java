package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
public class teacher_activity extends AppCompatActivity {

    CardView cv_teacher_profile,cv_teacher_result, cv_question_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_activity);
        cv_teacher_profile = (CardView) findViewById(R.id.cv_teacher_profile);
        cv_question_upload = findViewById(R.id.cv_question_upload);
        cv_teacher_result = findViewById(R.id.cv_teacher_result);

//        btn_logout = findViewById(R.id.btn_logout);

        cv_teacher_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), teacher_profile.class);
                startActivity(intent);
            }
        });

        cv_teacher_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getApplicationContext(), TeacherResult.class);
                startActivity(new Intent(getApplicationContext(), TeacherResult.class));
            }
        });
        cv_question_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Mcq_upload.class));
            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences preferences = getSharedPreferences("com.example.app3_login_status", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login_status", "off");
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();


        }
        return super.onOptionsItemSelected(item);
    }

}





