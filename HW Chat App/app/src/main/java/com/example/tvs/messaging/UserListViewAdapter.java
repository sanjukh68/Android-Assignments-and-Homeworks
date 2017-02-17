package com.example.tvs.messaging;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserListViewAdapter extends ArrayAdapter<User> {

    int resource;
    List<User> objects;
    Context context;

    public UserListViewAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        User user = objects.get(position);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        if(user.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            userName.setText(user.fullName + " (My profile. Click to edit)");
        else
            userName.setText(user.fullName);

        return convertView;
    }
}
