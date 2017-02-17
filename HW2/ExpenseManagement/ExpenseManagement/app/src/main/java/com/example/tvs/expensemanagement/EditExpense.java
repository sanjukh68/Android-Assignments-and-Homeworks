/*
Assignment No: Homework 2
File Name: EditExpense.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package com.example.tvs.expensemanagement;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditExpense extends AppCompatActivity implements View.OnClickListener {
    public int indexOfIBE, SelectedCategoryIndex;
    public String uriOfSelectedImage, selectedCategory;
    public Expense itemToBeEdited;
    public ArrayList<Expense> expenses = new ArrayList<>();
    public ArrayList<String> expenseNames = new ArrayList<>();
    public ArrayList<String> categoryList = new ArrayList<>();

    public ImageView calendar, attachment;
    public DatePickerDialog expenseDate;
    public Calendar newCalendar = Calendar.getInstance();
    public EditText editExpName, dateField, expenseName, amount;
    public Button getExpense, editExpense, saveEdit, cancelEdit;
    public Spinner categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        saveEdit = (Button) findViewById(R.id.saveEdit);
        cancelEdit = (Button) findViewById(R.id.cancelEdit);
        editExpName = (EditText) findViewById(R.id.editExpName);
        categories = (Spinner) findViewById(R.id.editCategory);
        amount = (EditText) findViewById(R.id.editAmount);
        dateField = (EditText) findViewById(R.id.editDate);
        calendar = (ImageView) findViewById(R.id.editDateImg);
        attachment = (ImageView) findViewById(R.id.editAttachment);
        editExpense = (Button) findViewById(R.id.saveEdit);
        getExpense = (Button) findViewById(R.id.getExpenses);
        expenseName = (EditText) findViewById(R.id.editExpName);

        getExpense.setOnClickListener(this);
        dateField.setOnClickListener(this);
        dateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        calendar.setOnClickListener(this);
        attachment.setOnClickListener(this);
        editExpense.setOnClickListener(this);
        cancelEdit.setOnClickListener(this);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedCategoryIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        expenses = getIntent().getParcelableArrayListExtra(MainActivity.ALL_EXPENSES);
        for(Expense e : expenses)
            expenseNames.add(e.expName);

        categoryList.add(getString(R.string.category_hint));
        categoryList.add(getString(R.string.category_groceries));
        categoryList.add(getString(R.string.category_invoice));
        categoryList.add(getString(R.string.category_transportation));
        categoryList.add(getString(R.string.category_shopping));
        categoryList.add(getString(R.string.category_rent));
        categoryList.add(getString(R.string.category_trips));
        categoryList.add(getString(R.string.category_utils));
        categoryList.add(getString(R.string.category_others));
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(categorySpinnerAdapter);

        expenseDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String dateToDisplay = dayOfMonth + " " + new DateFormatSymbols().getMonths()[month] + ", " + year;
                    dateField.setText(dateToDisplay);
                    Date temp = MainActivity.dateFormat.parse(dateToDisplay);
                    dateField.setTag(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        calendar.setEnabled(false);
        attachment.setEnabled(false);
        editExpense.setEnabled(false);
        dateField.setEnabled(false);
        expenseName.setEnabled(false);
        amount.setEnabled(false);
        categories.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int clickedElement = v.getId();
        switch (clickedElement) {
            case R.id.editDate:
            case R.id.editDateImg:  expenseDate.show();
                                    break;
            case R.id.editAttachment:Intent addPhoto = new Intent(EditExpense.this, SelectPhoto.class);
                                    startActivityForResult(addPhoto, MainActivity.GET_ATTACHMENT_CODE);
                                    break;
            case R.id.getExpenses:  AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setTitle(R.string.pick_exp_label)
                                            .setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, expenseNames), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    calendar.setEnabled(true);
                                                    attachment.setEnabled(true);
                                                    editExpense.setEnabled(true);
                                                    dateField.setEnabled(true);
                                                    expenseName.setEnabled(true);
                                                    amount.setEnabled(true);
                                                    categories.setEnabled(true);

                                                    indexOfIBE = which;
                                                    itemToBeEdited = expenses.get(which);
                                                    editExpName.setText(itemToBeEdited.expName);
                                                    Date startDate = itemToBeEdited.date;
                                                    try {
                                                        String dateInString = MainActivity.dateFormat.format(startDate);
                                                        dateField.setText(dateInString);
                                                        dateField.setTag(startDate);
                                                    } catch(Exception e) {}
                                                    categories.setSelection(categoryList.indexOf(itemToBeEdited.category));
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
            case R.id.saveEdit:
                                    try {
                                        if(expenseName.getText().toString().length()==0) {
                                            Toast.makeText(this, getString(R.string.warning_name), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        itemToBeEdited.expName = expenseName.getText().toString();
                                    } catch (Exception e) {
                                        Toast.makeText(this, getString(R.string.warning_name), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    try {
                                        selectedCategory = categoryList.get(SelectedCategoryIndex);
                                        if(selectedCategory.equals(getString(R.string.category_hint))) {
                                            Toast.makeText(this, getString(R.string.warning_category), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        else itemToBeEdited.category = selectedCategory;
                                    } catch (Exception e) {
                                        Toast.makeText(this, getString(R.string.warning_category), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    try {
                                        itemToBeEdited.amount = Double.parseDouble(amount.getText().toString());
                                    } catch (Exception e) {
                                        Toast.makeText(this, getString(R.string.warning_amount), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    try {   itemToBeEdited.date = (Date) dateField.getTag();  }
                                    catch (Exception e) {
                                        Toast.makeText(this, getString(R.string.warning_date), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Intent returnExpense = new Intent();
                                    returnExpense.putExtra(MainActivity.INDEX_INTENT_CODE, indexOfIBE);
                                    returnExpense.putExtra(MainActivity.ADDED_EXP, itemToBeEdited);
                                    setResult(RESULT_OK, returnExpense);
                                    finish();
                                    break;
            case R.id.cancelEdit:   setResult(RESULT_CANCELED);
                                    finish();
                                    break;
            default:                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case MainActivity.GET_ATTACHMENT_CODE:  if(resultCode == RESULT_OK) {
                                                        Bundle buffer = data.getExtras();
                                                        if(buffer.containsKey(AddExpense.IMAGE_URL_BUFFER_KEY)) {
                                                            uriOfSelectedImage = buffer.getString(AddExpense.IMAGE_URL_BUFFER_KEY);
                                                            ImageView imageSelected = (ImageView) findViewById(R.id.editAttachment);
                                                            imageSelected.setImageURI(Uri.parse(uriOfSelectedImage));
                                                            itemToBeEdited.image = uriOfSelectedImage;
                                                        }
                                                    }
                                                    else itemToBeEdited.image = null;
                                                    break;
            default:                                break;
        }
    }
}
