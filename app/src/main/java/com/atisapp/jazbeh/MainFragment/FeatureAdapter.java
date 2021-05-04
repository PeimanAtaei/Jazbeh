package com.atisapp.jazbeh.MainFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.R;

import java.util.List;

public class FeatureAdapter extends BaseAdapter {

    private static final String TAG = "FeatureAdapter";
    private Context context;
    private List<String> featuresList;

    public FeatureAdapter(Context context, List<String> featuresList) {
        this.context = context;
        this.featuresList = featuresList;
    }

    @Override
    public int getCount() {
        return featuresList.size();
    }

    @Override
    public Object getItem(int position) {
        return featuresList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.update_features_layout, parent, false);
        }

        // get current item to be displayed
        String currentItem = (String) getItem(position);
        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.update_feature_text);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem);
        // returns the view for the current row
        return convertView;
    }
}
