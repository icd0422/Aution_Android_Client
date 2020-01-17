package com.example.auction.product.list;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.auction.AppHelper;
import com.example.auction.R;
import com.example.auction.dialog.BidDialog;
import com.example.auction.product.item.Product;
import com.example.auction.product.item.ProductBid;
import com.example.auction.product.type.ProductStatus;
import com.example.auction.user.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductItemView extends LinearLayout {
    private TextView sellerText, statucText, remainingTime, nameText, startPriceText, immediatePurchasePriceText, correntPriceText;
    private ImageView productImg;
    private Button immediateBuyBtn, bidBtn;
    private Context context;

    private RequestQueue requestQueue;

    private Product product;

    private ViewGroup root;

    public ProductItemView(Context context) {
        super(context);

        this.context = context;

        init();
    }

    public ProductItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = (ViewGroup) inflater.inflate(R.layout.product_item_view, this, true);

        sellerText = (TextView) findViewById(R.id.tv_seller);
        statucText = (TextView) findViewById(R.id.tv_status);
        remainingTime = (TextView) findViewById(R.id.tv_remaining_time);
        nameText = (TextView) findViewById(R.id.tv_name);
        startPriceText = (TextView) findViewById(R.id.tv_start_price);
        immediatePurchasePriceText = (TextView) findViewById(R.id.tv_immediate_purchase_price);
        correntPriceText = (TextView) findViewById(R.id.tv_corrent_price);
        productImg = (ImageView) findViewById(R.id.iv_product);
        immediateBuyBtn = (Button) findViewById(R.id.btn_immediate_buy);
        bidBtn = (Button) findViewById(R.id.btn_bid);

        immediateBuyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bidBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new BidDialog(context).callFunction(product.getId());
            }
        });
    }

    void setFromProduct(Product product, String token, int rq) {

        this.product = product;

        if (rq == AppHelper.PRODUCT_VIEW_ALL) {
            this.sellerText.setText(String.format("판매자 : %s", product.getUserName()));
            if (product.getUserEmail() != null)
                this.sellerText.setText(sellerText.getText().toString() + "(" + product.getUserEmail() + ")");
            else userRequest(product.getUserId(), token);
        } else {
            sellerText.setVisibility(View.INVISIBLE);
        }

        int status = product.getStatus();
        if (status == ProductStatus.PRODUCT_READY.getState()) {
            this.statucText.setText(ProductStatus.PRODUCT_READY.getDesc());
            this.statucText.setTextColor(ProductStatus.PRODUCT_READY.getColor());
        } else if (status == ProductStatus.PRODUCT_ING.getState()) {
            this.statucText.setText(ProductStatus.PRODUCT_ING.getDesc());
            this.statucText.setTextColor(ProductStatus.PRODUCT_ING.getColor());
        } else if (status >= ProductStatus.PRODUCT_COMPELETE.getState()) {
            this.statucText.setText(ProductStatus.PRODUCT_COMPELETE.getDesc());
            this.statucText.setTextColor(ProductStatus.PRODUCT_COMPELETE.getColor());
        }

        correntPriceText.setVisibility(View.VISIBLE);
        this.remainingTime.setText("남은 시간 : " + product.getEndTime());
        this.nameText.setText(product.getName());
        this.startPriceText.setText(String.format("시작가 : %,d", product.getStartPrice()));
        this.immediatePurchasePriceText.setText(String.format("즉시 구입가 : %,d", product.getImmediatePurchasePrice()));

        this.remainingTime.setVisibility(View.VISIBLE);

        requestProductImg();

        if (product.getStatus() >= ProductStatus.PRODUCT_COMPELETE.getState()) {
            remainingTime.setVisibility(View.INVISIBLE);
        } else if (product.getStatus() == ProductStatus.PRODUCT_READY.getState()) {
            correntPriceText.setVisibility(View.INVISIBLE);

            long endLineMs = product.getEndLine().getTime();
            long termMs = product.getTerm() * 86400000;

            long remainApproveMaxMs = endLineMs - termMs;

            Log.d("jjh", "endLineMs : " + endLineMs + ", termMs : " + termMs + ", remainApproveMaxMs : " + remainApproveMaxMs);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일(HH시 mm분)", Locale.KOREA);

            remainingTime.setText("최대 승인 날짜 : " + formatter.format(new Date(remainApproveMaxMs)) + "까지");

            remainingTime.setVisibility(View.INVISIBLE);

        } else if (product.getStatus() == ProductStatus.PRODUCT_ING.getState()) {

            long endTimeMs = product.getEndTime().getTime();
            long crruentMs = new Date().getTime();

            long ingRemainTimeMs = endTimeMs - crruentMs;

            if (AppHelper.DEBUG)
                Log.d("jjh", "endTimeMs : " + endTimeMs + ", crruentMs : " + crruentMs + ", ingRemainTimeMs : " + ingRemainTimeMs);

            long ingRemainTimeS = ingRemainTimeMs / 1000;

            long s = ingRemainTimeS % 60;
            long m = (ingRemainTimeS / 60) % 60;
            long h = ((ingRemainTimeS - ((m * 60) + s)) % 43200) / 3600;
            long d = ((ingRemainTimeS - ((m * 60) + s)) / 43200);
            d -= 1;

            if(d>0) remainingTime.setText(String.format("D - %d", d));
            else if(h>0) remainingTime.setText(String.format("%d H", h));
            else if(m>0) remainingTime.setText(String.format("%d M", m));

            if (product.getCurrentPrice() != 0) {
                if (product.getStatus() > 0)
                    this.correntPriceText.setText(String.format("낙찰가 : %,d", product.getCurrentPrice()));
                if (product.getStatus() == ProductStatus.PRODUCT_ING.getState())
                    this.correntPriceText.setText(String.format("현재 최고 입찰가 : %,d", product.getCurrentPrice()));
            } else bidRequest(product.getId(), token);
        }

        immediateBuyBtn.setVisibility(View.VISIBLE);
        bidBtn.setVisibility(View.VISIBLE);

        if (rq == AppHelper.PRODUCT_VIEW_ALL) {
            if (product.getStatus() > 0 || product.getStatus() == ProductStatus.PRODUCT_READY.getState()) {
                immediateBuyBtn.setVisibility(View.INVISIBLE);
                bidBtn.setVisibility(View.INVISIBLE);
            }
        } else {
            immediateBuyBtn.setVisibility(View.INVISIBLE);
            bidBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void requestProductImg() {
        String url = product.getImage();
        ImageLoadTask task = new ImageLoadTask(url, productImg);
        task.execute();
    }

    void bidRequest(int productId, final String token) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = AppHelper.URL + "/bid/" + productId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ProductBid productBid = gson.fromJson(response, ProductBid.class);
                        if (productBid == null) {
                            correntPriceText.setText("아직 입찰 내역이 없습니다.");
                            return;
                        }
                        product.setCurrentPrice(productBid.getCurrentPrice());
                        if (product.getStatus() > 0)
                            correntPriceText.setText(String.format("낙찰가 : %,d", product.getCurrentPrice()));
                        else if (product.getStatus() == ProductStatus.PRODUCT_READY.getState()) ;
                        correntPriceText.setText(String.format("현재 최고 입찰가 : %,d", product.getCurrentPrice()));
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
    }

    void userRequest(int userId, final String token) {

        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = AppHelper.URL + "/user/" + userId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);

                        if (user == null) return;
                        product.setUserEmail(user.getEmail());
                        sellerText.setText(sellerText.getText().toString() + "(" + product.getUserEmail() + ")");
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
    }

    static int test;

    public class IngRemainTimeTask extends AsyncTask<Void, Void, Boolean> {

        long s;
        long m;
        long h;
        long d;

        int t;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            t = test;

            Toast.makeText(context, "실행 : " + t, Toast.LENGTH_SHORT).show();

            test++;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {

                long endTimeMs = product.getEndTime().getTime();
                long crruentMs = new Date().getTime();

                long ingRemainTimeMs = endTimeMs - crruentMs;

                if (AppHelper.DEBUG)
                    Log.d("jjh", "endTimeMs : " + endTimeMs + ", crruentMs : " + crruentMs + ", ingRemainTimeMs : " + ingRemainTimeMs);

                long ingRemainTimeS = ingRemainTimeMs / 1000;

                s = ingRemainTimeS % 60;
                m = (ingRemainTimeS / 60) % 60;
                h = ((ingRemainTimeS - ((m * 60) + s)) % 43200) / 3600;
                d = ((ingRemainTimeS - ((m * 60) + s)) / 43200);
                d -= 1;

                publishProgress();

                Thread.sleep(1000);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            remainingTime.setText(String.format("남은 시간 : %d일 %d시간 %d분 %d초", d, h, m, s));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Toast.makeText(context, "종료 : " + t, Toast.LENGTH_SHORT).show();
        }
    }
}
