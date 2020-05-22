package com.example.mechanic2.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.AdminFragment;
import com.example.mechanic2.models.Country;
import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.List;

public class MySpinnerAdapter extends ArrayAdapter<String> {
    private boolean isActive;
    private List<String> list;
    private List<Integer> ids;
    //ImageView arrowSpinner;

    public MySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects,List<Integer> ids,boolean isActive) {
        super(context, resource, objects);
        this.isActive = isActive;
        this.list = objects;
        this.ids=ids;
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
        TextView id_spinner = view.findViewById(R.id.id_spinner);
        LinearLayout parentLayout = view.findViewById(R.id.parent);
        ImageView arrowSpinner = view.findViewById(R.id.arrow_spinner);

        textView.setText(list.get(position));
        id_spinner.setText(String.valueOf(ids.get(position)));

        if (!isDropdown) {
            arrowSpinner.setVisibility(View.VISIBLE);
            arrowSpinner.getLayoutParams().height = app.pxToDp(24);
            arrowSpinner.getLayoutParams().width = app.pxToDp(24);
            parentLayout.setGravity(Gravity.CENTER);
            textView.setPadding(8, 8, 8, 8);
            ((View) textView.getParent()).setBackgroundColor(Application.getContext().getResources().getColor(R.color.transparent));
        } else {
            arrowSpinner.getLayoutParams().height = app.pxToDp(24);
            arrowSpinner.getLayoutParams().width = app.pxToDp(24);
            arrowSpinner.setVisibility(View.INVISIBLE);
            parentLayout.setGravity(Gravity.RIGHT);
            textView.setPadding(41, 41, 41, 41);
            ((View) textView.getParent()).setBackgroundColor(Application.getContext().getResources().getColor(R.color.indigo_700));
        }

        if (!mode) {
            textView.setTextColor(Application.getContext().getResources().getColor(R.color.grey_10));
            arrowSpinner.setColorFilter(Application.getContext().getResources().getColor(R.color.grey_10
            ));
        } else {
            textView.setTextColor(Application.getContext().getResources().getColor(R.color.grey_500));
            arrowSpinner.setColorFilter(Application.getContext().getResources().getColor(R.color.grey_500));
        }

        return view;
    }



}
