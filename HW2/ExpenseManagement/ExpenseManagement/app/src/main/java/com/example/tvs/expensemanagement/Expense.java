/*
Assignment No: Homework 2
File Name: Expense.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package com.example.tvs.expensemanagement;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Expense implements Parcelable{
    public String expName, category;
    public double amount;
    public Date date;
    public String image;

    public Expense() {
        super();
    }

    protected Expense(Parcel in) {
        expName = in.readString();
        category = in.readString();
        amount = in.readDouble();
        date = (java.util.Date) in.readSerializable();
        image = in.readString();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expName);
        dest.writeString(category);
        dest.writeDouble(amount);
        dest.writeSerializable(date);
        dest.writeString(image);
    }
}
