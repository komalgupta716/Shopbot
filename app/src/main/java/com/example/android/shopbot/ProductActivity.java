package com.example.android.shopbot;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

        ProductAsyncTask task = new ProductAsyncTask();
        task.execute(PRODUCT_REQUEST_URL);

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
