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
import com.example.fyp1.Class.Group;
import com.example.fyp1.R;

import java.util.ArrayList;

public class GroupListAdapter extends ArrayAdapter<Group> {

    private Context mContext;
    int mResource;

    public GroupListAdapter(Context context, int resource, ArrayList<Group> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String groupName = getItem(position).getName();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.group_ID);

        name.setText("  "+groupName);

        return convertView;
    }
}
