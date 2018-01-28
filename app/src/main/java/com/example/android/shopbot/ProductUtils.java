package com.example.android.shopbot;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.android.shopbot.SearchResultActivity.LOG_TAG;

public class ProductUtils {
    private ProductUtils() {
    }

    public static List<Store> fetchStoreData(String requestUrl) {

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Store> stores = extractFeatureFromJson(jsonResponse);
        return stores;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url==null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else{
            }
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Store> extractFeatureFromJson(String productJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(productJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding products to
        List<Store> stores = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseJsonResponse = new JSONObject(productJSON);
            JSONObject productData = baseJsonResponse.getJSONObject("data");
            JSONArray storeArray= productData.getJSONArray("stores");
            boolean isComparable= productData.getBoolean("is_comparable");

            String name="", price="", url="", logo="";

            if(isComparable) {
                // For each store in the storeArray, create an object
                for (int i = 0; i < storeArray.length(); i++) {

                    // Get a single item at position i within the list of products
                    JSONObject currentIndex = storeArray.getJSONObject(i);

                    Iterator<String> keys = currentIndex.keys();
                    // get some_name_i_wont_know in str_Name
                    String storeName = keys.next();

                    Object intervention = currentIndex.get(storeName);

                    if (intervention instanceof JSONArray) {
                        // It's an array
                    } else if (intervention instanceof JSONObject) {
                        // It's an object
                        JSONObject currentStore = currentIndex.getJSONObject(storeName);
                        name = currentStore.getString("product_store");
                        price = currentStore.getString("product_price");
                        url = currentStore.getString("product_store_url");
                        logo = currentStore.getString("product_store_logo");

                        Store store = new Store(name, logo, price, url);
                        stores.add(store);
                    }
                }
            }

            else
            {
                JSONObject onlyStore= storeArray.getJSONObject(0);
                name = onlyStore.getString("product_store");
                price = onlyStore.getString("product_price");
                url = onlyStore.getString("product_store_url");
                logo = onlyStore.getString("product_store_logo");

                Store store = new Store(name, logo, price, url);
                stores.add(store);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the product JSON results", e);
        }
        return stores;
    }
}