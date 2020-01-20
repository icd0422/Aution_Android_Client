package com.example.auction.product.item;

import android.util.Log;

import com.android.volley.toolbox.Volley;
import com.example.auction.request.RQ;

import java.util.ArrayList;

public class ProductList {
    private ArrayList<Product> productList = new ArrayList<Product>();

    public ArrayList<Product> getProducts() {
        return productList;
    }

    public void print() {
        String temp = "";
        for (int i = 0; i < productList.size(); i++) {
            Log.d("jjh", productList.get(i).toString());


        }
    }
}
