package com.example.tvs.firebasedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Conversation> {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference storageReference;
    Context context;
    int resource;
    List<Conversation> objects;
    int posToDel;

    public ExpenseAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        posToDel = position;
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        Conversation conversation = objects.get(position);
        ((TextView) convertView.findViewById(R.id.messageDisp120)).setText(conversation.message);
        ((TextView) convertView.findViewById(R.id.senderName)).setText(conversation.userName);

        ((ImageView) convertView.findViewById(R.id.deleteMessage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return convertView;
    }
}
