package uncc.expensemanagement_inclass8;


import android.app.Fragment;
import  android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment {
    displayInterface mListner;

    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            mListner = (displayInterface) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException("Implement interface in display");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Expense e = mListner.displayExpense();
        ((Button)getView().findViewById(R.id.buttonDisplayClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        Log.d("Sanju",""+e.toString());
        ((TextView)getView().findViewById(R.id.textViewDisplayName)).setText("Name : "+ e.getExpName());
        ((TextView)getView().findViewById(R.id.textViewDisplayAmount)).setText("Amount : "+e.getAmount());
        ((TextView)getView().findViewById(R.id.textViewDisplayCategory)).setText("Category : "+e.getCategory());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        ((TextView)getView().findViewById(R.id.textViewDisplayDate)).setText("Date : "+df.format(e.getDate()));
    }


    public interface displayInterface
    {
        public Expense displayExpense();
    }
}
