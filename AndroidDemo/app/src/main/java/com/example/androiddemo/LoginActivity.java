package com.example.androiddemo;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.androiddemo.databinding.ActivityMainBinding;
import com.example.androiddemo.databinding.LoginBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bouncycastle.util.Store;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    private LoginBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private String MycreatePhoneCode;

    public static String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public static boolean isValid(String phoneNumber) {
        String pattern = "^1[3456789]\\d{9}$";
        return phoneNumber.matches(pattern);
    }

    public void signInUp0(View view) {
        String username = binding.name0.getText().toString();
        String password = binding.password0.getText().toString();
        System.out.println(username);
        System.out.println(password);

        if(isValid(username))
        {
            if(password.equals("")){
                handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "请填写密码", Toast.LENGTH_SHORT).show()));
            }
            else{
                FormBody.Builder params = new FormBody.Builder();
                params.add("username",username);
                params.add("password",password);
                params.add("identity","普通用户");
                Request request = new Request.Builder().url(NetworkSettings.SIGN_IN_UP).post(
                        params.build()
                ).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        message.what = 200;
                        handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show()));
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Headers headers = response.headers();
                        List<String> cookies = headers.values("Set-Cookie");
                        String s = cookies.get(0);
                        session = s.substring(0, s.indexOf(";"));

                        MainActivity.session=session;

                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            if (body != null) {
                                Result result = mapper.readValue(body.string(), Result.class);
                                message.what = result.getcode();
                                if(message.what == 200){
                                    handler.post(()->{
//
                                        Intent intent = new Intent();
                                        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                        intent.setClass(LoginActivity.this, SearchActivity.class);
                                        startActivity(intent);
//
                                    });
                                    handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
                                }

                            } else {
                                message.what =401;
                                Log.e("RESPONSE_BODY_EMPTY", response.message());
                            }
                        } else {
                            message.what = 400;
                            Log.e("SERVER_ERROR", response.message());
                        }
                    }
                });
            }

        }
        else{
            handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "账号格式错误", Toast.LENGTH_SHORT).show()));
        }
    }
    public void toregister(View view) {

        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();

    }

    public void tologin1(View view) {

        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


}