package com.example.tvs.messaging;

import java.util.Date;

public class Message {

    String senderUid, recipientUid, message, imageURL;
    Date date;

    public Message() {
    }

    public Message(String senderUid, String recipientUid, String message, Date date, String imageURL) {
        this.senderUid = senderUid;
        this.recipientUid = recipientUid;
        this.message = message;
        this.date = date;
        this.imageURL = imageURL;
    }
}