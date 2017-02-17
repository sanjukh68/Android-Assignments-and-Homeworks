package com.example.tvs.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static String fullName;
    private TextView fullNameText, emailTxt, passwordTxt;
    private Button register, cancel;
    private ProgressDialog progressDialogue;
    private FirebaseAuth firebaseAuthentication;
    private static FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialogue = new ProgressDialog(this);
        firebaseAuthentication = FirebaseAuth.getInstance();

        fullNameText = (TextView) findViewById(R.id.fullNameInput);
        emailTxt = (TextView) findViewById(R.id.emailInput);
        passwordTxt = (TextView) findViewById(R.id.passwordInput);

        register = (Button) findViewById(R.id.signUpBtn);
        cancel = (Button) findViewById(R.id.cancelBtn);

        register.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void register(String fn, String e, String p) {
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
                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName).build();
                            firebaseUser.updateProfile(userProfileChangeRequest);

                            databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());

                            Map<String, ArrayList<Expense>> expenseTag = new HashMap<>();
                            ArrayList<Expense> expenses = new ArrayList<>();
                            expenseTag.put(MainActivity.EXPENSES_TAG, expenses);
                            databaseReference.child(MainActivity.EXPENSES_TAG).setValue(expenses);

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    }
                });
    }

    private boolean validInput(String fullName, String email, String password) {
        if(TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signUpBtn:    String fullName = fullNameText.getText().toString().trim();
                                    String email = emailTxt.getText().toString().trim();
                                    String password = passwordTxt.getText().toString().trim();
                                    if(validInput(fullName, email, password)) {
                                        progressDialogue.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialogue.show();
                                        progressDialogue.setMessage("Registering...");
                                        register(fullName, email, password);
                                    }
                                    break;
            case R.id.cancelBtn:    finish();
                                    break;
        }
    }
}
