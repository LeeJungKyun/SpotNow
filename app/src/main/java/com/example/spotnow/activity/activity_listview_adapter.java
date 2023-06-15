package com.example.spotnow.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spotnow.R;

import java.util.ArrayList;

public class activity_listview_adapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<activity_listview_info> sample;

    // Constructor for the Adapter class
    public activity_listview_adapter(Context context, ArrayList<activity_listview_info> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // Return the number of items to be displayed in the ListView
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        // Return the ID of the item
        return position;
    }

    @Override
    public activity_listview_info getItem(int position) {
        // Return the item object
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Return the View of each item

        // Inflate the View from the layout file using a layout inflater
        View view = mLayoutInflater.inflate(R.layout.activity_listview_format, null);

        // Find the ImageView and TextView within the View object
        ImageView activityImage = (ImageView) view.findViewById(R.id.activity_image);
        TextView activityTitle = (TextView) view.findViewById(R.id.activity_title);
        TextView activityAddress = (TextView) view.findViewById(R.id.activity_address);

        // Set the ImageView to have a rounded shape
        activityImage.setClipToOutline(true);

        // Get the image URL of the item at the current position and load the image using Glide
        String imageUrl = sample.get(position).getActivityImage();
        Glide.with(view.getContext()).load(imageUrl)
                .apply(new RequestOptions().centerCrop())
                .into(activityImage);

        // Set the title and address of the item at the current position to the TextViews
        activityTitle.setText(sample.get(position).getActivityTitle());
        activityAddress.setText(sample.get(position).getActivityContent());

        return view;
    }
}
