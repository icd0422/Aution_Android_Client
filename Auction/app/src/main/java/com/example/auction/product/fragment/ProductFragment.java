package com.example.auction.product.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.auction.AppHelper;
import com.example.auction.MainActivity;
import com.example.auction.R;
import com.example.auction.product.item.Product;
import com.example.auction.product.item.ProductList;
import com.example.auction.product.list.ProductAdapter;
import com.example.auction.product.type.ProductStatus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductFragment extends Fragment {

    private ProductList productList;
    private ListView productListView;
    private ProductAdapter productAdapter;

    private RequestQueue requestQueue;

    private MainActivity parentActivity;

    private String token;

    private int rq  ;

    private String email ;

    public ProductFragment(int rq){
        this.rq = rq ;
    }

    public void setEmail(String email){
        this.email = email ;
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
        productAdapter.setToken(token, rq);

        requestQueue = Volley.newRequestQueue(parentActivity);

        productListRequest();

        return rootView;
    }

    public void renew(){
        productListRequest();
    }

    void productListRequest() {
        String url =  null ;
        if(rq == AppHelper.PRODUCT_VIEW_ALL) url = AppHelper.URL + "/product";
        else if(rq == AppHelper.PRODUCT_VIEW_MY) url = AppHelper.URL + "/user/my/product";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);

                        ArrayList<Product> products = productList.getProducts() ;

                        Log.d("jjh", products.toString());

                        if(rq==AppHelper.PRODUCT_VIEW_ALL){
                            int size = products.size();
                            for(int i=0 ; i<size ; i++){
                                if(products.get(i).getStatus() >= ProductStatus.PRODUCT_COMPELETE.getState() || products.get(i).getUserEmail().equals(email)){
                                    products.remove(i);
                                    i--;
                                    size--;
                                }
                            }
                        }

                        productAdapter.setItems(products);
                        productListView.setAdapter(null);
                        productListView.setAdapter(productAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        if (AppHelper.DEBUG)
            Toast.makeText(getActivity().getApplicationContext(), "productListRequest()", Toast.LENGTH_LONG).show();
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        productList = gson.fromJson(response, ProductList.class);
        if (AppHelper.DEBUG) productList.print();
    }
}
