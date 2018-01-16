package com.example.android.shopbot;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Komal on 1/16/2018.
 */

public class StoreAdapter extends ArrayAdapter<Store> {

    public StoreAdapter(Activity context, ArrayList<Store> stores) {
        super(context,0, stores);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.store_list_item, parent, false);
        }

        Store currentStore = getItem(position);

        ImageView logoView = (ImageView) listItemView.findViewById(R.id.store_logo);
        String storeLogo = currentStore.getLogo();
        Picasso.with(getContext()).load(storeLogo).into(logoView);

        TextView priceTextView = (TextView) listItemView.findViewById(R.id.price);
        String productPrice = currentStore.getPrice();
        priceTextView.setText("Rs." + productPrice);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
    }

