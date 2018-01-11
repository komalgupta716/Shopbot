package com.example.android.shopbot;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Komal on 1/2/2018.
 */

public class ShopbotLoader extends AsyncTaskLoader {
    private String mUrl;
    public ShopbotLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Product> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Product> result = QueryUtils.fetchProductData(mUrl);
        return result;
    }
}
