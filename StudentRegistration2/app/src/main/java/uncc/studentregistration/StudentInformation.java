package uncc.studentregistration;

import java.io.Serializable;

/**
 * Created by sanju on 9/6/2016.
 */
public class StudentInformation implements Serializable{
    String name;
    String email;
    String Department;
    String mood;

    public StudentInformation(String name, String email, String department, String mood) {
        this.name = name;
        this.email = email;
        Department = department;
        this.mood = mood;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return Department;
    }

    public String getMood() {
        return mood;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
