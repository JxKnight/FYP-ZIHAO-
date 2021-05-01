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

public class GroupChatListAdapter extends ArrayAdapter<ChatDetails> {

    private Context mContext;
    int mResource;

    public GroupChatListAdapter(Context context, int resource, ArrayList<ChatDetails> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String Details = getItem(position).getChatDetails();
        String Time = getItem(position).getChatTime();
        String Receiver = getItem(position).getReceiver();
        String Sender = getItem(position).getSender();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView receiver = (TextView) convertView.findViewById(R.id.chat_receiver);
        TextView sender = (TextView) convertView.findViewById(R.id.chat_sender);
        TextView senderID = (TextView) convertView.findViewById(R.id.chat_sender_name);
        TextView receiverID = (TextView) convertView.findViewById(R.id.chat_receiver_name);

        if(Receiver==null){
            sender.setText(Details);
            senderID.setText(Sender);
        }else{
            receiver.setText(Details);
            receiverID.setText(Receiver);
        }
        return convertView;
    }
}
