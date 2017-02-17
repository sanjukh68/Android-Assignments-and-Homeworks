package com.example.tvs.messaging;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConversationListViewAdapter extends ArrayAdapter<Message> {

    int resource;
    List<Message> objects;
    Context context;
    FirebaseAuth firebaseAuth;
    String conversationWith;

    public ConversationListViewAdapter(Context context, int resource, List<Message> objects, String conversationWith) {
        super(context, resource, objects);
        this.resource = resource;
        this.objects = objects;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.conversationWith = conversationWith;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, parent, false);
        }


        LinearLayout otherMsg = (LinearLayout) convertView.findViewById(R.id.othersMsg);
        LinearLayout myMsg = (LinearLayout) convertView.findViewById(R.id.myMsg);
        TextView messageContent1 = (TextView) convertView.findViewById(R.id.messageContent1);
        ImageView otherImage = (ImageView) convertView.findViewById(R.id.otherImage);
        ImageView myImage = (ImageView) convertView.findViewById(R.id.myImage);
        TextView messageContent2 = (TextView) convertView.findViewById(R.id.messageContent2);

        Message message = objects.get(position);

        Picasso.with(context).load(message.imageURL).into(otherImage);
        Picasso.with(context).load(message.imageURL).into(myImage);

        if((message.recipientUid.equals(conversationWith) && message.senderUid.equals(firebaseAuth.getCurrentUser().getUid())) ||
            message.senderUid.equals(conversationWith) && message.recipientUid.equals(firebaseAuth.getCurrentUser().getUid())) {
            if (message.senderUid.equals(firebaseAuth.getCurrentUser().getUid())) {
                otherImage.setVisibility(View.GONE);
                messageContent2.setText(message.message);
                otherMsg.setVisibility(View.INVISIBLE);
            } else {
                messageContent1.setText(message.message);
                messageContent2.setText(message.message);
                myImage.setVisibility(View.GONE);
                myMsg.setVisibility(View.INVISIBLE);
            }
        }
        else {
            myMsg.setVisibility(View.INVISIBLE);
            otherMsg.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
