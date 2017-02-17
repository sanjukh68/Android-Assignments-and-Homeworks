package com.example.tvs.messaging;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private String email, password;
    private EditText emailTxt, passwordTxt;
    private Button signInBtn, signupBtn;
    private ProgressDialog progressDialogue;
    private FirebaseAuth firebaseAuthentication;
    private ImageView firebase, google, facebook;
    private RadioGroup loginModes;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 200;
    private int loginMethod = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuthentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuthentication.getCurrentUser();

        if(firebaseUser != null)
            startActivity(new Intent(this, ActivityUserList.class));

        progressDialogue = new ProgressDialog(this);
        emailTxt = (EditText) findViewById(R.id.emailInput);
        passwordTxt = (EditText) findViewById(R.id.password1Input);
        signInBtn = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        firebase = (ImageView) findViewById(R.id.firebaseImage);
        google = (ImageView) findViewById(R.id.googleImage);
        facebook = (ImageView) findViewById(R.id.facebookImage);
        loginModes = (RadioGroup) findViewById(R.id.loginModes);

        loginModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.firebaseBtn:
                        loginMethod = 0;
                        break;
                    case R.id.googleBtn:
                        loginMethod = 1;
                        break;
                    case R.id.facebookBtn:
                        loginMethod = 2;
                        break;
                }
            }
        });

        firebase.setOnClickListener(this);
        google.setOnClickListener(this);
        facebook.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
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

    private void loginWithFirebase(String e, String p) {
        firebaseAuthentication.signInWithEmailAndPassword(e, p)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialogue.dismiss();
                        Toast.makeText(ActivityLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialogue.dismiss();
                        if(task.isSuccessful())
                            startActivity(new Intent(getApplicationContext(), ActivityUserList.class));
                    }
                });
    }

    private void loginWithGoogle(String e, String p) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            startActivity(new Intent(getApplicationContext(), ActivityUserList.class));
        } else {
        }
        progressDialogue.dismiss();
    }

    private void loginWithFacebook(String e, String p) {

    }

    private void login(String e, String p) {
        progressDialogue.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialogue.show();
        progressDialogue.setMessage("Logging in...");
        switch(loginMethod) {
            case 0:
                loginWithFirebase(e, p);
                break;
            case 1:
                loginWithGoogle(e, p);
                break;
            case 2:
                loginWithFacebook(e, p);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signupBtn:
                startActivity(new Intent(getApplicationContext(), ActivityRegistration.class));
                break;
            case R.id.loginBtn:
                if(!validInput()) return;
                login(email, password);
                break;
        }
    }
}
