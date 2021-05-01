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
import com.example.fyp1.Class.StudyMaterial;
import com.example.fyp1.R;

import java.util.ArrayList;

public class StudyMaterialListAdapter extends ArrayAdapter<StudyMaterial> {

    private Context mContext;
    int mResource;

    public StudyMaterialListAdapter(Context context, int resource, ArrayList<StudyMaterial> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String CreatedDate = getItem(position).getCreatedDate();
        String Name = getItem(position).getName();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.study_material_list_name);
        name.setText(Name);
        return convertView;
    }
}
