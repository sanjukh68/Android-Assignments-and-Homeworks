package com.example.tvs.inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String BASE_URL = "http://ec2-54-166-14-133.compute-1.amazonaws.com/api/";
    public static final String FILE_URL = BASE_URL + "file/";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String SIGNUP_URL = BASE_URL + "signup";
    public static final String MESSAGE_URL = BASE_URL + "messages";
    public static final String ADD_MESSAGE = BASE_URL + "message/add";

    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;

    String emailData, passwordData;
    EditText email, password;
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.logIn);
        signup = (Button) findViewById(R.id.signUp);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.signUp:   Intent signUpIntent = new Intent(this, SignUpActivity.class);
                                startActivity(signUpIntent);
                                break;
            case R.id.logIn:    emailData = email.getText().toString();
                                passwordData = password.getText().toString();
                                if(emailData.length() == 0 || passwordData.length() == 0) {
                                    Toast.makeText(this, "One or more fields are invalid", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                new LoginTask(this).execute(emailData, passwordData);
        }
    }

    public class LoginTask extends AsyncTask<String, Void, Void> {

        private final OkHttpClient client = new OkHttpClient();
        Context context;

        public LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("email", params[0])
                    .add("password", params[1])
                    .build();

            Request request = new Request.Builder()
                    .url(MainActivity.LOGIN_URL)
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Gson gson = new Gson();
                LoginResp loginDetails = gson.fromJson(response.body().string(), LoginResp.class);
                if (loginDetails.getStatus().toLowerCase().equals("ok")) {
                    editor.putString("TOKEN", loginDetails.getToken());
                    editor.commit();
                    Intent messages = new Intent(context, MessagesActivity.class);
                    messages.putExtra("RESPONSE", loginDetails);
                    startActivity(messages);
                }
                else Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}