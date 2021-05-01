package com.example.fyp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fyp1.R;
import com.example.fyp1.Class.SubjectLectureClass;

import java.util.ArrayList;

public class SubjectDetailLectureAdapter extends ArrayAdapter<SubjectLectureClass> {

    private static final String TAG = "SubjectDetailLectureAdapter";

    private Context mContext;
    int mResource;

    public SubjectDetailLectureAdapter(Context context, int resource, ArrayList<SubjectLectureClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String Lecture = getItem(position).getLecture();
        String[] Class = getItem(position).getLectureClass().split("/");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lecture = (TextView)convertView.findViewById(R.id.register_subject_lecture_class);
        TextView time1 = (TextView) convertView.findViewById(R.id.register_subject_lecture_class_time1);
        TextView time2 = (TextView) convertView.findViewById(R.id.register_subject_lecture_class_time2);

        lecture.setText(Lecture);
        time1.setText(Class[0]);
        if(Class.length==1){
            time2.setText("");
        }else{
            time2.setText(Class[1]);
        }


        return convertView;
    }
}
