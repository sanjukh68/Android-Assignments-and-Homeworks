package com.example.tvs.messaging;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ActivityUserList extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    public static FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private ImageView logout;
    private ListView messageRV;
    private Bitmap imageInBitmap;
    private String imageName;
    private ArrayList<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        messageRV = (ListView) findViewById(R.id.messageRV);
        logout = (ImageView) findViewById(R.id.logout);

        logout.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        messageRV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent getDetails = new Intent(getApplicationContext(), ActivityProfileEdit.class);
                getDetails.putExtra("UID", allUsers.get(position));
                getDetails.putExtra("POSITION", position);
                startActivity(getDetails);
                return true;
            }
        });

        messageRV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(allUsers.get(position).uid.equals(firebaseUser.getUid())) {
                    Intent getDetails = new Intent(getApplicationContext(), ActivityProfileEdit.class);
                    getDetails.putExtra("UID", allUsers.get(position));
                    getDetails.putExtra("POSITION", position);
                    startActivity(getDetails);
                } else {
                    Intent getDetails = new Intent(getApplicationContext(), ActivityConversation.class);
                    getDetails.putExtra("RECIPIENT", allUsers.get(position).uid);
                    startActivity(getDetails);
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            User currentUser;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<User>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<User>>() {};
                allUsers = dataSnapshot.child("USERS").getValue(genericTypeIndicator);
                UserListViewAdapter userListViewAdapter = new UserListViewAdapter(getApplicationContext(), R.layout.users_list_view, allUsers);
                messageRV.setAdapter(userListViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void logout() {
        firebaseAuth.signOut();
        startActivity(new Intent(this, ActivityLogin.class));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout:       logout();
                break;
            case R.id.send:         String message = ((TextView) findViewById(R.id.messageField)).getText().toString().trim();
                /*if(message == null || message == "" || message.length() == 0) {
                    Toast.makeText(this, "No message!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(allConversations == null || allConversations.size() == 0) {
                    ArrayList<Conversation> conversations = new ArrayList<>();
                    conversations.add(new Conversation(firebaseUser.getUid(), message, userNameFromUid, new Conversation.Comments(), null, new Date()));
                    dbr.child("CONVERSATION").setValue(conversations);
                } else {
                    allConversations.add(new Conversation(firebaseUser.getUid(), message, userNameFromUid, new Conversation.Comments(), null, new Date()));
                    dbr.child("CONVERSATION").setValue(allConversations);
                }*/
                break;
            case R.id.attachImage:  Intent openGallery = new Intent();
                openGallery.setType("image/*");
                openGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(openGallery, 200);
                break;
        }
    }

    private void uploadImage() {
        StorageReference userImageFolder = storage.getReference().child(firebaseUser.getUid()).child("images/");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageInBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imageRef = userImageFolder.child(imageName);
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 200:   if(resultCode== Activity.RESULT_OK) {
                try {
                    imageInBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    imageName = (new File(String.valueOf(Uri.parse(data.getDataString())))).getName();
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(resultCode!=0) Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
