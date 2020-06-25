package com.example.mechanic2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.models.Job;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JobAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {


    private ArrayList<Job> data;
    private final String server = "http://drkamal3.com/Mechanic/index.php?route=searchJob&search=";

    public JobAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.data = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position).getName();
    }

    private static class ViewHolder {

        private TextView textView;
        private TextView idTv;
        private LinearLayout parent;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NotNull ViewGroup parent) {

        ViewHolder mViewHolder;
        Job job;
        if (convertView == null) {
            mViewHolder = new ViewHolder();

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert vi != null;
            convertView = vi.inflate(R.layout.item_test, parent, false);
            mViewHolder.textView = convertView.findViewById(R.id.title);
            mViewHolder.idTv = convertView.findViewById(R.id.id);
            mViewHolder.parent = convertView.findViewById(R.id.parent);

            convertView.setTag(mViewHolder);
            //mViewHolder.textView.setTag(data.get(position));
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (position <= data.size() - 1)
            job = data.get(position);
        else job = new Job("", 0);

            mViewHolder.parent.setBackgroundColor(Application.getContext().getResources().getColor(R.color.blue_400));

        mViewHolder.textView.setText(job.getName());
        mViewHolder.idTv.setText(String.valueOf(job.getId()));


        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Job> suggestions = new ArrayList<>();
                suggestions.add(new Job(Application.getContext().getResources().getString(R.string.all_jobs), 0));
                if (constraint != null) {
                    HttpURLConnection conn = null;
                    InputStream input = null;
                    try {
                        URL url = new URL(server + constraint.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        input = conn.getInputStream();
                        InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                        BufferedReader buffer = new BufferedReader(reader, 8192);
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = buffer.readLine()) != null) {
                            builder.append(line);
                        }
                        JSONArray terms = new JSONArray(builder.toString());


                        for (int ind = 0; ind < terms.length(); ind++) {
                            JSONObject jsonObject = terms.getJSONObject(ind);
                            suggestions.add(new Job(jsonObject.getString("name"), jsonObject.getInt("id")));
                        }
                        results.values = suggestions;
                        results.count = suggestions.size();

                        data = suggestions;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (conn != null) conn.disconnect();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else notifyDataSetInvalidated();
            }
        };
    }
}