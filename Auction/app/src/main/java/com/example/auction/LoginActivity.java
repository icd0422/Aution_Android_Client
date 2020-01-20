package com.example.auction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView idText, passwordText;
    private Button loginBtn;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = new Handler();

        idText = (TextView) findViewById(R.id.tv_id);
        passwordText = (TextView) findViewById(R.id.tv_password);
        loginBtn = (Button) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new requestLogin().start();
            }
        });
    }

    class requestLogin extends Thread {
        @Override
        public void run() {
            super.run();

            HttpPostData();
        }
    }
        //////
    public void HttpPostData() {
        try {
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL(AppHelper.URL + "/user/login");       // URL 설정
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
            buffer.append("email").append("=").append(idText.getText().toString()).append("&");                 // php 변수에 값 대입
            buffer.append("password").append("=").append(passwordText.getText().toString());

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            //--------------------------
            //   서버에서 전송받기
            //--------------------------

            /*InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }

            final String bodyResponse = builder.toString();                       // 전송결과를 전역 변수에 저장*/

            final Map<String, List<String>> headerMap = http.getHeaderFields();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(headerMap.get("Authorization") != null){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("token", headerMap.get("Authorization").get(0));
                        intent.putExtra("email", idText.getText().toString());
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (MalformedURLException e) {
            Log.e("MalformedURLException", e.toString());
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        } // try
    } // HttpPostData
} // Activity






