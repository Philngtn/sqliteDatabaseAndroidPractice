package com.example.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class courseListViewAdapter extends ArrayAdapter<courseModel> {

    // Building constructor
    public courseListViewAdapter(@NonNull Context context, int resource, @NonNull List<courseModel> courseList) {
        super(context, resource, courseList);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get a single item in a object list
        courseModel singleCourses = getItem(position);

        // Setup the custom layout for single cell of the list view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_list_items, parent, false);
        }

        // Define the elements in the custom layout
        TextView tvCourseName = (TextView) convertView.findViewById(R.id.txtCourse);
        TextView tvCourseCode = (TextView) convertView.findViewById(R.id.txtCode);
        TextView tvAvgAssignment = (TextView) convertView.findViewById(R.id.txtAvgAssignment);

        // Get avg grade of a single course based on its assignment grades
        databaseHelper databaseHelper = new databaseHelper(getContext());
        double avgAssignment = databaseHelper.getAvgAssignmentOfaSingleCourse(singleCourses.getCourseID());

        // Set value for elements in custom layout
        tvCourseName.setText(singleCourses.getCourseName());
        tvCourseCode.setText(singleCourses.getCourseCode());

        // For the empty-assignment course
        if (Double.isNaN(avgAssignment))
            tvAvgAssignment.setText("Assignment Average: NA");
        else
            tvAvgAssignment.setText("Assignment Average: " +  String.format("%.2f",avgAssignment));


        return convertView;
    }
}
