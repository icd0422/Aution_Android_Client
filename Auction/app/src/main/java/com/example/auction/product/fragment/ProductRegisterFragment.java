package com.example.auction.product.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.auction.AppHelper;
import com.example.auction.MainActivity;
import com.example.auction.R;
import com.example.auction.product.item.ImageInfo;
import com.example.auction.product.type.ProductStatus;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductRegisterFragment extends Fragment {

    private ImageView productImage;
    private Spinner categorySpinner;
    private EditText nameText, startPriceText, immediatePriceText, termText, descriptionText;
    private TextView endLineText;
    private Button endLineBtn, registerBtn ;

    private DatePickerDialog.OnDateSetListener dateCallbackMethod;

    private MainActivity parentActivity;

    private Bitmap productBitmap = null;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.product_register_fragment, container, false);

        productImage = (ImageView) rootView.findViewById(R.id.iv_product);
        categorySpinner = (Spinner) rootView.findViewById(R.id.sn_category);
        nameText = (EditText) rootView.findViewById(R.id.et__name);
        startPriceText = (EditText) rootView.findViewById(R.id.et_start_price);
        immediatePriceText = (EditText) rootView.findViewById(R.id.et_immediate_price);
        termText = (EditText) rootView.findViewById(R.id.et_term);
        endLineText = (TextView) rootView.findViewById(R.id.tv_end_line);
        endLineBtn = (Button) rootView.findViewById(R.id.btn_end_line);
        descriptionText = (EditText) rootView.findViewById(R.id.et_description);
        registerBtn = (Button) rootView.findViewById(R.id.btn_register);

        parentActivity = (MainActivity) getActivity();

        productBitmap = null ;

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.callGallery();
            }
        });

        endLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(parentActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(parentActivity, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endLineText.setText(String.format("%d-%02d-%02dT%02d:%02d:00", year, month + 1, dayOfMonth, hourOfDay, minute));
                            }
                        }, 0, 0, true);

                        timePickerDialog.show();
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                datePickerDialog.show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productBitmap != null) requestImgUpload();
            }
        });


        return rootView;
    }

    public static String get64BaseImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void requestImgUpload() {

        String URL = "https://api.imgur.com/3/image";

        RequestQueue requestQueue = Volley.newRequestQueue(parentActivity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_RESPONSE", response);

                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_ERROR_RESPONSE", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Client-ID 7866b1a561b1f93");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", get64BaseImage(productBitmap));
                return parameters;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void processResponse(String response) {
        Gson gson = new Gson();
        ImageInfo imageInfo = gson.fromJson(response, ImageInfo.class);

        if (AppHelper.DEBUG)
            Toast.makeText(parentActivity, imageInfo.getData().getLink(), Toast.LENGTH_LONG).show();

        requestProductRegister(imageInfo.getData().getLink());
    }

    public void setProdutImage(Bitmap bitmap) {
        productBitmap = bitmap;
        productImage.setImageBitmap(productBitmap);
    }

    void requestProductRegister(final String imgUrl){
        try {

            String URL = AppHelper.URL + "/product" ;

            RequestQueue requestQueue = Volley.newRequestQueue(parentActivity);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", nameText.getText().toString());
            jsonBody.put("description", descriptionText.getText().toString());
            jsonBody.put("category", categorySpinner.getSelectedItem().toString());
            jsonBody.put("startPrice", startPriceText.getText().toString());
            jsonBody.put("immediatePurchasePrice", immediatePriceText.getText().toString());
            jsonBody.put("status", ProductStatus.PRODUCT_ING.getState() +"");
            jsonBody.put("image", imgUrl);
            jsonBody.put("term", termText.getText().toString());
            jsonBody.put("endLine", endLineText.getText().toString());

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(parentActivity, "상품 등록 성공!", Toast.LENGTH_LONG).show();
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

