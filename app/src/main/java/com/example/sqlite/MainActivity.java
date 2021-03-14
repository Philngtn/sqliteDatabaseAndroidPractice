package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Define global scope of elements in MainActivity
    TextView txtAvgCourse;
    FloatingActionButton ftBtnAddCourses;
    ListView lv_allCoursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define all elements via ID
        ftBtnAddCourses = findViewById(R.id.FloatButton);
        txtAvgCourse = findViewById(R.id.txtBoxTotalGPA);
        lv_allCoursesList = findViewById(R.id.lv_mainAllCourses);

        // Floating button click action
        ftBtnAddCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_showDialog(view);
            }
        });

        setUpListView();
        setUpOnclickListener();
        setUpAvgAllAssignment();

    }

    public void setUpAvgAllAssignment(){
        databaseHelper databaseHelper = new databaseHelper(MainActivity.this);
        double totalAvg = databaseHelper.getAvgOfAllAssignment();
        if (Double.isNaN(totalAvg))
            txtAvgCourse.setText("Average of All Assignments: NA" );
        else
            txtAvgCourse.setText("Average of All Assignments: " + String.format("%.2f",totalAvg));
    }

    public void setUpListView(){
        //Showing all available courses
        databaseHelper databaseHelper = new databaseHelper(MainActivity.this);
        List<courseModel> allCourses = databaseHelper.getAllCourses();
        courseListViewAdapter adapter = new courseListViewAdapter(this,0, allCourses);
        lv_allCoursesList.setAdapter(adapter);
        setUpAvgAllAssignment();

    }

    public void setUpOnclickListener(){

         lv_allCoursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 courseModel selectedCourse = (courseModel) lv_allCoursesList.getItemAtPosition(position);
//                 Toast.makeText(MainActivity.this, String.valueOf(selectedCourse.getCourseID()), Toast.LENGTH_SHORT).show();

                 Intent showDetail = new Intent(getApplicationContext(), assignmentActivity.class);
                 showDetail.putExtra("id", String.valueOf(selectedCourse.getCourseID()));
                 showDetail.putExtra("courseName", selectedCourse.getCourseName());
                 showDetail.putExtra("courseCode", selectedCourse.getCourseCode());

                 startActivity(showDetail);
             }
         });
    }

    public void btn_showDialog(View view){
        final AlertDialog.Builder addCourse = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.adding_information,null);

        final EditText courseName = (EditText)mView.findViewById(R.id.courseName);
        final EditText courseCode = (EditText)mView.findViewById(R.id.courseTitle);
        Button btn_save = (Button)mView.findViewById(R.id.saveBtn);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancelBtn);


        addCourse.setView(mView);

        final AlertDialog addCourseDialog = addCourse.create();
        addCourseDialog.setCanceledOnTouchOutside(false);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseDialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (courseName.getText().toString().isEmpty())
                        Snackbar.make(view, "Please Add Course Name", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    else if (courseCode.getText().toString().isEmpty())
                        Snackbar.make(view, "Please Add Course Code", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    else {
                        courseModel courseModel = new courseModel(-1, courseName.getText().toString().trim(), courseCode.getText().toString().trim());

                        databaseHelper databaseHelper = new databaseHelper(MainActivity.this);

                        boolean success = databaseHelper.addOneCourse(courseModel);

//                        Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                        addCourseDialog.dismiss();
                    }
                }catch (Exception e){
                    Snackbar.make(view, "Error", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                setUpListView();
            }
        });
        addCourseDialog.show();
    }
}