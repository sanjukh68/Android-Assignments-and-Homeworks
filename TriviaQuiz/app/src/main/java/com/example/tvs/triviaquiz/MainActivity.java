/**
 * Assignment: Homework 04
 * File: MainActivity.java
 * Sujal T V, Sanju K H
 */

package com.example.tvs.triviaquiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetQuestions.QuestionApiResponse {
    ArrayList<Question> questions;
    final static String QUESTIONS_LIST_KEY = "Questions";
    final static String BASE_URL = "http://dev.theappsdr.com/apis/trivia_json/index.php";

    Button exit, start;
    TextView appStatusText;
    ImageView appStatusImage;
    ProgressBar appStatusLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exit = (Button) findViewById(R.id.exit);
        start = (Button) findViewById(R.id.start);
        appStatusLoader = (ProgressBar) findViewById(R.id.appStatusLoader);
        appStatusImage =  (ImageView) findViewById(R.id.appLogo);
        appStatusText = (TextView) findViewById(R.id.appStatusText);

        exit.setOnClickListener(this);
        start.setOnClickListener(this);

        if(!isConnected()) {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            appStatusLoader.setVisibility(View.GONE);
            appStatusText.setText(getResources().getString(R.string.no_internet));
        }
        else new GetQuestions(this).execute(BASE_URL);
    }

    @Override
    public void onClick(View v) {
        int clickedElement = v.getId();
        switch(clickedElement) {
            case R.id.exit:     finish();
                                break;
            case R.id.start:    Intent startTrivia = new Intent(this, StartTrivia.class);
                                startTrivia.putParcelableArrayListExtra(QUESTIONS_LIST_KEY, questions);
                                startActivity(startTrivia);
                                break;
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) return true;
        return false;
    }

    @Override
    public void handleResponse(ArrayList<Question> response) {
        questions = response;
        appStatusLoader.setVisibility(View.GONE);
        appStatusImage.setVisibility(View.VISIBLE);
        start.setEnabled(true);
        appStatusText.setText(getResources().getString(R.string.app_ready));

    }

    public void onBackPressed() {
        //moveTaskToBack(false);
        finish();
    }
}