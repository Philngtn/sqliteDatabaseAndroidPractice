package com.example.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class assignmentListViewAdapter  extends ArrayAdapter<assignmentModel> {

    // Building constructor
    public assignmentListViewAdapter(@NonNull Context context, int resource, @NonNull List<assignmentModel> assignmentList) {
        super(context, resource, assignmentList);
    }



    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get a single item in a object list
        assignmentModel singleAssignments = getItem(position);

        // Setup the custom layout for single cell of the list view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.assignment_list_items, parent, false);
        }

        // Define the elements in the custom layout
        TextView tvAssignmentName = (TextView) convertView.findViewById(R.id.txtAssignmentName);
        TextView tvAssignmentGrade = (TextView) convertView.findViewById(R.id.txtAssignmentGrade);

        // Set value for elements in custom layout
        tvAssignmentGrade.setText(String.valueOf(singleAssignments.getGrade()) + " %");
        tvAssignmentName.setText(singleAssignments.getAssignmentTitle());

        // Return a adapter
        return convertView;
    }
}
