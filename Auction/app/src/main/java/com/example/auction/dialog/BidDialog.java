package com.example.auction.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.auction.AppHelper;
import com.example.auction.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BidDialog {

    private Context context;

    private Dialog dlg;

    private int pid;

    private EditText bidMoneyEdit;

    private Button confirmBtn, cancleBtn;


    public BidDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(int pid) {

        this.pid = pid;

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.bid_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        bidMoneyEdit = (EditText) dlg.findViewById(R.id.et_bid_money);

        confirmBtn = (Button) dlg.findViewById(R.id.btn_confirm);
        cancleBtn = (Button) dlg.findViewById(R.id.btn_cancle);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBid();
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }

    void requestBid() {
        try {

            String URL = AppHelper.URL + "/bid/update" ;

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", Integer.toString(pid));
            jsonBody.put("currentPrice", bidMoneyEdit.getText().toString());
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);

                    if(response.equals("-1")){
                        Toast.makeText(context, "입찰가가 현재 최고 입찰가보다 작거나 같습니다.", Toast.LENGTH_LONG).show();
                    }else if(response.equals("-2")){
                        Toast.makeText(context, "입찰가가 즉시 구입가보다 크거나 같습니다.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, "입찰 성공!", Toast.LENGTH_LONG).show();
                        AppHelper.PRODUCT_FRAGMENT_ALL.renew();
                        dlg.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_ERROR_RESPONSE", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", AppHelper.TOKEN);

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
