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
import com.example.fyp1.Class.SubjectTutorialClass;

import java.util.ArrayList;

public class SubjectDetailTutorialAdapter extends ArrayAdapter<SubjectTutorialClass> {

    private static final String TAG = "SubjectDetailLectureAdapter";

    private Context mContext;
    int mResource;

    public SubjectDetailTutorialAdapter(Context context, int resource, ArrayList<SubjectTutorialClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String tutorialLecture = getItem(position).getTutorial();
        String tutorialTime = getItem(position).getTime();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lecture = (TextView) convertView.findViewById(R.id.register_subject_tutorial_class);
        TextView time = (TextView) convertView.findViewById(R.id.register_subject_class_tutorial_time);

        lecture.setText(tutorialLecture);
        time.setText(tutorialTime);

        return convertView;
    }
}
