/**
 * Assignment: Homework 04
 * File: Stats.java
 * Sujal T V, Sanju K H
 */

package com.example.tvs.triviaquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Stats extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Integer> answers = new ArrayList<>();
    Integer correctAnswers = 0, percentage = 0;

    Button goHome, replay;
    TextView tryMessage, progressValue;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        goHome = (Button) findViewById(R.id.goHome);
        replay = (Button) findViewById(R.id.replay);
        progressValue = (TextView) findViewById(R.id.progressValue);
        tryMessage = (TextView) findViewById(R.id.tryMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        goHome.setOnClickListener(this);
        replay.setOnClickListener(this);

        questions = getIntent().getParcelableArrayListExtra(MainActivity.QUESTIONS_LIST_KEY);
        answers = getIntent().getIntegerArrayListExtra(StartTrivia.ANSWERS_KEY);

        for(int i = 0; i < answers.size(); i++)
            if (questions.get(i).answer == answers.get(i)) correctAnswers++;
        percentage = (int) Math.ceil(correctAnswers*100/questions.size());
        progressValue.setText(percentage + "%");
        progressBar.setProgress(percentage);
        if(percentage==100) tryMessage.setText(getResources().getString(R.string.congratulations));
        else tryMessage.setText(getResources().getString(R.string.try_again_message));
    }

    public void onBackPressed() {
        moveTaskToBack(false);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onClick(View v) {
        int clickedElement = v.getId();
        switch(clickedElement) {
            case R.id.goHome:       startActivity(new Intent(this, MainActivity.class));
                                    break;
            case R.id.replay:       Intent startQuiz = new Intent(this, StartTrivia.class);
                                    startQuiz.putParcelableArrayListExtra(MainActivity.QUESTIONS_LIST_KEY, questions);
                                    startActivity(startQuiz);
                                    break;
        }
    }
}