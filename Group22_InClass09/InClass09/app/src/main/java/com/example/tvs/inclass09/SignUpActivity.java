package com.example.tvs.inclass09;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    String fNameData, lNameData, emailData, password1Data, password2Data;
    EditText fName, lName, email, password1, password2;
    Button cancelSignup, startSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cancelSignup = (Button) findViewById(R.id.cancelSignUp);
        startSignup = (Button) findViewById(R.id.startSignup);

        cancelSignup.setOnClickListener(this);
        startSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.cancelSignUp: finish();
                                    break;
            case R.id.startSignup:  signupTriggered();
                                    break;
        }
    }

    private void signupTriggered() {
        fName = (EditText) findViewById(R.id.fName);
        lName = (EditText) findViewById(R.id.lName);
        email = (EditText) findViewById(R.id.email);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);

        fNameData = fName.getText().toString();
        lNameData = lName.getText().toString();
        emailData = email.getText().toString();
        password1Data = password1.getText().toString();
        password2Data = password2.getText().toString();

        if(fNameData.length() == 0 || lNameData.length() == 0 ||
                emailData.length() == 0 || password1Data.length() == 0 || password2Data.length() == 0) {
            Toast.makeText(this, "One or more fields are invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password1Data.equals(password2Data)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        new SignupTask().execute(emailData, password1Data, fNameData, lNameData);
    }

    public class SignupTask extends AsyncTask<String, Void, Void> {

        private final OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... params) {
            Log.d("Demo", params[0] + params[1] + params[2] + params[3] + "");
            RequestBody formBody = new FormBody.Builder()
                    .add("email", params[0])
                    .add("password", params[1])
                    .add("fname", params[2])
                    .add("lname", params[3])
                    .build();

            Request request = new Request.Builder()
                    .url(MainActivity.SIGNUP_URL)
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Gson gson = new Gson();
                LoginResp loginDetails = gson.fromJson(response.body().string(), LoginResp.class);
                if (loginDetails.getStatus().toLowerCase().equals("ok")) {
                    MainActivity.editor.putString("TOKEN", loginDetails.getToken());
                    MainActivity.editor.commit();
                    Intent messages = new Intent(SignUpActivity.this, MessagesActivity.class);
                    messages.putExtra("RESPONSE", loginDetails);
                    startActivity(messages);
                }
                else Toast.makeText(SignUpActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
