package com.example.mechanic2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.FilterListeners;
import com.example.mechanic2.models.Car;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CarAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {


    private ArrayList<Car> data;
    private final String server = "http://drkamal3.com/Mechanic/index.php?route=searchCar&lastId=0&search=";

    public CarAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
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

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NotNull ViewGroup parent) {

        ViewHolder mViewHolder;
        Car car;

        //data.add(new Car(Application.getContext().getResources().getString(R.string.all_cars), 0));
        app.l(position + "RRR");
        if (convertView == null) {
            mViewHolder = new ViewHolder();

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert vi != null;
            convertView = vi.inflate(R.layout.item_test, parent, false);
            mViewHolder.textView = convertView.findViewById(R.id.title);
            mViewHolder.idTv = convertView.findViewById(R.id.id);

            convertView.setTag(mViewHolder);
            //mViewHolder.textView.setTag(data.get(position));
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //car = (Car) mViewHolder.textView.getTag();
            car = data.get(position);
            mViewHolder.textView.setText(car.getName());
            mViewHolder.idTv.setText(String.valueOf(car.getId()));


        return convertView;
    }/* */

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Car> suggestions = new ArrayList<>();

                suggestions.add(new Car(Application.getContext().getResources().getString(R.string.all_cars), 0));
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
                            suggestions.add(new Car(jsonObject.getString("name"), Integer.parseInt(jsonObject.getString("id"))));
                        }
                        app.l(suggestions.size() + "QAZ");
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