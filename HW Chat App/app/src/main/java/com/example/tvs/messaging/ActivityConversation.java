package com.example.tvs.messaging;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ActivityConversation extends AppCompatActivity implements View.OnClickListener{

    private  FirebaseAuth firebaseAuth;
    private  DatabaseReference firebaseDatabase;
    private ListView conversationLV;
    private ImageView sendImg, attachImage;
    private static ArrayList<Message> messages;
    private String recipient;
    private static String IMAGE_URL = null;
    private static Bitmap imageInBitmap = null;
    private FirebaseStorage storage;
    private static  String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        messages =  new ArrayList<>();

        recipient = (String) getIntent().getExtras().get("RECIPIENT");

        conversationLV = (ListView) findViewById(R.id.conversationLV);
        sendImg = (ImageView) findViewById(R.id.send);
        attachImage = (ImageView) findViewById(R.id.attachImage);

        storage = FirebaseStorage.getInstance();

        sendImg.setOnClickListener(this);
        attachImage.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("MESSAGES");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Message>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Message>>() {};
                messages = dataSnapshot.getValue(genericTypeIndicator);
                if(messages != null && messages.size()!=0) {

                    ConversationListViewAdapter conversationListViewAdapter = new ConversationListViewAdapter(getApplicationContext(), R.layout.conversation_list_view, messages, recipient);
                    conversationLV.setAdapter(conversationListViewAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                if(imageInBitmap!=null)
                    uploadImage();
                else {
                    String message = ((TextView) findViewById(R.id.messageField)).getText().toString().trim();
                    if (message == null || message == "" || message.length() == 0) {
                        Toast.makeText(this, "No message!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Message conversation = new Message(firebaseAuth.getCurrentUser().getUid(),
                            recipient, message, new Date(), IMAGE_URL);
                    if (messages == null)
                        messages = new ArrayList<>();
                    messages.add(conversation);
                    firebaseDatabase.setValue(messages);

                }
                break;
            case R.id.attachImage:
            case R.id.addProfilePic:
                Log.d("DD", "dd");
                Intent openGallery = new Intent();
                openGallery.setType("image/*");
                openGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(openGallery, 240);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 240:
                    if(resultCode== Activity.RESULT_OK) {
                        try {
                            imageInBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                            imageName = (new File(String.valueOf(Uri.parse(data.getDataString())))).getName();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(resultCode!=0) Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void uploadImage() {
        StorageReference userImageFolder = storage.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("images/");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageInBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imageRef = userImageFolder.child(imageName);
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                IMAGE_URL = taskSnapshot.getDownloadUrl().toString();
                String message = ((TextView) findViewById(R.id.messageField)).getText().toString().trim();
                if(message == null || message == "" || message.length() == 0) {
                    Toast.makeText(getApplicationContext(), "No message!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message conversation = new Message(firebaseAuth.getCurrentUser().getUid(),
                        recipient, message, new Date(), taskSnapshot.getDownloadUrl().toString());
                if(messages == null)
                    messages = new ArrayList<>();
                messages.add(conversation);
                firebaseDatabase.setValue(messages);
                imageInBitmap = null;
                IMAGE_URL = null;
            }
        });
    }
}
