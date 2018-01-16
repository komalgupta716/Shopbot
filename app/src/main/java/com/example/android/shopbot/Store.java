package com.example.android.shopbot;

/**
 * Created by Komal on 1/16/2018.
 */

public class Store {
    private String name;
    private String logo;
    private String price;
    private String url;

    public Store(String n, String l, String p, String u){
        name = n;
        logo = l;
        price = p;
        url = u;
    }

    public String getName(){return name;}
    public String getLogo(){return logo;}
    public String getPrice(){return price;}
    public String getUrl(){return url;}
}
