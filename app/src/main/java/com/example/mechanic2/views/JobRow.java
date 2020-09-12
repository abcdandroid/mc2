package com.example.mechanic2.views;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.JobAutoCompleteAdapter;
import com.example.mechanic2.interfaces.AddJobClickListener;

public class JobRow extends LinearLayout {


    public void setRemoveFocus(boolean removeFocus) {
        this.removeFocus = removeFocus;
        if (removeFocus) jobTitle.clearFocus();
        invalidate();
    }

    private boolean removeFocus;

    public int getSelectedJobId() {
        return selectedJobId;
    }

    private int selectedJobId;
    private String selectedJobName;
    private String text;
    private String idJobRow;

    public void setIdJobRow(String idJobRow) {
        this.idJobRow = idJobRow;
        idHolder.setText(idJobRow);
        invalidate();
    }

    public String getIdJobRow() {
        return idHolder.getText().toString();
    }

    public void setText(String text) {
        this.text = text;
        jobTitle.setText(text);
        invalidate();
    }

    public String getText() {
        return jobTitle.getText().toString();
    }

    public String getSelectedJobName() {
        return selectedJobName;
    }

    public JobRow(Context context) {
        super(context);
        init(context, null);
    }

    public JobRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public JobRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private LinearLayout jobParent;
    private ImageView jobAddRemoveIcon;
    private AutoCompleteTextView jobTitle;
    public static AddJobClickListener addJobClickListener;

    private TextView idHolder;
    static int i;

    private void init(Context context, AttributeSet attrs) {

        View v = inflate(context, R.layout.view_job_row, this);

        jobParent = v.findViewById(R.id.job_parent);
        jobAddRemoveIcon = v.findViewById(R.id.job_add_remove_icon);
        jobTitle = v.findViewById(R.id.region_name);
        idHolder = v.findViewById(R.id.id_holder);

        i++;
        if (i != 1)
            jobTitle.requestFocus();

        if (removeFocus) jobTitle.clearFocus();


        JobAutoCompleteAdapter adapter = new JobAutoCompleteAdapter(context, R.layout.item, false);
        jobTitle.setAdapter(adapter);
        jobTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedJobId = Integer.parseInt(((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.id)).getText().toString());
                selectedJobName = ((TextView) parent.getAdapter().getView(position, view, ((ViewGroup) view.getParent())).findViewById(R.id.title)).getText().toString();
                idHolder.setText(String.valueOf(selectedJobId));

                Intent intent = new Intent("fromJobRow");
                intent.putExtra("jobId", idHolder.getText().toString());
                intent.putExtra("jobName", jobTitle.getText().toString());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        jobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idHolder.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jobAddRemoveIcon.setOnClickListener(v1 -> {
            if (addJobClickListener != null)
                addJobClickListener.onClick(v1);
        });

        invalidate();
    }

}
