package com.example.auction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private String token;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = getIntent().getStringExtra("token");
        email = getIntent().getStringExtra("email");
        AppHelper.TOKEN = token;

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("전체 상품"));
        tabs.addTab(tabs.newTab().setText("내가 올린 상품"));
        tabs.addTab(tabs.newTab().setText("나의 입찰 내역"));
        tabs.addTab(tabs.newTab().setText("상품 등록"));

        AppHelper.PRODUCT_FRAGMENT_ALL.setEmail(email);

        getSupportFragmentManager().beginTransaction().add(R.id.container, AppHelper.PRODUCT_FRAGMENT_ALL).commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selectedFragment = null;

                if (position == 0) {
                    selectedFragment = AppHelper.PRODUCT_FRAGMENT_ALL;
                } else if (position == 1) {
                    selectedFragment = AppHelper.PRODUCT_FRAGMENT_MY;
                } else if (position == 3) {
                    selectedFragment = AppHelper.PRODUCT_FRAGMENT_REGISTER;
                } else return;

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void callGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, AppHelper.GET_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppHelper.GET_IMAGE_REQUEST) {

            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());

                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        AppHelper.PRODUCT_FRAGMENT_REGISTER.setProdutImage(img);
                    } catch (Exception e) {

                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String getToken() {
        return token;
    }


}
