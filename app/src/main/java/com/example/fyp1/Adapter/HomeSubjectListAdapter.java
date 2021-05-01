package com.example.fyp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fyp1.Class.Subject;
import com.example.fyp1.R;

import java.util.ArrayList;

public class HomeSubjectListAdapter extends ArrayAdapter<Subject> {

    private static final String TAG = "LecturerSubjectAdapter";

    private Context mContext;
    int mResource;

    public HomeSubjectListAdapter(Context context, int resource, ArrayList<Subject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id = getItem(position).getSubjectCode();
        String name = getItem(position).getSubjectName();
        String creditHour = getItem(position).getSubjectCreditHours();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView subjectCodeName = (TextView) convertView.findViewById(R.id.lecturer_subject_list_code);
        TextView subjectCreditHours = (TextView) convertView.findViewById(R.id.lecturer_subject_list_credit_hours);

        //productsID.append(productID);
        subjectCodeName.setText(id+" - "+name);
        subjectCreditHours.setText(creditHour);

        return convertView;
    }
}
