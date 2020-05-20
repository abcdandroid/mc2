package com.example.mechanic2.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.FilterListeners;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoodAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> data;
    private final String server = "http://drkamal3.com/Mechanic/index.php?route=searchGood&search=";
    private FilterListeners filterListeners;

    public GoodAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.data = new ArrayList<>();
    }

    public void setFilterListeners(FilterListeners filterFinishedListener)
    {
        filterListeners = filterFinishedListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
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

                        app.l(terms.toString());

                        ArrayList<String> suggestions = new ArrayList<>();
                        suggestions.add("همه");
                        //----


                        //----


                        for (int ind = 0; ind < terms.length(); ind++) {
                            String s=terms.getString(ind);
                            //JSONObject jsonObject=terms.getJSONObject(ind);
                           // suggestions.add(jsonObject.getString("name"));
                            suggestions.add(s);
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

                List<String> filterList = (ArrayList<String>) results.values;

                if (filterListeners != null && filterList!= null)
                    filterListeners.filteringFinished(filterList.size());

                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else notifyDataSetInvalidated();
            }
        };
    }
}