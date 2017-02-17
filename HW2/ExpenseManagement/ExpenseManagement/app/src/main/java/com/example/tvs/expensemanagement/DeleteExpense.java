/*
Assignment No: Homework 2
File Name: DeleteExpense.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package com.example.tvs.expensemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class DeleteExpense extends AppCompatActivity implements View.OnClickListener{
    public int indexOfIBE;
    public Expense itemToBeEdited;
    public ArrayList<Expense> expenses = new ArrayList<>();
    public ArrayList<String> expenseNames = new ArrayList<>();

    public Button getExpense, deleteExpense, cancelDelete;
    public TextView category;
    public ImageView attachment;
    public EditText dateField, amount, editExpName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expense);

        getExpense = (Button) findViewById(R.id.getExpenses);
        deleteExpense = (Button) findViewById(R.id.deleteExpense);
        cancelDelete = (Button) findViewById(R.id.cancelDelete);
        category = (TextView) findViewById(R.id.categoryText);
        editExpName = (EditText) findViewById(R.id.editExpName);
        amount = (EditText) findViewById(R.id.editAmount);
        dateField = (EditText) findViewById(R.id.editDate);
        attachment = (ImageView) findViewById(R.id.editAttachment);

        getExpense.setOnClickListener(this);
        deleteExpense.setOnClickListener(this);
        cancelDelete.setOnClickListener(this);

        expenses = getIntent().getParcelableArrayListExtra(MainActivity.ALL_EXPENSES);
        for(Expense e : expenses)
            expenseNames.add(e.expName);

        amount.setEnabled(false);
        dateField.setEnabled(false);
    }


    @Override
    public void onClick(View view) {{
        int clickedElement = view.getId();
        switch (clickedElement) {
            case R.id.getExpenses:  AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setTitle(R.string.pick_exp_label)
                                            .setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, expenseNames), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    indexOfIBE = which;
                                                    itemToBeEdited = expenses.get(which);
                                                    editExpName.setText(itemToBeEdited.expName);
                                                    category.setText(itemToBeEdited.category);

                                                    Date startDate = itemToBeEdited.date;
                                                    try {
                                                        String dateInString = MainActivity.dateFormat.format(startDate);
                                                        dateField.setText(dateInString);
                                                        dateField.setTag(startDate);
                                                    } catch(Exception e) {}

                                                    amount.setText(Double.toString(itemToBeEdited.amount));
                                                    try {
                                                        attachment.setImageURI(Uri.parse(itemToBeEdited.image));
                                                    } catch (Exception e) {
                                                        attachment.setImageResource(R.mipmap.ic_add_to_queue_black_24dp);
                                                    }
                                                }
                                            });
                                    AlertDialog dialogue = builder.create();
                                    dialogue.show();
                                    break;
            case R.id.deleteExpense:Intent deleteExp = new Intent();
                                    deleteExp.putExtra(MainActivity.DELETED_EXP, indexOfIBE);
                                    setResult(RESULT_OK, deleteExp);
                                    finish();
                                    break;
            case R.id.cancelDelete:   setResult(RESULT_CANCELED);
                                    finish();
            default:                break;
        }
    }

    }
}
