package com.example.android.shopbot;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Komal on 1/2/2018.
 */

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Activity context, ArrayList<Product> products) {
        super(context,0, products);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Product currentItem = getItem(position);

        //need to figure out how to add image from internet
        /*
        ImageView productImageView = (ImageView) listItemView.findViewById(R.id.product_image);
        String productName = currentItem.getName();
        nameTextView.setText(productName);
        */

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.product_name);
        String productName = currentItem.getName();
        nameTextView.setText(productName);

        TextView priceTextView = (TextView) listItemView.findViewById(R.id.product_price);
        String productPrice = currentItem.getPrice();
        priceTextView.setText(productPrice);


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
