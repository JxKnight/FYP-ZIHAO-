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
import com.example.fyp1.Class.User;
import com.example.fyp1.R;

import java.util.ArrayList;

public class GroupFriendListAdapter extends ArrayAdapter<User> {

    private Context mContext;
    int mResource;

    public GroupFriendListAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String ID = getItem(position).getID();
        String Name = getItem(position).getName();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView id = (TextView) convertView.findViewById(R.id.group_friend_ID);
        TextView name = (TextView) convertView.findViewById(R.id.group_friend_NAME);

        id.setText(ID);
        name.setText(Name);


        return convertView;
    }
}
