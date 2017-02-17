package com.example.tvs.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        (findViewById(R.id.closeDetails)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Expense expense = (Expense) getIntent().getSerializableExtra(MainActivity.DETAILS_TAG);
        ((TextView) findViewById(R.id.expNameD)).setText(expense.getExpName());
        ((TextView) findViewById(R.id.categoryNameD)).setText(expense.getCategory());
        ((TextView) findViewById(R.id.amountValueD)).setText(Double.toString(expense.getAmount()));
        ((TextView) findViewById(R.id.dateD))
                .setText(new SimpleDateFormat("dd MMM, yyyy").format(expense.getDate()));
    }
}
