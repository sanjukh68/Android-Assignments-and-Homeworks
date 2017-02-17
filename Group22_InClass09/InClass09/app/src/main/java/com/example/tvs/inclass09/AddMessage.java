package com.example.tvs.inclass09;

public class AddMessage {
    String status;
    MessageDetails message;

    public String getStatus() {
        return status;
    }

    class MessageDetails {
        long Id;
        String Comment, UserId, FileThumbnailId;
        String Type, CreatedAt, UpdatedAt;
    }
}
