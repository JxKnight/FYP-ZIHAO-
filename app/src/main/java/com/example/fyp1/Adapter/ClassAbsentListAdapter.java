package com.example.fyp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fyp1.Class.User;
import com.example.fyp1.R;

import java.util.ArrayList;

public class ClassAbsentListAdapter extends ArrayAdapter<User> {

    private Context mContext;
    int mResource;

    public ClassAbsentListAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String ID = getItem(position).getID();
        String NAME = getItem(position).getName();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView IDNAME = (TextView) convertView.findViewById(R.id.absent_id_name);

        IDNAME.setText(ID+"-"+NAME);

        return convertView;
    }
}
