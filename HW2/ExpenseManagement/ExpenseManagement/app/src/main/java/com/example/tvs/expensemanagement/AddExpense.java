/*
Assignment No: Homework 2
File Name: AddExpense.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package com.example.tvs.expensemanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddExpense extends AppCompatActivity implements View.OnClickListener {
    public final static String IMAGE_URL_BUFFER_KEY = "imageUrl";
    public Expense newExp;
    public Calendar newCalendar = Calendar.getInstance();
    public int SelectedCategoryIndex;
    public ArrayList<String> categoryList = new ArrayList<>();

    public DatePickerDialog expenseDate;
    public EditText expenseName, date, amount;
    public String uriOfSelectedImage, selectedCategory;
    public Button addExpense;
    public Spinner categories;
    public ImageView calendar, attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        date = (EditText) findViewById(R.id.date);
        calendar = (ImageView) findViewById(R.id.calendar);
        attachment = (ImageView) findViewById(R.id.gallery);
        addExpense = (Button) findViewById(R.id.addExpBtn);
        categories = (Spinner) findViewById(R.id.category);

        date.setOnClickListener(this);
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        calendar.setOnClickListener(this);
        attachment.setOnClickListener(this);
        addExpense.setOnClickListener(this);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedCategoryIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        newExp = (Expense) getIntent().getExtras().get(MainActivity.NEW_EXP_OBJ);

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
                    date.setText(dateToDisplay);
                    Date temp = MainActivity.dateFormat.parse(dateToDisplay);
                    date.setTag(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        int clickedBtn = v.getId();
        switch (clickedBtn) {
            case R.id.date:
            case R.id.calendar: expenseDate.show();
                                break;
            case R.id.gallery:  Intent addPhoto = new Intent(AddExpense.this, SelectPhoto.class);
                                startActivityForResult(addPhoto, MainActivity.GET_ATTACHMENT_CODE);
                                break;
            case R.id.addExpBtn:expenseName = (EditText) findViewById(R.id.expName);
                                try {
                                    if(expenseName.getText().toString().trim().length()==0) {
                                        Toast.makeText(this, getString(R.string.warning_name), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    newExp.expName = expenseName.getText().toString().trim();
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
                                    else newExp.category = selectedCategory;
                                } catch (Exception e) {
                                    Toast.makeText(this, getString(R.string.warning_category), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                amount = (EditText) findViewById(R.id.amount);
                                try {
                                   newExp.amount = Double.parseDouble(amount.getText().toString().trim());
                                } catch (Exception e) {
                                    Toast.makeText(this, getString(R.string.warning_amount), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                try {
                                    newExp.date = (Date) date.getTag();
                                }
                                catch (Exception e) {
                                    Toast.makeText(this, getString(R.string.warning_date), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Intent returnExpense = new Intent();
                                returnExpense.putExtra(MainActivity.ADDED_EXP, newExp);
                                setResult(RESULT_OK, returnExpense);
                                finish();
                                break;
            default:            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case MainActivity.GET_ATTACHMENT_CODE:  if(resultCode == RESULT_OK) {
                                                        Bundle buffer = data.getExtras();
                                                        if(buffer.containsKey(IMAGE_URL_BUFFER_KEY)) {
                                                            uriOfSelectedImage = buffer.getString(IMAGE_URL_BUFFER_KEY);
                                                            ImageView imageSelected = (ImageView) findViewById(R.id.gallery);
                                                            imageSelected.setImageURI(Uri.parse(uriOfSelectedImage));
                                                            newExp.image = uriOfSelectedImage;
                                                        }
                                                    }
                                                    else newExp.image = null;
                                                    break;
            default:                                break;
        }
    }
}