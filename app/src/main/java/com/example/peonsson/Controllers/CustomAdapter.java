package com.example.peonsson.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Peonsson on 2015-11-21.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, ArrayList<String> currencies) {
        super(context, R.layout.custom_row, currencies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_row, parent, false);

        String singleCurrencyItem = getItem(position);

        TextView listViewItemViewText = (TextView) customView.findViewById(R.id.listViewItemViewText);
        ImageView myImg = (ImageView) customView.findViewById(R.id.myImg);

        listViewItemViewText.setText(singleCurrencyItem);
        myImg.setImageResource(R.mipmap.smile);

        return customView;
    }
}
