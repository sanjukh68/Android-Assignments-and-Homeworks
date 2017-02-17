package com.example.tvs.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXPENSES_TAG = "EXPENSES";
    public static final String DETAILS_TAG = "DETAILS";
    private String email, password;
    private EditText emailTxt, passwordTxt;
    private Button signInBtn, registerBtn;
    private ProgressDialog progressDialogue;
    private FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialogue = new ProgressDialog(this);
        firebaseAuthentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuthentication.getCurrentUser();
        if(firebaseUser != null)
            startActivity(new Intent(this, HomeActivity.class));

        emailTxt = (EditText) findViewById(R.id.emailInput);
        passwordTxt = (EditText) findViewById(R.id.passwordInput);
        signInBtn = (Button) findViewById(R.id.signUpBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        signInBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    private boolean validInput() {
        email = emailTxt.getText().toString().trim();
        password = passwordTxt.getText().toString().trim();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(String e, String p) {
        progressDialogue.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialogue.show();
        progressDialogue.setMessage("Logging in...");
        firebaseAuthentication.signInWithEmailAndPassword(e, p)
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialogue.dismiss();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            })
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialogue.dismiss();
                    if(task.isSuccessful())
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registerBtn:  startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                                    break;
            case R.id.signUpBtn:    if(!validInput()) return;
                                    login(email, password);
                                    break;
        }
    }
}
