package com.example.tvs.messaging;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityProfileEdit extends AppCompatActivity implements View.OnClickListener {

    private TextView profileFirstName, profileLastName, profileGender;
    private EditText editFNameField, editLNameField;
    private RadioGroup editGenderGB;
    private RadioButton profileMaleBtn, profileFemaleBtn;
    private ImageButton tnEditor;
    private ImageView imageViewTN;
    private static FirebaseStorage storage;
    private Button updateProfileBtn, profileDone;
    private Bitmap imageInBitmap;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuthentication;
    private static DatabaseReference databaseReference;
    private static int newImageAdded = 0;
    private String imageUrl;
    private int position;
    private static ArrayList<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS");
        storage = FirebaseStorage.getInstance();
        firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuthentication.getCurrentUser();

        editFNameField = (EditText) findViewById(R.id.editNameField);
        editLNameField = (EditText) findViewById(R.id.editText3);
        editGenderGB = (RadioGroup) findViewById(R.id.profileGenderGB);
        imageViewTN = (ImageView) findViewById(R.id.imageViewTN);
        profileMaleBtn = (RadioButton) findViewById(R.id.profileMaleBtn);
        profileFemaleBtn = (RadioButton) findViewById(R.id.profileFemaleBtn);
        updateProfileBtn = (Button) findViewById(R.id.updateProfileBtn);
        profileDone = (Button) findViewById(R.id.profileDone);
        tnEditor = (ImageButton) findViewById(R.id.tnEditor);

        profileFirstName = (TextView) findViewById(R.id.profileFirstName);
        profileLastName = (TextView) findViewById(R.id.profileLastName);
        profileGender = (TextView) findViewById(R.id.profileGender);

        profileDone.setOnClickListener(this);
        updateProfileBtn.setOnClickListener(this);
        tnEditor.setOnClickListener(this);

        User user = (User) getIntent().getExtras().getSerializable("UID");
        imageUrl = ((User) getIntent().getExtras().getSerializable("UID")).profileUrl;
        position = getIntent().getExtras().getInt("POSITION");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<User>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<User>>() {};
                allUsers = dataSnapshot.getValue(genericTypeIndicator);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(firebaseAuth.getCurrentUser().getUid().equals(user.uid)) {
            profileFirstName.setVisibility(View.INVISIBLE);
            profileLastName.setVisibility(View.INVISIBLE);
            profileGender.setVisibility(View.INVISIBLE);

            editFNameField.setText(user.fName);
            editLNameField.setText(user.lName);
            if(user.gender.equals("Male"))
                profileMaleBtn.setChecked(true);
            else profileFemaleBtn.setChecked(true);
            Picasso.with(this).load(user.profileUrl).into(imageViewTN);
        } else {
            editFNameField.setVisibility(View.INVISIBLE);
            editLNameField.setVisibility(View.INVISIBLE);
            editGenderGB.setVisibility(View.INVISIBLE);
            updateProfileBtn.setVisibility(View.INVISIBLE);
            tnEditor.setVisibility(View.INVISIBLE);
            profileFirstName.setText(user.fName);
            profileLastName.setText(user.lName);
            profileGender.setText(user.gender);
            Picasso.with(this).load(user.profileUrl).into(imageViewTN);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tnEditor:
                Intent openGallery = new Intent();
                openGallery.setType("image/*");
                openGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(openGallery, 240);
                break;
            case R.id.updateProfileBtn:
                if(newImageAdded==0) {
                    String fName = editFNameField.getText().toString();
                    String lName = editLNameField.getText().toString();
                    String gender = profileMaleBtn.isChecked() ? "Male" : "Female";
                    User userupdate = new User(fName, lName, fName + " " + lName, gender, firebaseAuth.getCurrentUser().getUid(), null);
                    allUsers.remove(position);
                    allUsers.add(position, userupdate);
                    databaseReference.setValue(allUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ActivityProfileEdit.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    uploadImage();
                break;
            case R.id.profileDone:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 240:
                if(resultCode== Activity.RESULT_OK) {
                    try {
                        imageInBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imageViewTN.setImageBitmap(imageInBitmap);
                        newImageAdded = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(resultCode!=0) Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void uploadImage() {
        StorageReference userImageFolder = storage.getReference().child(firebaseUser.getUid()).child("images/");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(imageInBitmap == null)
            imageInBitmap = ((BitmapDrawable) imageViewTN.getDrawable()).getBitmap();
        imageInBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imageRef = userImageFolder.child("Profile Picture");
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                for(int i = 0; i < ActivityRegistration.userDetails.size(); i++) {
                    if(ActivityRegistration.userDetails.get(i).uid.equals(firebaseAuthentication.getCurrentUser().getUid())) {
                        imageUrl = taskSnapshot.getDownloadUrl().toString();
                        break;
                    }
                }
                String fName = editFNameField.getText().toString();
                String lName = editLNameField.getText().toString();
                String gender = profileMaleBtn.isChecked() ? "Male" : "Female";
                User userupdate = new User(fName, lName, fName + " " + lName, gender, firebaseAuth.getCurrentUser().getUid(), imageUrl);
                allUsers.remove(position);
                allUsers.add(position, userupdate);
                databaseReference.setValue(allUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActivityProfileEdit.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}