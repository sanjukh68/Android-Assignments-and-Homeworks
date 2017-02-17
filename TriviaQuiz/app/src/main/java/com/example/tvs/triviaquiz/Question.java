/**
 * Assignment: Homework 04
 * File: Question.java
 * Sujal T V, Sanju K H
 */

package com.example.tvs.triviaquiz;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by TVS on 23/9/16.
 */

public class Question implements Parcelable{
    Integer id, answer;
    String question, imageUrl;
    ArrayList<String> choises;

    Question(Integer id, String question, String imageUrl, ArrayList<String> choises, Integer answer) {
        this.id = id;
        this.question = question;
        this.imageUrl = imageUrl;
        this.choises = choises;
        this.answer = answer;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        imageUrl = in.readString();
        choises = in.createStringArrayList();
        answer = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public static ArrayList<Question> parseResponse(String response) throws JSONException {
        ArrayList<Question> questions = new ArrayList<>();
        Question question;
        JSONObject data = new JSONObject(response);
        JSONArray listOfQuestions = data.getJSONArray("questions");
        for(int i=0;i<listOfQuestions.length();i++) {
            JSONObject questionObject = (JSONObject) listOfQuestions.get(i);
            Integer id = questionObject.getInt("id");
            String questionText = questionObject.getString("text");
            String imageUrl = null;
            if(questionObject.has("image")) imageUrl = questionObject.getString("image");

            JSONObject choiseObject = questionObject.getJSONObject("choices");
            Integer answer = choiseObject.getInt("answer");
            JSONArray choiseArray = choiseObject.getJSONArray("choice");
            ArrayList<String> choisesList = new ArrayList<String>();
            for(int j = 0; j < choiseArray.length(); j++)
                choisesList.add(choiseArray.getString(j));
            question = new Question(id, questionText, imageUrl, choisesList, answer);
            questions.add(question);
        }
        return questions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(imageUrl);
        dest.writeStringList(choises);
        dest.writeInt(answer);
    }
}