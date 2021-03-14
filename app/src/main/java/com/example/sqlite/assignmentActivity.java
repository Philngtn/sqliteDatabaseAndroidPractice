package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class assignmentActivity extends AppCompatActivity {


    // Declare elements for global scope ( It's important since if we declare in the onCreate,
    // Other function could not use the local scope -> crash
    TextView courseNameDisplay;
    TextView courseCodeDisplay;
    FloatingActionButton ftBtnAddAssignment;
    Button btnDeleteCourse;
    ListView lv_assignmentDisplay;
    int parsedStringID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        // Define elements by ID
        courseNameDisplay = (TextView) findViewById(R.id.courseName);
        courseCodeDisplay = (TextView) findViewById(R.id.courseCode);
        ftBtnAddAssignment = findViewById(R.id.FloatButton_addAssignment);
        btnDeleteCourse = findViewById(R.id.btnDelete);
        lv_assignmentDisplay = findViewById(R.id.lv_assignments);


        // Back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get data from the main list view by Intent
        getSelectedCourse();

        //floating button click
        ftBtnAddAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ShowDialog(v);
            }
        });

        // Course delete button
        btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper databaseHelper = new databaseHelper(assignmentActivity.this);
                databaseHelper.deleteCourse(parsedStringID);

                Intent moveToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(moveToMain);
            }
        });

        // Setup the list view adapter
        setUpListView();

    }

    public void setUpListView(){
        //Showing all available assignments in a course
        databaseHelper databaseHelper = new databaseHelper(assignmentActivity.this);
        List<assignmentModel> allAssignments = databaseHelper.getAllAssignmentInACourse(parsedStringID);

        // Setup list view
//        Toast.makeText(this, allAssignments.toString(), Toast.LENGTH_SHORT).show();
        assignmentListViewAdapter adapter = new assignmentListViewAdapter(this,0, allAssignments);
        lv_assignmentDisplay.setAdapter(adapter);

    }


    private void getSelectedCourse(){
        // Retrieve data via sending Intent
        Intent previousIntent = getIntent();
        // Get ID of the course
        parsedStringID = Integer.valueOf(previousIntent.getStringExtra("id"));
        String parsedStringCourseName = previousIntent.getStringExtra("courseName");
        String parsedStringCourseCode = previousIntent.getStringExtra("courseCode");

        // Set text displayed on top of activity
        courseNameDisplay.setText(parsedStringCourseName);
        courseCodeDisplay.setText(parsedStringCourseCode);
    }

    private void btn_ShowDialog(View v) {

        // Construct a dialog
        final AlertDialog.Builder addAssignment = new AlertDialog.Builder(assignmentActivity.this);
        // Get the layout of custom dialog
        View mView = getLayoutInflater().inflate(R.layout.adding_assignments,null);

        // Define elements in custom layout of dialog
        final EditText assignmentTitle = (EditText)mView.findViewById(R.id.assignment_title);
        final EditText assignmentGrade = (EditText)mView.findViewById(R.id.assignment_grade);
        Button btn_save = (Button)mView.findViewById(R.id.saveBtnAssignment);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancelBtnAssignment);

        // Set view of dialog
        addAssignment.setView(mView);

        // Create a dialog
        final AlertDialog addAssignmentDialog = addAssignment.create();

        // Not cancel touch out
        addAssignmentDialog.setCanceledOnTouchOutside(false);

        // Response CANCEL button
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssignmentDialog.dismiss();
            }
        });

        // Response SAVE button
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // FIX INPUTS
                    if (assignmentTitle.getText().toString().isEmpty())
                        Toast.makeText(assignmentActivity.this, "Please add the assignment name", Toast.LENGTH_SHORT).show();
                    else if (assignmentGrade.getText().toString().isEmpty())
                        Toast.makeText(assignmentActivity.this, "Please add the assignment grade", Toast.LENGTH_SHORT).show();
                    else {

                        if(Integer.parseInt(assignmentGrade.getText().toString()) > 100 || Integer.parseInt(assignmentGrade.getText().toString()) <0){
                            Toast.makeText(assignmentActivity.this, "Please re-enter the grade", Toast.LENGTH_SHORT).show();
                        } else {

                            // Construct a database input
                            assignmentModel assignmentModel = new assignmentModel(-1, parsedStringID, assignmentTitle.getText().toString().trim(), Integer.parseInt(assignmentGrade.getText().toString()));
//                          Toast.makeText(assignmentActivity.this,assignmentModel.toString() , Toast.LENGTH_SHORT).show();

                            // Add to the database
                            databaseHelper databaseHelper = new databaseHelper(assignmentActivity.this);
                            boolean success = databaseHelper.addOneAssignment(assignmentModel);
//                          Toast.makeText(assignmentActivity.this, "Add = " + success, Toast.LENGTH_SHORT).show();

                            // Turn off the Dialog after clicking save
                            addAssignmentDialog.dismiss();
                        }
                    }
                }catch (Exception e){
                         // Catch error
                         Toast.makeText(assignmentActivity.this, "Error" , Toast.LENGTH_SHORT).show();
                }
                // Refresh the listview
                setUpListView();
            }
        });

        // Show Dialog
        addAssignmentDialog.show();
    }
}