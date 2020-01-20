package com.example.auction.request;

import com.android.volley.RequestQueue;

public enum RQ {

    LOGIN,
    MAIN ;

    private RequestQueue requestQueue ;

    RQ(){
        this.requestQueue = null ;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }
}
