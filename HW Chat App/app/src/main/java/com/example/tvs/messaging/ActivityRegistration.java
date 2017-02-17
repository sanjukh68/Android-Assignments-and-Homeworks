package com.example.tvs.messaging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityRegistration extends AppCompatActivity implements View.OnClickListener {

    private static String fullName;
    private TextView firstNameTxt, lastNameTxt, emailTxt, password1Txt, password2Txt;
    private Button register, cancel;
    private ImageView addProfilePic, profilePic;
    private ProgressDialog progressDialogue;
    private FirebaseAuth firebaseAuthentication;
    private RadioButton maleCheckbox;
    private Bitmap imageInBitmap = null;
    private static FirebaseStorage storage;
    private static FirebaseUser firebaseUser;
    private static DatabaseReference databaseReference;
    public static ArrayList<User> userDetails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        addProfilePic = (ImageView) findViewById(R.id.addProfilePic);
        profilePic = (ImageView) findViewById(R.id.profilePicReg);
        firstNameTxt = (TextView) findViewById(R.id.firstNameInput);
        lastNameTxt = (TextView) findViewById(R.id.lastNameInput);
        emailTxt = (TextView) findViewById(R.id.emailInput);
        password1Txt = (TextView) findViewById(R.id.password1Input);
        password2Txt = (TextView) findViewById(R.id.password2Input);
        register = (Button) findViewById(R.id.loginBtn);
        cancel = (Button) findViewById(R.id.cancelBtn);
        maleCheckbox = (RadioButton) findViewById(R.id.maleCheckbox);

        profilePic.setOnClickListener(this);
        addProfilePic.setOnClickListener(this);
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);

        progressDialogue = new ProgressDialog(this);
        progressDialogue.setCancelable(false);
        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<User>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<User>>() {};
                ActivityRegistration.userDetails = dataSnapshot.getValue(genericTypeIndicator);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void register(String fn, String e, String p, final String fName, final String lName, final String gender) {
        fullName = fn;
        firebaseAuthentication.createUserWithEmailAndPassword(e, p)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialogue.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialogue.dismiss();
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration is successful", Toast.LENGTH_SHORT).show();

                            firebaseUser = firebaseAuthentication.getCurrentUser();
                            storage = FirebaseStorage.getInstance();

                            ActivityRegistration.userDetails.add(new User(fName, lName, fullName, gender, firebaseUser.getUid(), null));
                            databaseReference.setValue(ActivityRegistration.userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadImage();
                                }
                            });
                        }
                    }
                });
    }

    private boolean validInput(String fullName, String email, String password1, String password2) {
        if(TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2) ) {
            Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!password1.equals(password2)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.profilePicReg:
            case R.id.addProfilePic:
                                    Intent openGallery = new Intent();
                                    openGallery.setType("image/*");
                                    openGallery.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(openGallery, 220);
                                    break;
            case R.id.loginBtn:     String firstName = firstNameTxt.getText().toString().trim();
                                    String lastName = lastNameTxt.getText().toString().trim();
                                    String email = emailTxt.getText().toString().trim();
                                    String password1 = password1Txt.getText().toString().trim();
                                    String password2 = password2Txt.getText().toString().trim();
                                    String fullNametext = firstName + " " + lastName;
                                    String gender = maleCheckbox.isChecked()?"Male":"Female";
                                    if(validInput(fullNametext, email, password1, password2)) {
                                        progressDialogue.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialogue.show();
                                        progressDialogue.setMessage("Registering...");
                                        register(fullNametext, email, password1, firstName, lastName, gender);
                                    }
                                    break;
            case R.id.cancelBtn:    finish();
                                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 220:
                if(resultCode== Activity.RESULT_OK) {
                    try {
                        imageInBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        profilePic.setImageBitmap(imageInBitmap);
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
            imageInBitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
        imageInBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imageRef = userImageFolder.child("Profile Picture");
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                for(int i = 0; i < ActivityRegistration.userDetails.size(); i++) {
                    if(ActivityRegistration.userDetails.get(i).uid.equals(firebaseAuthentication.getCurrentUser().getUid())) {
                        ActivityRegistration.userDetails.get(i).profileUrl = taskSnapshot.getDownloadUrl().toString();
                        break;
                    }
                }
                databaseReference.setValue(ActivityRegistration.userDetails);
                firebaseAuthentication.signOut();
                startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
            }
        });
    }
}