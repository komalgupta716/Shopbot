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
import java.util.List;

import static com.example.android.shopbot.MainActivity.LOG_TAG;

/**
 * Created by Komal on 1/3/2018.
 */

public class QueryUtils {

    private QueryUtils() {
    }

    public static List<Product> fetchProductData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Product> products = extractFeatureFromJson(jsonResponse);
        return products;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
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
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
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


    private static List<Product> extractFeatureFromJson(String productJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(productJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<Product> products = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseJsonResponse = new JSONObject(productJSON);
            JSONArray productArray = baseJsonResponse.getJSONArray("data");

            // For each product in the productArray, create an object
            for (int i = 0; i < productArray.length(); i++) {

                // Get a single item at position i within the list of products
                JSONObject currentItem = productArray.getJSONObject(i);

                String name = currentItem.getString("product_title");
                String price = currentItem.getString("product_lowest_price");
                String url = currentItem.getString("product_link");
                String image = currentItem.getString("product_image");

                Product product = new Product(name, price, url, image);
                products.add(product);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the product JSON results", e);
        }
        return products;
    }
}
