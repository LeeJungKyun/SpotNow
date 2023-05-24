package com.example.spotnow.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spotnow.R;

import java.util.ArrayList;

public class user_listview_adapter extends BaseAdapter
{
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<user_listview_info> sample;

    public user_listview_adapter(Context context, ArrayList<user_listview_info> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public user_listview_info getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View coverView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.user_listview_format, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.poster);
        TextView movieName = (TextView)view.findViewById(R.id.movieName);
        TextView grade = (TextView)view.findViewById(R.id.grade);

        imageView.setImageResource(sample.get(position).getPoster());
        movieName.setText(sample.get(position).getName());
        grade.setText(sample.get(position).getIntroduce());

        return view;
    }
}
