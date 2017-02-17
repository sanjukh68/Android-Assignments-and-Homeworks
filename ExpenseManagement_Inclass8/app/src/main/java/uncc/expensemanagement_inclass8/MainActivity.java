package uncc.expensemanagement_inclass8;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseFragment.OnFragmentInteractionListener, AddExpenseFragment.OnCancelListener, AddExpenseFragment.OnSaveListener, ExpenseAdapter.OnItemAddedListener,DisplayFragment.displayInterface{


    ArrayList<Expense> expenseList = new ArrayList<Expense>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction()
                .add(R.id.linearLayoutContainer, new ExpenseFragment(), "tag_expenses")
                .commit();
    }

    @Override
    public void gotoNextFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.linearLayoutContainer, new AddExpenseFragment(), "tag_add")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void deletedPosition(int pos) {
        expenseList.remove(pos);
        Toast.makeText(getApplicationContext(),"Item deleted successfully!",Toast.LENGTH_LONG).show();
    }
    int expensePosition;
    @Override
    public void selectedPosition(int pos) {
        expensePosition = pos;
        getFragmentManager().beginTransaction()
                .replace(R.id.linearLayoutContainer, new DisplayFragment(), "tag_display")
                .addToBackStack(null)
                .commit();

    }

    @Override public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void goBack() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void saveExpenses(Expense expense) {
        expenseList.add(expense);
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            ExpenseFragment expenseFragment = (ExpenseFragment)getFragmentManager().findFragmentByTag("tag_expenses");
            expenseFragment.FillList(expenseList);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void sendPosition(int position) {
    }

    @Override
    public Expense displayExpense() {
        return expenseList.get(expensePosition);
    }
}
