package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class student_dashboard extends AppCompatActivity {

    CardView cv_student_profile,cv_mcq_practice,cv_student_result,cv_paper_generater;
    final Context context=this;
    ArrayList<String> twomarkquestions = new ArrayList<>();
    ArrayList<String> threemarkquestions = new ArrayList<>();
    ArrayList<String> fivemarkquestions = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int number_paper=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);


        cv_student_profile = (CardView)findViewById(R.id.cv_student_profile);
        cv_mcq_practice = (CardView)findViewById(R.id.cv_mcq_practice);
        cv_student_result = (CardView)findViewById(R.id.cv_student_result);
        cv_paper_generater = findViewById(R.id.cv_paper_generater_s);


//        btn_logout = findViewById(R.id.btn_logout);

        cv_student_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Student_profile.class);
                startActivity(intent);
            }
        });

        cv_student_result.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StudentResult.class);
                intent.putExtra("from","StudentResult");
                startActivity(intent);
            }
        });


        cv_paper_generater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
                }

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pdf_generate);
                dialog.setTitle("Title...");
                final Spinner sn_question_generate = dialog.findViewById(R.id.sn_question_subject);
                Button btn_paper_generate = dialog.findViewById(R.id.btn_paper_generate);



                btn_paper_generate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Subject from spinner : ",sn_question_generate.getSelectedItem().toString());
                        renderData(sn_question_generate.getSelectedItem().toString());
                        Log.i("Size of two mark array",""+twomarkquestions.size());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });



        // It is for dialog box of quiz subject and mark inputs
        cv_mcq_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_mcq_practice);
                dialog.setTitle("Title...");
                final Spinner sn_mcq_subject = dialog.findViewById(R.id.sn_mcq_subject);
                final EditText et_number_of_mcq_question = dialog.findViewById(R.id.et_number_of_mcq_question);
                Button btn_quiz_start = dialog.findViewById(R.id.btn_quiz_start);

                btn_quiz_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(et_number_of_mcq_question.getText().toString()))
                        {
                            et_number_of_mcq_question.setError("Enter No of MCQquestion");
                        }
                        else
                        {
                            int no_q = Integer.parseInt(et_number_of_mcq_question.getText().toString());

                            if(no_q >= 10)
                            {
                                Intent i = new Intent(context,Mcq_Quiz.class);
                                i.putExtra("Subject_Name",sn_mcq_subject.getSelectedItem().toString());
                                i.putExtra("No_of_questions",no_q);
                                dialog.dismiss();
                                startActivity(i);
                            }

                            else
                            {
                                Toast.makeText(context, "Enter no greater than 10", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
                dialog.show();

            }
        });

    }


    private void renderData(String sub)
    {
        Log.i("R","Enterd render data");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Questions").child("Theory_questions").child(sub);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("RE","Enterd render data");


                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    Theoryquestion theoryquestion = d.getValue(Theoryquestion.class);
                    Log.i("two : ","question + "+theoryquestion.getSubject());

                    if(theoryquestion.getWeightage().equals("2.0"))
                    {
                        Log.i("two : ","question");
                        twomarkquestions.add(theoryquestion.getQuestion());
                    }
                    else if(theoryquestion.getWeightage().equals("3.0"))
                    {
                        threemarkquestions.add(theoryquestion.getQuestion());
                    }
                    else
                    {
                        fivemarkquestions.add(theoryquestion.getQuestion());
                        Log.i("five : ","question");
                    }

                }
//                rendered_data_flag=true;
                createpdf();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void createpdf(){


        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        //paint.setColor(Color.RED);
        //canvas.drawCircle(50, 50, 30, paint);
        //paint.setColor(Color.BLACK);
        int  y = 50;
        canvas.drawText("Section A",10,y,paint);
        y += 20;
        for(int i = 0 ; i< 10 ;i++){
            String string = twomarkquestions.get(i);
            canvas.drawText(string, 10, y, paint);
            y += 20;
        }
        y += 20;

        canvas.drawText("Section B",10,y,paint);
        y += 20;
        for(int i = 0 ; i< 8 ;i++){
            String string = threemarkquestions.get(i);
            canvas.drawText(string, 10, y, paint);
            y += 20;
        }
        y += 20;
//
//        int z = y + 20;
//        textview.setText("sajfgsfl sdulfvf");
//        textview.setX(10);
//        textview.setY(z);
//        canvas.drawText("Hekfsf ehjfs v eblgblejgb evgebegrmnegbergbejg grjerg", 10, 50, paint);
//        canvas.drawText("skhg sfkndbdlkbdb kndbdb dbnnb ", 10, 70, paint);
//        canvas.drawText("sjkfsbf sbbbvhv;v jvfyff ytfyf8ifv f6 we", 10, 90, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(100, 100, 100, paint);

        y =50;
        canvas.drawText("Section C",10,y,paint);
        y += 20;
        for(int i = 0 ; i< 5 ;i++){
            String string = fivemarkquestions.get(i);
            canvas.drawText(string, 10, y, paint);
            y += 20;
        }
        y += 20;

//        canvas.drawText("Section D",10,y,paint);
//        y += 20;
////        for(int i = 0 ; i< 10 ;i++){
////            String string = arrayList.get(i).toString();
////            canvas.drawText(string, 10, y, paint);
////            y += 20;
////        }
//        y += 20;


//        rendered_data_flag=false;
        twomarkquestions.clear();
        threemarkquestions.clear();
        fivemarkquestions.clear();




        /*String s1 = "xgboost-1.0.2-py3-none-win_amd64.whl is not a supported wheel on this platform\nxgboost-1.0.2-py3-none-win_amd64.whl is not a supported wheel on this platform";
        String lines[] = s1.split("\n");
        for(int i = 0 ; i< lines.length ;i++){
            String string = lines[i];
            canvas.drawText(string, 10, y, paint);
            y += 20;
        }*/

        /*String s2 = "Oh when the blues go marching in Oh when the blues go marching in Oh when the blues go marching in Oh when the blues go marching in Oh when the blues go marching in Oh when the blues go marching in";

        StringBuilder sb = new StringBuilder(s2);
        int ii = 0;
        while ((ii = sb.indexOf(" ", ii + 65)) != -1) {
            sb.replace(ii, ii + 1, "\n");
        }
        String newString = sb.toString();
        String a[] = newString.split("\n");
        y += 20;
        for(int i = 0 ; i< a.length ;i++){
            String string = a[i];
            canvas.drawText(string, 10, y, paint);
            y += 20;
        }*/


        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Download_Paper/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-"+number_paper+".pdf";
        number_paper+=1;
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout)
        {
            SharedPreferences preferences = getSharedPreferences("com.example.app3_login_status",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login_status","off");
            editor.commit();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();


        }
        return super.onOptionsItemSelected(item);
    }
}
