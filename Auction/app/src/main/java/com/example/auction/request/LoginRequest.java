package com.example.auction.request;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.auction.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

class LoginRequest {
    private Context context;

    private String email, password;

    private String url;

    private RequestQueue requestQueue;


    LoginRequest(Context context, String email, String password) {
        this.context = context;
        this.email = email;
        this.password = password;

        this.url = Url.LOGIN.getUrl();

        if (RQ.LOGIN.getRequestQueue() == null)
            RQ.LOGIN.setRequestQueue(Volley.newRequestQueue(context));
        requestQueue = RQ.LOGIN.getRequestQueue();
    }

    void request() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return super.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String token = response.headers.get("Authorization");

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("token", token);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(stringRequest);
    }
}
