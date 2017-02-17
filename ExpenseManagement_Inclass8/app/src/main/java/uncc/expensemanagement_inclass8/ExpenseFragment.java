package uncc.expensemanagement_inclass8;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class ExpenseFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    ListView listViewexpenses;
    ExpenseAdapter adapter;
    ArrayList<Expense> expenses = new ArrayList<Expense>();
    public ExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.gotoNextFragment();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        expenses = new ArrayList<>();

        try{
            mListener = (OnFragmentInteractionListener)context;
        }catch(ClassCastException e){

        }
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*((ListView)getView().findViewById(R.id.listviewExpenses)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.deletedPosition(pos);
                return false;
            }
        });*/
        //final EditText editText = (EditText)getView().findViewById(R.id.editTextFragment);

        getView().findViewById(R.id.imageViewAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoNextFragment();
            }
        });

            listViewexpenses = (ListView) getView().findViewById(R.id.listviewExpenses);
            adapter = new ExpenseAdapter(getView().getContext(), R.layout.row_layout, expenses);
            listViewexpenses.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
           // checkList();
            listViewexpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mListener.selectedPosition(i);
                }
            });
        listViewexpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.deletedPosition(i);
                adapter.notifyDataSetChanged();
                return false;
            }
        });


        /*
        getActivity().findViewById(R.id.buttonClickMe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(getActivity(), "Button Pressed", Toast.LENGTH_SHORT).show();
        }*/
    }
    public void checkList()
    {
        if (expenses.size() > 0)
        {
            getView().findViewById(R.id.textViewEmpty).setVisibility(View.INVISIBLE);
        }
        else
        {
            getView().findViewById(R.id.textViewEmpty).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void gotoNextFragment();
        void deletedPosition(int pos);
        void selectedPosition(int pos);
    }

    public void FillList(ArrayList<Expense> expenses)
    {
        this.expenses = expenses;
        adapter.notifyDataSetChanged();
      //  checkList();
    }

    public void UpdateView(int position)
    {
        Expense expense = expenses.get(position);

    }
}
