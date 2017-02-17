package com.example.tvs.firebasedemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewAdapter.ClickHandler {

    private FirebaseAuth firebaseAuth;
    public static FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private TextView userName;
    private ImageView logoutBtn, sendImg, attachImage;
    private ListView messageListView;
    private RecyclerView messageRV;
    private DatabaseReference dbr;
    private int positionToComment;
    private String userNameFromUid;
    private Bitmap imageInBitmap;
    private String imageName;
    private ArrayList<Conversation> allConversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        messageRV = (RecyclerView) findViewById(R.id.messageRV);
        userName = (TextView) findViewById(R.id.userName);
        sendImg = (ImageView) findViewById(R.id.send);
        attachImage = (ImageView) findViewById(R.id.attachImage);
        logoutBtn = (ImageView) findViewById(R.id.logout);

        sendImg.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        attachImage.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messageRV.setLayoutManager(lm);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference().child(firebaseUser.getUid());

        dbr = FirebaseDatabase.getInstance()
                .getReference();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Conversation>> gti = new GenericTypeIndicator<ArrayList<Conversation>>() {};
                allConversations = dataSnapshot.child("CONVERSATION").getValue(gti);
                RecyclerViewAdapter rva;
                if(allConversations != null && allConversations.size() != 0)
                    rva = new RecyclerViewAdapter(getApplicationContext(), R.layout.expense_list, allConversations, HomeActivity.this);
                else
                    rva = new RecyclerViewAdapter(getApplicationContext(), R.layout.expense_list, new ArrayList<Conversation>(), HomeActivity.this);
                messageRV.setAdapter(rva);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNameFromUid = dataSnapshot.child("DISP_NAME").getValue(String.class);
                userName.setText(userNameFromUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void logout() {
        firebaseAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout:       logout();
                                    break;
            case R.id.send:         String message = ((TextView) findViewById(R.id.messageField)).getText().toString().trim();
                                    if(message == null || message == "" || message.length() == 0) {
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
                                    }
                                    break;
            case R.id.attachImage:  Intent openGallery = new Intent();
                                    openGallery.setType("image/*");
                                    openGallery.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(openGallery, 200);
                                    break;
//            case R.id.imageViewAdd:
//                    download();
//                startActivity(new Intent(this, AddExpenseActivity.class));
//                                    break;
        }
    }

    private void download() {
        StorageReference storageReference = storage.getReferenceFromUrl("gs://testdemo-8ef7f.appspot.com")
                .child(firebaseUser.getUid())
                .child("images/");

        long data = 1024 * 1024;
        StorageReference imgRef = storageReference.child("image%3A19");
        imgRef.getBytes(data).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        });

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
                allConversations.add(new Conversation(firebaseUser.getUid(), null, userNameFromUid, new Conversation.Comments(), taskSnapshot.getDownloadUrl().toString(), new Date()));
                dbr.child("CONVERSATION").setValue(allConversations);
            }
        });
    }

    @Override
    public void deleteMessage(int position) {
        StorageReference imageToDelete = storage.getReferenceFromUrl(allConversations.get(position).imageUrl);
        imageToDelete.delete();
        allConversations.remove(position);
        dbr.child("CONVERSATION").setValue(allConversations);
    }

    @Override
    public void commentMessage(int position) {
        positionToComment = position;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Comment");
        alert.setMessage("Your comment");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(input.getText().toString().trim() == null) {
                    Toast.makeText(HomeActivity.this, "No comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                Conversation c = allConversations.get(positionToComment);
                c.comments.add(new Conversation.Comments(firebaseUser.getUid(), input.getText().toString().trim(), userNameFromUid, new Date()));
                dbr.child("CONVERSATION").setValue(allConversations);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
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
