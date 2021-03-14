package com.example.sqlite;

public class courseModel {

    // Building a model for an course table in local database
    private int courseID;
    private String courseName;
    private String courseCode;


    public courseModel(int courseID, String courseName, String courseCode) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "courseModel{" +
                "courseID=" + courseID +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                '}';
    }
}
