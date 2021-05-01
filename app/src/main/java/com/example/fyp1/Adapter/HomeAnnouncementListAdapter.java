package com.example.fyp1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fyp1.Class.Announcement;
import com.example.fyp1.Class.Announcement;
import com.example.fyp1.Class.Subject;
import com.example.fyp1.R;

import java.util.ArrayList;

public class HomeAnnouncementListAdapter extends ArrayAdapter<Announcement> {

    private Context mContext;
    int mResource;

    public HomeAnnouncementListAdapter(Context context, int resource, ArrayList<Announcement> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String Title = getItem(position).getTitle();
        String Details = getItem(position).getDetails();
        String SCode = getItem(position).getSubjectName();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView ATitle = (TextView) convertView.findViewById(R.id.home_Annoucement_title);
        TextView ADetails = (TextView) convertView.findViewById(R.id.home_Annoucement_details);
        TextView ASCode = (TextView) convertView.findViewById(R.id.home_Annoucement_SubjectCode);
        //productsID.append(productID);
        ATitle.setText(Title);
        ADetails.append(Details);
        ASCode.setText(SCode);

        return convertView;
    }
}
