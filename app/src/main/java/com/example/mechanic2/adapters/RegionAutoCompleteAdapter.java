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
import com.example.mechanic2.app.app;
import com.example.mechanic2.models.Region;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RegionAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {


    private ArrayList<Region> data;
    private final String server = getContext().getString(R.string.sRe);
    private boolean showAllRegion=true;

    public RegionAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        this.data = new ArrayList<>();
    }

    public RegionAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, boolean showAllRegion) {
        super(context, resource);

        this.data = new ArrayList<>();
        this.showAllRegion = showAllRegion;
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
        Region region;
        if (convertView == null) {
            mViewHolder = new ViewHolder();

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert vi != null;
            convertView = vi.inflate(R.layout.item_test, parent, false);
            mViewHolder.textView = convertView.findViewById(R.id.title);
            mViewHolder.idTv = convertView.findViewById(R.id.id);
            mViewHolder.parent = convertView.findViewById(R.id.parent);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (position <= data.size() - 1)
            region = data.get(position);
        else region = new Region("", 0);

        mViewHolder.parent.setBackgroundColor(Application.getContext().getResources().getColor(R.color.purple));

        mViewHolder.textView.setText(region.getName());
        mViewHolder.idTv.setText(String.valueOf(region.getId()));


        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Region> suggestions = new ArrayList<>();
                if (showAllRegion)
                    suggestions.add(new Region(Application.getContext().getResources().getString(R.string.all_regions), 0));
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
                            suggestions.add(new Region(jsonObject.getString("name"), jsonObject.getInt("id")));
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