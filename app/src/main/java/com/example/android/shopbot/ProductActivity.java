package com.example.android.shopbot;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Komal on 1/13/2018.
 */

public class ProductActivity extends AppCompatActivity {
    public static final String LOG_TAG = ProductActivity.class.getName();
    private StoreAdapter mAdapter;
    private TextView mEmptyStateTextView;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    ViewPager viewPager;
    MyCustomPagerAdapter myCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mAdapter = new StoreAdapter(this, new ArrayList<Store>());

        ListView storesList = (ListView) findViewById(R.id.stores_list);
        storesList.setAdapter(mAdapter);


        Bundle bundle = getIntent().getExtras();
        String productUrl = bundle.getString("productUrl");

        final String PRODUCT_REQUEST_URL = productUrl + "&api_key=Uj3KahNgL3owF7EtbGMy57926uJttmHFBU0";


        ProductInformationAsyncTask task_one = new ProductInformationAsyncTask();
        task_one.execute(PRODUCT_REQUEST_URL);

        ProductAsyncTask task_two = new ProductAsyncTask();
        task_two.execute(PRODUCT_REQUEST_URL);

        storesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current store that was clicked on
                Store currentStore = mAdapter.getItem(position);

                //Get currentItem's url
                Uri storeUri= Uri.parse(currentStore.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storeUri);
                // Send the intent to launch a new activity
                startActivity(websiteIntent);


            }
        });

    }

    private class ProductInformationAsyncTask extends AsyncTask<String, Void, ProductInformation>{

        @Override
        protected ProductInformation doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            ProductInformation productInformation= ProductInformationUtils.fetchStoreData(urls[0]);
            return productInformation;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(ProductInformation data) {

            TextView productNameTextView = (TextView) findViewById(R.id.product_name);
            String productName = data.getProductName();
            productNameTextView.setText(productName);
            /*
            RatingBar ratingView = (RatingBar) findViewById(R.id.rating_bar);
            float rating = data.getRating();
            ratingView.setRating(rating);
            */

            viewPager = (ViewPager)findViewById(R.id.viewPager);
            sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
            myCustomPagerAdapter = new MyCustomPagerAdapter(ProductActivity.this, data.getImages());
            viewPager.setAdapter(myCustomPagerAdapter);

            dotscount = myCustomPagerAdapter.getCount();
            dots = new ImageView[dotscount];

            for(int i = 0; i < dotscount; i++){

                dots[i] = new ImageView(ProductActivity.this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                sliderDotspanel.addView(dots[i], params);

            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for(int i = 0; i< dotscount; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

    }

    private class ProductAsyncTask extends AsyncTask<String, Void, List<Store>> {

        @Override
        protected List<Store> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Store> result = ProductUtils.fetchStoreData(urls[0]);
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Store> data) {

            mAdapter.clear();

            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}