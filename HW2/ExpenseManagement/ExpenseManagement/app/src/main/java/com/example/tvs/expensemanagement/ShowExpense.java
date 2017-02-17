/*
Assignment No: Homework 2
File Name: ShowExpense.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package com.example.tvs.expensemanagement;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowExpense extends AppCompatActivity implements Button.OnClickListener{
    private int expenseIndex = 0;
    private ArrayList<Expense> expenses = new ArrayList<>();

    Button finishButton;
    TextView nameValue, categoryValue, amountValue, dateValue;
    ImageView imageViewForward, imageViewBackward, imageViewFastForward, imageViewRewind, imageViewReceipt;
    Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        nameValue = (TextView)findViewById(R.id.textViewNameValue);
        categoryValue = (TextView)findViewById(R.id.textViewCategoryValue);
        amountValue = (TextView)findViewById(R.id.textViewAmountValue);
        dateValue = (TextView)findViewById(R.id.textViewDateValue);
        finishButton = (Button) findViewById(R.id.buttonFinish);
        imageViewForward = (ImageView) findViewById(R.id.imageViewForward);
        imageViewBackward = (ImageView)findViewById(R.id.imageViewBackward);
        imageViewFastForward = (ImageView)findViewById(R.id.imageViewFastForward);
        imageViewRewind = (ImageView)findViewById(R.id.imageViewRewind);
        imageViewReceipt = (ImageView)findViewById(R.id.imageViewReceipt);

        finishButton.setOnClickListener(this);
        imageViewForward.setOnClickListener(this);
        imageViewBackward.setOnClickListener(this);
        imageViewFastForward.setOnClickListener(this);
        imageViewRewind.setOnClickListener(this);

        expenses = getIntent().getParcelableArrayListExtra(MainActivity.ALL_EXPENSES);
        ShowExpense();
    }

    public void ShowExpense() {
        Log.d("demo", "ShowExpense " + expenseIndex);
        expense = expenses.get(expenseIndex);
        nameValue.setText(expense.expName);

        Date startDate = expense.date;
        try {
            String dateInString = MainActivity.dateFormat.format(startDate);
            dateValue.setText(dateInString);
            dateValue.setTag(startDate);
        } catch(Exception e) {}


        amountValue.setText(Double.toString(expense.amount));
        Log.d("demo", "ShowExpense came here 1");
        try {
            imageViewReceipt.setImageURI(Uri.parse(expense.image));
        } catch (Exception e) {
            imageViewReceipt.setImageResource(R.mipmap.ic_add_to_queue_black_24dp);
        }

        categoryValue.setText(expense.category);

        if(expenses.size()==1) {
            imageViewRewind.setEnabled(false);
            imageViewBackward.setEnabled(false);
            imageViewFastForward.setEnabled(false);
            imageViewForward.setEnabled(false);
        }
        else {
            imageViewRewind.setEnabled(true);
            imageViewBackward.setEnabled(true);
            imageViewFastForward.setEnabled(true);
            imageViewForward.setEnabled(true);
        }

        if (expenseIndex == 0) {
            imageViewRewind.setEnabled(false);
            imageViewBackward.setEnabled(false);
        }
        else if (expenseIndex == expenses.size() - 1) {
            imageViewForward.setEnabled(false);
            imageViewFastForward.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        int clickedElement = view.getId();
        switch (clickedElement) {
            case R.id.imageViewForward:     expenseIndex++;
                                            ShowExpense();
                                            break;
            case R.id.imageViewBackward:    expenseIndex--;
                                            ShowExpense();
                                            break;
            case R.id.imageViewFastForward: expenseIndex = expenses.size() -1;
                                            ShowExpense();
                                            break;
            case R.id.imageViewRewind:      expenseIndex = 0;
                                            ShowExpense();
                                            break;
            case R.id.buttonFinish:         finish();
                                            break;
        }
    }
}