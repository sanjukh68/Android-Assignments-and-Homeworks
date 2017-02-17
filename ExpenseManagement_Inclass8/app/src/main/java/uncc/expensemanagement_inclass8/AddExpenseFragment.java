package uncc.expensemanagement_inclass8;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;



public class AddExpenseFragment extends Fragment {

    private OnCancelListener mListener;
    private OnSaveListener mListenerSave;
    Button addExpense;
    Spinner categories;
    public ArrayList<String> categoryList = new ArrayList<>();
    public int SelectedCategoryIndex;
    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        categoryList.add(getString(R.string.category_hint));
        categoryList.add(getString(R.string.category_groceries));
        categoryList.add(getString(R.string.category_invoice));
        categoryList.add(getString(R.string.category_transportation));
        categoryList.add(getString(R.string.category_shopping));
        categoryList.add(getString(R.string.category_rent));
        categoryList.add(getString(R.string.category_trips));
        categoryList.add(getString(R.string.category_utils));
        categoryList.add(getString(R.string.category_others));

        categories = (Spinner) getView().findViewById(R.id.category);

        //addExpense.setOnClickListener(this);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedCategoryIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_spinner_item, categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(categorySpinnerAdapter);

        getView().findViewById(R.id.buttonadd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) getView().findViewById(R.id.editTextName);
                String name = editText.getText().toString().trim();
                String category = categoryList.get(SelectedCategoryIndex);

                EditText editTextAmount = (EditText) getView().findViewById(R.id.amount);;
                String amount = editTextAmount.getText().toString().trim();

/*
                try {
                    if(name.trim().length()==0) {
                        Toast.makeText(getView().getContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(getView().getContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    String selectedCategory = categoryList.get(SelectedCategoryIndex);
                    if(selectedCategory.equals(getString(R.string.category_hint))) {
                        Toast.makeText(getView().getContext(), "category cannot be empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(getView().getContext(), "category cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    if (amount.length() == 0)
                    {
                        Toast.makeText(getView().getContext(), "amount cannot be empty", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getView().getContext(), "amount cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
*/

                Expense expense = new Expense(name, category, Double.parseDouble(amount));
                mListenerSave.saveExpenses(expense);
            }
        });

        getView().findViewById(R.id.buttoncancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goBack();
            }
        });
        //final EditText editText = (EditText)getView().findViewById(R.id.editTextFragment);

//        getView().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
 //           @Override
 //           public void onClick(View view) {
//                mListener.onFragmentTextchanged(editText.getText().toString());
//            }
//        });
        /*
        getActivity().findViewById(R.id.buttonClickMe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(getActivity(), "Button Pressed", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCancelListener) {
            mListener = (OnCancelListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCancelListener");
        }

        if (context instanceof OnSaveListener) {
            mListenerSave = (OnSaveListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSaveListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCancelListener {
        // TODO: Update argument type and name
        void goBack();
    }

    public interface OnSaveListener {
        // TODO: Update argument type and name
        void saveExpenses(Expense expense);
    }
}
