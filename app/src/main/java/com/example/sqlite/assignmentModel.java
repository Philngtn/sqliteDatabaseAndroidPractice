package com.example.sqlite;

public class assignmentModel {


    // Building a model for an assignment table in local database
    private int assignmentID;
    private int courseID;
    private String assignmentTitle;
    private int grade;

    public assignmentModel(int assignmentID, int courseID, String assignmentTitle, int grade) {
        this.assignmentID = assignmentID;
        this.courseID = courseID;
        this.assignmentTitle = assignmentTitle;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "assignmentModel{" +
                "assignmentID=" + assignmentID +
                ", courseID=" + courseID +
                ", assignmentTitle='" + assignmentTitle + '\'' +
                ", grade=" + grade +
                '}';
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
