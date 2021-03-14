package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class databaseHelper extends SQLiteOpenHelper {

    // Define the constant for easy use in query command
    public static final String COURSE_TABLE = "COURSE_TABLE";
    public static final String COLUMN_COURSE_ID = "COURSE_ID";
    public static final String COLUMN_COURSE_NAME = "COURSE_NAME";
    public static final String COLUMN_COURSE_CODE = "COURSE_CODE";

    public static final String ASSIGNMENT_TABLE = "ASSIGNMENT_TABLE";
    public static final String COLUMN_ASSIGNMENT_ID = "ASSIGNMENT_ID";
    public static final String COLUMN_ASSTITLE = "ASSTITLE";
    public static final String COLUMN_GRADE = "GRADE";
    public static final String COLUMN_ASSIGN_COURSE_ID = COLUMN_COURSE_ID;


    public databaseHelper(@Nullable Context context) {
        super(context, "courses.db", null, 1);
    }


    // Create a new db or open an exist one
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableCourses = "CREATE TABLE " + COURSE_TABLE + " (" + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_COURSE_NAME + " TEXT, " + COLUMN_COURSE_CODE + " TEXT)";
        String createTableAssignments = "CREATE TABLE " + ASSIGNMENT_TABLE + " (" + COLUMN_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ASSIGN_COURSE_ID + " INT, " + COLUMN_ASSTITLE + " TEXT, " + COLUMN_GRADE + " INT)";
        db.execSQL(createTableCourses);
        db.execSQL(createTableAssignments);
    }

    // this is called if the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // Explain how the db works
    // Define the return value of method
    public double getAvgOfAllAssignment(){
        double totalAvg = 0;
        double count = 0;

        // Define the queryString for raw SQLquery
        String queryString = "SELECT * FROM " + ASSIGNMENT_TABLE;

        // Select the readable db
        SQLiteDatabase db = this.getReadableDatabase();
        // Query the db return a cursor ( like pointer in C++)
        Cursor cursor = db.rawQuery(queryString,null);

        // Move the cursor to the first elements of query list
        if(cursor.moveToFirst()){
            do{
                int assignmentGrade = cursor.getInt(3);
                totalAvg = totalAvg + assignmentGrade;
                count = count + 1;
             // move to the next element of a query list
            }while(cursor.moveToNext());
        }else{
            // Do nothing if we do not find anything from the query command
        }

        // close both the cursor and the db when done
        cursor.close();
        db.close();
        // return value
        return  totalAvg/count;
    }

    public double getAvgAssignmentOfaSingleCourse(int courseID){

        double avg = 0;
        double count = 0;

        String queryString = "SELECT * FROM " + ASSIGNMENT_TABLE + " WHERE " + COLUMN_ASSIGN_COURSE_ID + " = " + courseID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                int assignmentGrade = cursor.getInt(3);
                avg = avg + assignmentGrade;
                count = count + 1;
            }while(cursor.moveToNext());
        }else{

        }
        // close both the cursor and the db when done
        cursor.close();
        db.close();

        return  avg/count;

    }


    public boolean addOneAssignment(assignmentModel assignmentModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ASSTITLE, assignmentModel.getAssignmentTitle());
        cv.put(COLUMN_ASSIGN_COURSE_ID, assignmentModel.getCourseID());
        cv.put(COLUMN_GRADE, assignmentModel.getGrade());

        long insert = db.insert(ASSIGNMENT_TABLE, null, cv);

        if (insert == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<assignmentModel> getAllAssignmentInACourse(int courseID){
        List<assignmentModel> returnAllAssignments =  new ArrayList<>();

        String queryString = "SELECT * FROM " + ASSIGNMENT_TABLE + " WHERE " + COLUMN_ASSIGN_COURSE_ID + " = " + courseID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                int assignmentID = cursor.getInt(0);
                int assignmentCourseID = cursor.getInt(1);
                String assignmentTitle = cursor.getString(2);
                int assignmentGrade = cursor.getInt(3);

                assignmentModel foundAssignment = new assignmentModel(assignmentID, assignmentCourseID, assignmentTitle, assignmentGrade);
                returnAllAssignments.add(foundAssignment);

            }while(cursor.moveToNext());
        }else{

        }
        // close both the cursor and the db when done
        cursor.close();
        db.close();

        return  returnAllAssignments;
    }

    public boolean addOneCourse(courseModel courseModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_COURSE_NAME, courseModel.getCourseName());
        cv.put(COLUMN_COURSE_CODE, courseModel.getCourseCode());

        long insert = db.insert(COURSE_TABLE, null, cv);

        if (insert == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<courseModel> getAllCourses(){

        List<courseModel> returnAllCourses = new ArrayList<>();

        String queryString = "SELECT * FROM " + COURSE_TABLE;

        // Better to chose READable since it not block other actions, Writeable will block other actions
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            // loop through the cursor (result set) and create a new customer object. Put them into the return list.
            do {
              int courseID = cursor.getInt(0);
              String courseName = cursor.getString(1);
              String courseCode = cursor.getString(2);
              // if we have a boolean
              // boolean courseAvailable = cursor.getInt(3) == 1 ? true : false

              courseModel foundCourse = new courseModel(courseID,courseName,courseCode);
              returnAllCourses.add(foundCourse);


            } while (cursor.moveToNext());
        }else {
            // Failure. do not add anything to the list
        }

        // close both the cursor and the db when done
        cursor.close();
        db.close();

        return returnAllCourses;

    };

    public boolean deleteCourse(int courseID){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + COURSE_TABLE + " WHERE " + COLUMN_COURSE_ID + " = " + courseID;

        Cursor cursor = db.rawQuery(queryString, null);
        deleteAssignmentWithCourse(courseID);

        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }

    public boolean deleteAssignmentWithCourse(int courseID){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ASSIGNMENT_TABLE + " WHERE " + COLUMN_COURSE_ID + " = " + courseID;

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }
}
