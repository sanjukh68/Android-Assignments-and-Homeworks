package uncc.expensemanagement_inclass8;

/**
 * Created by sanju on 10/17/2016.
 */

/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;



import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sanju on 10/3/2016.
 */

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    List<Expense> mData;
    Context mContext;
    int mResource;
    OnItemAddedListener mListener;

    public ExpenseAdapter(Context context, int resource, List<Expense> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.textViewName);
        TextView value = (TextView) convertView.findViewById(R.id.textViewValue);

        Expense expense = mData.get(position);
        name.setText(expense.getExpName());
        value.setText(String.valueOf(expense.getAmount()));

        return convertView;
    }
    public interface OnItemAddedListener {
        // TODO: Update argument type and name
        void sendPosition(int position);
    }

}