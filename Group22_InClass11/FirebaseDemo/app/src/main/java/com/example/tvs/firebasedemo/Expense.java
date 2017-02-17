package com.example.tvs.firebasedemo;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    public String expName, category;
    public double amount;
    Date date;

    public Date getDate() {
        return date;
    }

    public Expense() {
    }

    public Expense(String expName, String category, double amount) {
        this.expName = expName;
        this.category = category;
        this.amount = amount;
        this.date = new Date();
    }

    public Expense(String expName, String category, double amount, Date date) {
        this.expName = expName;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getExpName() {
        return expName;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expName='" + expName + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                '}';
    }
}

