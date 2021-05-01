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

public class SubjectListHomeAdapter extends ArrayAdapter<Subject> {

   // private static final String TAG = "AdminRoleAdapter";

    private Context mContext;
    int mResource;

    public SubjectListHomeAdapter(Context context, int resource, ArrayList<Subject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // String productID = getItem(position).getProductId();
        String id = getItem(position).getSubjectCode();
        String name = getItem(position).getSubjectName();
        String creditHour = getItem(position).getSubjectCreditHours();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView subjectCodeName = (TextView) convertView.findViewById(R.id.subjectlist_home_course);
        TextView subjectCreditHours = (TextView) convertView.findViewById(R.id.subjectlist_home_attendancepercentage);

        //productsID.append(productID);
        subjectCodeName.setText(id+" - "+name);
        subjectCreditHours.append(creditHour+"%");

        return convertView;
    }
}
