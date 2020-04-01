package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
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

public class Mcq_upload extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView filename;
    Button fopen,fsubmit;
    String fpath;
    RadioGroup upload_question_options;
    RadioButton rb_mcq,rb_question;

    Spinner spinner_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_upload);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }

        fopen = (Button) findViewById(R.id.btn_fileopen);
        filename = (TextView)findViewById(R.id.filename);
        fsubmit = findViewById(R.id.btn_filesubmit);
        upload_question_options = findViewById(R.id.question_upload_type);
        rb_mcq = findViewById(R.id.rb_mcq);
        rb_question = findViewById(R.id.rb_question);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Questions");
        spinner_subject = findViewById(R.id.spinner_subject);

        fopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(Mcq_upload.this)
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.xlsx$"))
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });

        fsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean ismcq = false;
                if(upload_question_options.getCheckedRadioButtonId() == rb_mcq.getId())
                {
                    databaseReference = firebaseDatabase.getReference("Questions").child("MCQ").child(spinner_subject.getSelectedItem().toString());
                    ismcq=true;
                }
                else
                {
                    databaseReference = firebaseDatabase.getReference("Questions").child("Theory_questions").child(spinner_subject.getSelectedItem().toString());

                }

                readExcel(fpath,ismcq);
                Toast.makeText(Mcq_upload.this, "File Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            filename.setText(filePath);
            fpath = filePath;

        }
    }


    private void readExcel(String filepath,Boolean ismcq)
    {

        File inputFile = new File(filepath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();
            if(ismcq)
            {
//                Log.i("Number of rows",rowsCount+"");
                //outter loop, loops through rows
                for (int r = 1; r < rowsCount; r++) {
                    Row row = sheet.getRow(r);
//                Log.i("Subject : ",getCellAsString(row, 7, formulaEvaluator)+"");
//                Log.i("Result of row",getCellAsString(row, 7, formulaEvaluator).equals(spinner_subject.getSelectedItem().toString().toLowerCase())+"");
                    if(getCellAsString(row, 6, formulaEvaluator).toLowerCase().equals(spinner_subject.getSelectedItem().toString().toLowerCase()))
                    {
                        String id = databaseReference.push().getKey();
                        MCQquestion questions = new MCQquestion();
                        questions.setId(id);
                        int cellsCount = row.getPhysicalNumberOfCells();
                        Log.i("Number of Cellls",cellsCount+"");
//                inner loop, loops through columns
                        for (int c = 0; c < cellsCount; c++) {
//                    handles if there are to many columns on the excel sheet.
                            String value = getCellAsString(row, c, formulaEvaluator);

                            if(c==0)
                            {
                                questions.setQuestion(value);
                            }
                            if(c==1)
                            {
                                questions.setOp1(value);
                            }
                            if(c==2)
                            {
                                questions.setOp2(value);
                            }
                            if(c==3)
                            {
                                questions.setOp3(value);
                            }
                            if(c==4)
                            {
                                questions.setOp4(value);
                            }
                            if(c==5)
                            {
                                questions.setCorrectanswer(value);
                            }

                        }
                        databaseReference.child(id).setValue(questions);
                    }
                }
            }
            else
            {
//                Log.i("Number of rows",rowsCount+"");
                //outter loop, loops through rows
                for (int r = 1; r < rowsCount; r++) {
                    Row row = sheet.getRow(r);
//                Log.i("Subject : ",getCellAsString(row, 7, formulaEvaluator)+"");
//                Log.i("Result of row",getCellAsString(row, 7, formulaEvaluator).equals(spinner_subject.getSelectedItem().toString().toLowerCase())+"");
                    if(getCellAsString(row, 3, formulaEvaluator).toLowerCase().equals(spinner_subject.getSelectedItem().toString().toLowerCase()))
                    {
                        String id = databaseReference.push().getKey();
                        Theoryquestion questions = new Theoryquestion();
                        questions.setId(id);
                        int cellsCount = row.getPhysicalNumberOfCells();
                        Log.i("Number of Cellls",cellsCount+"");
//                inner loop, loops through columns
                        for (int c = 0; c < cellsCount; c++) {
//                    handles if there are to many columns on the excel sheet.
                            String value = getCellAsString(row, c, formulaEvaluator);

                            if(c==0)
                            {
                                questions.setQuestion(value);
                            }
                            if(c==1)
                            {
                                questions.setWeightage(value);
                            }
                            if(c==2)
                            {
                                questions.setChapter(value);
                            }
                            if(c==3)
                            {
                                questions.setSubject(value);
                            }


                        }
                        databaseReference.child(id).setValue(questions);
                    }
                }
            }
//

        }




        catch (FileNotFoundException e) {
            Log.e("Error", "readExcelData: FileNotFoundException. " + e.getMessage() );}
        catch (IOException e) {
            Log.e("Error", "readExcelData: Error reading inputstream. " + e.getMessage() );
        }

    }




    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e("Error", "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }
}

