/**
 * Assignment: Homework 04
 * File: StartTrivia.java
 * Sujal T V, Sanju K H
 */

package com.example.tvs.triviaquiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StartTrivia extends AppCompatActivity implements View.OnClickListener, GetImage.ImageApiResponse{
    Integer questionIndex = 0;
    static long timeStamp = 120000;
    final static String QUESTION_NUMBER = "Q", TIMESPAN_TEXT = "Time Left: ", TIME_UNIT = " seconds", ANSWERS_KEY = "Answers";
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Integer> answers = new ArrayList<>();
    Question newQuestion;

    Button quit, next;
    ProgressBar imageStatusBar;
    ImageView noImage;
    RadioGroup options;
    TextView questionNo, timeLeft, question, imageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_trivia);

        quit = (Button) findViewById(R.id.quit);
        next = (Button) findViewById(R.id.next);
        questionNo = (TextView) findViewById(R.id.questionNo);
        timeLeft = (TextView) findViewById(R.id.timeLeft);
        question = (TextView) findViewById(R.id.question);
        imageStatus = (TextView) findViewById(R.id.imageStatus);
        imageStatusBar = (ProgressBar) findViewById(R.id.imageStatusBar);
        noImage = (ImageView) findViewById(R.id.noImage);
        options = (RadioGroup) findViewById(R.id.options);

        quit.setOnClickListener(this);
        next.setOnClickListener(this);

        timeLeft.setText(TIMESPAN_TEXT + timeStamp/1000 + TIME_UNIT);
        countDownTimer.start();
        questions = getIntent().getParcelableArrayListExtra(MainActivity.QUESTIONS_LIST_KEY);
        showQuestionDetails(questionIndex);
    }

    @Override
    public void onClick(View v) {
        int clickedElement = v.getId();
        switch(clickedElement) {
            case R.id.quit:     countDownTimer.cancel();
                                Intent goHome = new Intent(this, MainActivity.class);
                                startActivity(goHome);
                                break;
            case R.id.next:     storeAnswer();
                                if(questionIndex+1==questions.size()) getStatus();
                                else showQuestionDetails(++questionIndex);
                                break;
        }
    }

    void showQuestionDetails(Integer index) {
        newQuestion = questions.get(index);
        questionNo.setText(QUESTION_NUMBER + (index+1));
        question.setText(newQuestion.question);
        if(newQuestion.imageUrl==null) {
            imageStatus.setText(null);
            imageStatusBar.setVisibility(View.GONE);
            noImage.setVisibility(View.VISIBLE);
            noImage.setImageResource(R.drawable.no_image);
        }
        else {
            imageStatus.setText(getResources().getString(R.string.image_status));
            imageStatusBar.setVisibility(View.VISIBLE);
            noImage.setVisibility(View.GONE);
            new GetImage(this).execute(newQuestion.imageUrl);
        }
        options.removeAllViews();
        for(int i=0;i<newQuestion.choises.size();i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(newQuestion.choises.get(i).toString());
            rb.setId(i);
            options.addView(rb);
        }
    }

    @Override
    public void handleResponse(Bitmap image) {
        imageStatus.setText(null);
        imageStatusBar.setVisibility(View.GONE);
        noImage.setVisibility(View.VISIBLE);
        noImage.setImageBitmap(image);
    }

    CountDownTimer countDownTimer = new CountDownTimer(timeStamp, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft.setText(TIMESPAN_TEXT + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + TIME_UNIT);
        }

        @Override
        public void onFinish() {
            timeLeft.setText(TIMESPAN_TEXT + 0 + TIME_UNIT);
            Toast.makeText(StartTrivia.this, getResources().getString(R.string.time_over), Toast.LENGTH_SHORT).show();
            storeAnswer();
            getStatus();
        }
    };

    void getStatus() {
        Intent showStats = new Intent(this, Stats.class);
        showStats.putParcelableArrayListExtra(MainActivity.QUESTIONS_LIST_KEY, questions);
        if(questionIndex + 1 < questions.size())
            for(int i = ++questionIndex; i < questions.size(); i++) answers.add(-1);
        showStats.putIntegerArrayListExtra(ANSWERS_KEY, answers);
        countDownTimer.cancel();
        startActivity(showStats);
    }

    void storeAnswer() {
        answers.add(options.getCheckedRadioButtonId()+1);
        options.clearCheck();
    }
}