package com.example.tvs.messaging;

import java.io.Serializable;

public class User implements Serializable {

    String fName, lName, fullName;
    String gender;
    String uid;

    String profileUrl;

    public User() {
    }

    public User(String fName, String lName, String fullName, String gender, String uid, String profileUrl) {
        this.uid = uid;
        this.fName = fName;
        this.lName = lName;
        this.fullName = fullName;
        this.gender = gender;
        this.profileUrl = profileUrl;
    }
}
