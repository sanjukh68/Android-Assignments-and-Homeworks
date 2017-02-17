/*
Assignment No: Homework 2
File Name: MainActivity.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package com.example.tvs.expensemanagement;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    protected final static String DATE_FORMAT = "dd MMMM, yyyy";
    protected final int ADD_CODE = 7000, EDIT_CODE = 7002, DELETE_CODE = 7004, SHOW_CODE = 7006;
    public final static int GET_ATTACHMENT_CODE = 8000;
    public final static String NEW_EXP_OBJ = "newObj", ALL_EXPENSES = "allExpenses";
    public final static String ADDED_EXP = "newlyAddedExp", DELETED_EXP = "deletedExpenses";
    public final static String INDEX_INTENT_CODE = "index";
    public ArrayList<Expense> currentExpenses = new ArrayList<>();
    public static DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.MANAGE_DOCUMENTS)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.MANAGE_DOCUMENTS}, 1);
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        Button addExpBtn, editExpBtn, delExpBtn, showExpBtn, finBtn;
        addExpBtn = (Button) findViewById(R.id.addExpense);
        editExpBtn = (Button) findViewById(R.id.editExpense);
        delExpBtn = (Button) findViewById(R.id.deleteExpense);
        showExpBtn = (Button) findViewById(R.id.showExpense);
        finBtn = (Button) findViewById(R.id.finish);

        addExpBtn.setOnClickListener(this);
        editExpBtn.setOnClickListener(this);
        delExpBtn.setOnClickListener(this);
        showExpBtn.setOnClickListener(this);
        finBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int buttonClicked = v.getId();
        switch(buttonClicked) {
            case R.id.addExpense:   Intent addExpInt = new Intent(this, AddExpense.class);
                                    Expense newExp = new Expense();
                                    addExpInt.putExtra(NEW_EXP_OBJ, newExp);
                                    startActivityForResult(addExpInt, ADD_CODE);
                                    break;
            case R.id.editExpense:  if(currentExpenses.size()==0) {
                                        Toast.makeText(this, getString(R.string.nothing_to_edit), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Intent editExpInt = new Intent(this, EditExpense.class);
                                    editExpInt.putExtra(ALL_EXPENSES, currentExpenses);
                                    startActivityForResult(editExpInt, EDIT_CODE);
                                    break;
            case R.id.deleteExpense:if(currentExpenses.size()==0) {
                                        Toast.makeText(this, getString(R.string.nothing_to_delete), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Intent delExpInt = new Intent(this, DeleteExpense.class);
                                    delExpInt.putExtra(ALL_EXPENSES, currentExpenses);
                                    startActivityForResult(delExpInt, DELETE_CODE);
                                    break;
            case R.id.showExpense:  if(currentExpenses.size()==0) {
                                        Toast.makeText(this, getString(R.string.nothing_to_show), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Intent showExpInt = new Intent(this, ShowExpense.class);
                                    showExpInt.putExtra(ALL_EXPENSES, currentExpenses);
                                    startActivityForResult(showExpInt, SHOW_CODE);
                                    break;
            case R.id.finish:       finish();
                                    break;
            default:                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case ADD_CODE:      if(resultCode==RESULT_OK) {
                                    Expense newObj = data.getParcelableExtra(ADDED_EXP);
                                    Toast.makeText(this, getString(R.string.expense_added), Toast.LENGTH_LONG).show();
                                    currentExpenses.add(newObj);
                                }
                                else if (resultCode == RESULT_CANCELED)
                                    Toast.makeText(this, getString(R.string.no_changes_done), Toast.LENGTH_LONG).show();
                                break;
            case EDIT_CODE:     if(resultCode==RESULT_OK) {
                                    int index = data.getExtras().getInt(INDEX_INTENT_CODE);
                                    Expense editedExpense = data.getParcelableExtra(ADDED_EXP);
                                    Toast.makeText(this, getString(R.string.item_updated), Toast.LENGTH_LONG).show();
                                    currentExpenses.set(index, editedExpense);
                                }
                                else if (resultCode == RESULT_CANCELED)
                                    Toast.makeText(this, getString(R.string.no_changes_done), Toast.LENGTH_LONG).show();
                                break;
            case DELETE_CODE:  if(resultCode==RESULT_OK) {
                                    int deleteId = (int) data.getExtras().get(DELETED_EXP);
                                    currentExpenses.remove(deleteId);
                                    Toast.makeText(this, getString(R.string.item_deleted), Toast.LENGTH_LONG).show();
                                }
                                else if (resultCode == RESULT_CANCELED)
                                    Toast.makeText(this, getString(R.string.no_changes_done), Toast.LENGTH_LONG).show();
                                break;
            case SHOW_CODE:     break;
            default:            break;
        }
    }
}
