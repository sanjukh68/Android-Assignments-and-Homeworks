package com.example.tvs.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner categories;
    private String[] spinnerData = {"Category", "Groceries", "Invoice", "Transportation",
            "Shopping", "Rent", "Trips", "Utilities", "Others"};
    private static int selectedCategoryIndex = 0;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Button buttonadd, cancel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ArrayList<Expense> allExpenses = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(MainActivity.EXPENSES_TAG);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("sanju","allExpense");
                allExpenses = new ArrayList<>();
                ArrayList expenses = (ArrayList) dataSnapshot.getValue();
                if(null == expenses) return;
                for(int i = 0; i < expenses.size(); i++) {
                    HashMap hashMap = (HashMap) expenses.get(i);
                    String expenseName = (String) hashMap.get("expName");
                    String category = (String) hashMap.get("category");
                    Double expense = Double.parseDouble(hashMap.get("amount").toString());
                    HashMap dateMap = (HashMap) hashMap.get("date");
                    Date date = new Date((Long) dateMap.get("time"));
                    allExpenses.add(new Expense(expenseName, category, expense, date));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        categories = (Spinner) findViewById(R.id.category);
        buttonadd = (Button) findViewById(R.id.buttonadd);
        cancel = (Button) findViewById(R.id.buttoncancel);

        buttonadd.setOnClickListener(this);
        cancel.setOnClickListener(this);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerData);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(categorySpinnerAdapter);
    }

    private boolean validateInput(String expenseName, int categoryIndex, String amount) {
        if(expenseName.equals(null) || categoryIndex==0 || amount.equals(null)) {
            Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addExpense() {

        Log.d("sanju","addExpense");
        String expenseName = ((TextView) findViewById(R.id.editTextName)).getText().toString();
        String amount = ((TextView) findViewById(R.id.amount)).getText().toString();
        if(validateInput(expenseName, selectedCategoryIndex, amount)) {
            Double amountValue = Double.parseDouble(amount);
            Expense e = new Expense(expenseName, spinnerData[selectedCategoryIndex], amountValue);
            Map<String, ArrayList<Expense>> expenseTag = new HashMap<>();
            allExpenses.add(e);
            expenseTag.put(MainActivity.EXPENSES_TAG, allExpenses);
            databaseReference.setValue(allExpenses);
            Toast.makeText(AddExpenseActivity.this, "Expense added", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonadd:    addExpense();
                                    break;
            case R.id.buttoncancel: finish();
                                    break;
        }
    }
}
