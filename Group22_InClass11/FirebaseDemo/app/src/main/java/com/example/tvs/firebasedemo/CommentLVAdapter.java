package com.example.tvs.firebasedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CommentLVAdapter extends ArrayAdapter<Conversation.Comments> {

    int resource;
    Context context;
    List<Conversation.Comments> objects;


    public CommentLVAdapter(Context context, int resource, List<Conversation.Comments> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        Conversation.Comments comments = objects.get(position);

        ((TextView) convertView.findViewById(R.id.commentText420)).setText(comments.comment);
        ((TextView) convertView.findViewById(R.id.commentUserName420)).setText(comments.userName);
        ((TextView) convertView.findViewById(R.id.commentedTime567)).setText(new PrettyTime().format(comments.addedTime));

        return convertView;
    }
}