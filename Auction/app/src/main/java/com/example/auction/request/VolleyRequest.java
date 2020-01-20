package com.example.auction.request;

import android.content.Context;

public class VolleyRequest {

    static public void loginRquest(Context context, String email, String password) {
        new LoginRequest(context, email, password).request();
    }

    static public void productListRequest(Context context, int what, String token, String email) {
        new ProductListRequest(context, what, token, email).request();
    }

}
