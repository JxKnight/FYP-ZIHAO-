package com.example.fyp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fyp1.Class.SubjectLectureClass;
import com.example.fyp1.R;

import java.util.ArrayList;

public class LecturerSubjectDetailLectureAdapter extends ArrayAdapter<String> {

    private static final String TAG = "LecturerSubjectDetailLectureAdapter";

    private Context mContext;
    int mResource;

    public LecturerSubjectDetailLectureAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String Lecture = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lecture = (TextView)convertView.findViewById(R.id.lecturer_subject_lecture_class);

        lecture.setText(Lecture);


        return convertView;
    }
}
