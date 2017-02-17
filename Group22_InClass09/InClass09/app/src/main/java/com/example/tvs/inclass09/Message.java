package com.example.tvs.inclass09;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{

    ArrayList<MessageDetails> messages;

    class MessageDetails {
        String UserFname, UserLname,Id, Comment, FileThumbnailId;
        String Type, CreatedAt;

        public String getUserFname() {
            return UserFname;
        }

        public void setUserFname(String userFname) {
            UserFname = userFname;
        }

        public String getUserLname() {
            return UserLname;
        }

        public void setUserLname(String userLname) {
            UserLname = userLname;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String comment) {
            Comment = comment;
        }

        public String getFileThumbnailId() {
            return FileThumbnailId;
        }

        public void setFileThumbnailId(String fileThumbnailId) {
            FileThumbnailId = fileThumbnailId;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getCreatedAt() {
            return CreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            CreatedAt = createdAt;
        }
    }

    public ArrayList<MessageDetails> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageDetails> messages) {
        this.messages = messages;
    }
}
