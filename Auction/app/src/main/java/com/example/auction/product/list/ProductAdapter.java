package com.example.auction.product.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.auction.product.item.Product;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private ArrayList<Product> items = new ArrayList<Product>();

    private Context context;

    private String token;

    private int rq;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = null;
        this.items = items;
    }

    public void setToken(String token, int rq) {
        this.token = token;
        this.rq = rq;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ProductItemView productItemView = null;

        if (view == null) {
            productItemView = new ProductItemView(context);
        } else {
            productItemView = (ProductItemView) view;
        }

        Product item = items.get(i);
        productItemView.setFromProduct(item, token, rq);

        return productItemView;
    }


}
