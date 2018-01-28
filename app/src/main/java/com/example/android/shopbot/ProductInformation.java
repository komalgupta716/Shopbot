package com.example.android.shopbot;
import java.util.ArrayList;

public class ProductInformation {
    private String product_name;
    private ArrayList<String> images;
    //private String rating;

    public ProductInformation()
    {}

    public ProductInformation(String pn, ArrayList<String> i){
        product_name=pn;
        images=i;
    }
    /*
    public ProductInformation(String pn, ArrayList<String> i, String r){
        product_name=pn;
        images=i;
        rating=r;
    }
    */

    public String getProductName(){return product_name; }
    public ArrayList<String> getImages(){return images;}
    /*
    public float getRating(){
        return Float.parseFloat(rating);
    }*/
}