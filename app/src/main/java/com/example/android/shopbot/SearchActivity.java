package com.example.android.shopbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button button=  (Button) findViewById(R.id.show_results);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int startingPrice, endingPrice;

                EditText productNameField= (EditText) findViewById(R.id.product_name_field);
                String productName= productNameField.getText().toString();

                EditText startingPriceField= (EditText) findViewById(R.id.starting_price);
                String minPrice = startingPriceField.getText().toString();
                if(minPrice.matches(""))
                    startingPrice=-1;
                else
                    startingPrice= Integer.parseInt(minPrice);

                EditText endingPriceField= (EditText) findViewById(R.id.ending_price);
                String maxPrice = endingPriceField.getText().toString();
                if(maxPrice.matches(""))
                    endingPrice=-1;
                else
                    endingPrice= Integer.parseInt(maxPrice);

                if(productName.matches("")){
                    Toast.makeText(getApplicationContext(), "Enter a product name to search", Toast.LENGTH_SHORT).show();
                }

                else if(startingPrice<-1 || endingPrice<-1)
                    Toast.makeText(getApplicationContext(), "Invalid Price Entered", Toast.LENGTH_SHORT).show();

                else if(endingPrice<startingPrice && endingPrice!=-1 && startingPrice!=-1)
                    Toast.makeText(getApplicationContext(), "Maximum Price should be greater than Minimum Price", Toast.LENGTH_SHORT).show();

                else {
                    Intent searchResultIntent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    searchResultIntent.putExtra("productName", productName);
                    searchResultIntent.putExtra("startingPrice", startingPrice);
                    searchResultIntent.putExtra("endingPrice", endingPrice);

                    // Send the intent to launch a new activity
                    startActivity(searchResultIntent);
                }

            }
        });

    }


}
