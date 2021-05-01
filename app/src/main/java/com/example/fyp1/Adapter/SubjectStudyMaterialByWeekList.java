package com.example.fyp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fyp1.Class.ChatDetails;
import com.example.fyp1.R;

import java.util.ArrayList;

public class SubjectStudyMaterialByWeekList extends ArrayAdapter<String> {

    private Context mContext;
    int mResource;

    public SubjectStudyMaterialByWeekList(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String week = getItem(position);


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView Week = (TextView) convertView.findViewById(R.id.subject_material_weeks);

        Week.setText(week);
        return convertView;
    }
}
