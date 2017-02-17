package com.example.tvs.firebasedemo;

import java.util.ArrayList;
import java.util.Date;

public class Conversation {
    String userId, message, userName, imageUrl;
    Date addedTime;
    ArrayList<Comments> comments = new ArrayList<>();

    public Conversation() {
    }

    public Conversation(String userId, String message, String userName, Comments comment, String imageUrl, Date addedTime) {
        this.userId = userId;
        this.message = message;
        this.userName = userName;
        this.comments.add(comment);
        this.imageUrl = imageUrl;
        this.addedTime = addedTime;
    }

    public static class Comments {
        String userId, comment, userName;
        Date addedTime;

        public Comments() {
        }

        public Comments(String userId, String comment, String userName, Date addedTime) {
            this.userId = userId;
            this.comment = comment;
            this.userName = userName;
            this.addedTime = addedTime;
        }
    }
}