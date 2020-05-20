package com.example.mechanic2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.AdminFragment;

import java.util.List;

public class MySpinnerAdapter extends ArrayAdapter<String> {
    private boolean isActive;
    private List<String> list;

    public MySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, boolean isActive) {
        super(context, resource, objects);
        this.isActive = isActive;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return createView(position, parent, isActive, false);
    }

    public void disableAdapter(boolean isActive) {
        this.isActive = isActive;
        notifyDataSetChanged();
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, parent, false, true);
    }

    private View createView(int position, @NonNull ViewGroup parent, boolean mode, boolean isDropdown) {
        LayoutInflater inflater = (LayoutInflater) Application.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_spinner, parent, false);
        TextView textView = view.findViewById(R.id.text1);


        textView.setText(list.get(position));
        if (!isDropdown) {
            textView.setPadding(8, 8, 8, 8);
            ((View) textView.getParent()).setBackgroundColor(Application.getContext().getResources().getColor(R.color.transparent));
        } else {
            textView.setPadding(41, 41, 41, 41);
            ((View) textView.getParent()).setBackgroundColor(Application.getContext().getResources().getColor(R.color.indigo_700));
        }

        if (!mode) {
            textView.setTextColor(Application.getContext().getResources().getColor(R.color.grey_10));
        } else
            textView.setTextColor(Application.getContext().getResources().getColor(R.color.grey_500));

        return view;
    }

}
