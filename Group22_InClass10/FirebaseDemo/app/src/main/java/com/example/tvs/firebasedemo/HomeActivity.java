package com.example.tvs.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private TextView noListMessage;
    private ImageView imageViewAdd;
    private ExpenseAdapter adapter;
    private ListView listViewexpenses;
    private ArrayList<Expense> allExpenses = new ArrayList<>();

    private void logout() {
        firebaseAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listViewexpenses = (ListView) findViewById(R.id.listviewExpenses);
        noListMessage = (TextView) findViewById(R.id.textViewEmpty);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference().child(firebaseUser.getUid())
                .child(MainActivity.EXPENSES_TAG);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allExpenses = new ArrayList<>();
                ArrayList expenses = (ArrayList) dataSnapshot.getValue();
                if(null == expenses) {
                    noListMessage.setVisibility(View.VISIBLE);
                    listViewexpenses.setVisibility(View.INVISIBLE);
                    return;
                }
                listViewexpenses.setVisibility(View.VISIBLE);
                noListMessage.setVisibility(View.INVISIBLE);
                for(int i = 0; i < expenses.size(); i++) {
                    HashMap hashMap = (HashMap) expenses.get(i);
                    String expenseName = (String) hashMap.get("expName");
                    String category = (String) hashMap.get("category");
                    Double expense = Double.parseDouble(hashMap.get("amount").toString());
                    HashMap dateMap = (HashMap) hashMap.get("date");
                    Date date = new Date((Long) dateMap.get("time"));
                    allExpenses.add(new Expense(expenseName, category, expense, date));
                }
                adapter = new ExpenseAdapter(getApplicationContext(), R.layout.expense_list, allExpenses);
                listViewexpenses.setAdapter(adapter);
                adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewexpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showDetails = new Intent(getApplicationContext(), DetailsActivity.class);
                showDetails.putExtra(MainActivity.DETAILS_TAG, allExpenses.get(position));
                startActivity(showDetails);
            }
        });
        listViewexpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                allExpenses.remove(position);
                Map<String, ArrayList<Expense>> expenseTag = new HashMap<>();
                expenseTag.put(MainActivity.EXPENSES_TAG, allExpenses);
                databaseReference.setValue(allExpenses);
                adapter.notifyDataSetChanged();
                Toast.makeText(HomeActivity.this, "Expense deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        imageViewAdd = (ImageView) findViewById(R.id.imageViewAdd);
        imageViewAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageViewAdd: startActivity(new Intent(this, AddExpenseActivity.class));
                                    break;
        }
    }

    @Override
    public void onBackPressed() {
        logout();
        super.onBackPressed();
    }
}
