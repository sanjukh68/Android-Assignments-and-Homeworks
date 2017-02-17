package com.example.tvs.firebasedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    Context context;
    int resource;
    List<Expense> objects;

    public ExpenseAdapter(Context context, int resource, List<Expense> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        Expense expense = objects.get(position);
        ((TextView) convertView.findViewById(R.id.textViewName)).setText(expense.getExpName());
        ((TextView) convertView.findViewById(R.id.textViewValue)).setText("$ " + Double.toString(expense.getAmount()));

        return convertView;
    }
}
