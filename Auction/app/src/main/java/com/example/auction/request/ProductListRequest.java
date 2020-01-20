package com.example.auction.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.auction.main.Tabs;
import com.example.auction.product.fragment.ProductFragment;
import com.example.auction.product.item.Product;
import com.example.auction.product.item.ProductList;
import com.example.auction.product.type.ProductStatus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductListRequest {

    private Context context;


    private int what;

    private String token, email;

    private String url;

    private RequestQueue requestQueue;


    ProductListRequest(Context context, int what, String token, String email) {
        this.context = context;
        this.what = what;
        this.token = token;
        this.email = email;

        if (this.what == Tabs.PRODUCT_FRAGMENT_ALL.getNum())
            this.url = Url.PRODUCT_LIST_ALL.getUrl();
        else if (this.what == Tabs.PRODUCT_FRAGMENT_MY.getNum())
            this.url = Url.PRODUCT_LIST_MY.getUrl();

        if (RQ.MAIN.getRequestQueue() == null)
            RQ.MAIN.setRequestQueue(Volley.newRequestQueue(context));

        requestQueue = RQ.MAIN.getRequestQueue();
    }

    void request() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);

                return params;
            }

            @Override
            public String getBodyContentType() {

                return super.getBodyContentType();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return super.getParams();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(stringRequest);
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        ProductList productList = gson.fromJson(response, ProductList.class);



        sendProductList(productList);
    }

    void sendProductList(ProductList productList) {

        if (what == Tabs.PRODUCT_FRAGMENT_ALL.getNum()) {
            int size = products.size();
            for (int i = 0; i < size; i++) {
                if (products.get(i).getStatus() >= ProductStatus.PRODUCT_COMPELETE.getState() || products.get(i).getUserEmail().equals(email)) {
                    products.remove(i);
                    i--;
                    size--;
                }
            }
        }

        ((ProductFragment)context).setProduct()
    }
}
