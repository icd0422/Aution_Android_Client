package com.example.auction.product.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.auction.AppHelper;
import com.example.auction.R;
import com.example.auction.main.MainActivity;
import com.example.auction.product.item.ProductList;
import com.example.auction.product.list.ProductAdapter;
import com.example.auction.request.VolleyRequest;

public class ProductFragment extends Fragment {

    private ProductList productList;
    private ListView productListView;
    private ProductAdapter productAdapter;

    private MainActivity parentActivity;

    private String token;

    private int what;

    private String email;

    public ProductFragment(int what) {
        this.what = what;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.product_fragment, container, false);

        parentActivity = (MainActivity) getActivity();
        token = parentActivity.getToken();

        if (AppHelper.DEBUG) Toast.makeText(parentActivity, token, Toast.LENGTH_LONG).show();

        productListView = (ListView) rootView.findViewById(R.id.lv_products);
        productAdapter = new ProductAdapter(parentActivity);
        productAdapter.setToken(token, );

        VolleyRequest.productListRequest(parentActivity, what, token, email);



        return rootView;
    }

    public void refreshProductListView(ProductList productList){

        productAdapter.setItems(productList.getProducts());
        productListView.setAdapter(null);
        productListView.setAdapter(productAdapter);

    }

    public void renew() {
        VolleyRequest.productListRequest(parentActivity, what, token, email);
    }


}
