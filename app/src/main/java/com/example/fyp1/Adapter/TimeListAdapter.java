package com.example.fyp1.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fyp1.R;

import java.util.ArrayList;

public class TimeListAdapter extends ArrayAdapter<String> {

    private static final String TAG = "AdminRoleAdapter";
    private Context mContext;
    int mResource;

    public TimeListAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // String productID = getItem(position).getProductId();
        String time = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView productIDTV = convertView.findViewById(R.id.limited_time);
        productIDTV.setText(time);
        return convertView;
    }
}
