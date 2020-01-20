package com.example.auction.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auction.R;
import com.example.auction.request.VolleyRequest;

public class LoginActivity extends AppCompatActivity {

    private TextView emailText, passwordText;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (TextView) findViewById(R.id.tv_email);
        passwordText = (TextView) findViewById(R.id.tv_password);
        loginBtn = (Button) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyRequest.loginRquest(getApplicationContext(), emailText.getText().toString(), passwordText.getText().toString());
            }
        });
    }
}






