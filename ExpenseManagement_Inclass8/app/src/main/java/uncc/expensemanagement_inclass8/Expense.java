/*
Assignment No: Homework 2
File Name: Expense.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package uncc.expensemanagement_inclass8;


import java.util.Date;

public class Expense{
    public String expName, category;
    public double amount;
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Expense(String expName, String category, double amount) {
        this.expName = expName;
        this.category = category;
        this.amount = amount;
        this.date = new Date();

    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
