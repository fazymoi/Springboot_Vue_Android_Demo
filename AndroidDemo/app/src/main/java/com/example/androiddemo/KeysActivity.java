package com.example.androiddemo;

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

import com.example.androiddemo.databinding.KeysBinding;
import com.example.androiddemo.databinding.PersonalBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

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

public class KeysActivity extends AppCompatActivity {

    private KeysBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();

    private static String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = KeysBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void changekey(View view){
        String pwd1 = binding.nowpwd1.getText().toString();
        String pwd2 = binding.nowpwd2.getText().toString();

        if(pwd1.equals(pwd2)){
            FormBody.Builder params = new FormBody.Builder();
            params.add("password1",pwd1);
            params.add("password2",pwd2);
            Request request = new Request.Builder().addHeader("cookie", MainActivity.session).
                    url(NetworkSettings.Change_Key).post(
                            params.build()
                    ).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException{
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            Result result = mapper.readValue(body.string(), Result.class);
                            message.what = result.getcode();
                            if(message.what == 200){
                                handler.post(()->{
                                    Intent intent = new Intent();
                                    //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                    intent.setClass(KeysActivity.this, PersonalActivity.class);
                                    startActivity(intent);
//                                        finish();
                                });
                                handler.post(()->Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show());
                            }
                        }
                        else {
                            message.what =401;
                            Log.e("RESPONSE_BODY_EMPTY", response.message());
                        }
                    }
                    else {
                        message.what = 400;
                        Log.e("SERVER_ERROR", response.message());
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    message.what = 200;
                    handler.post(() -> handler.post(() -> Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show()));
                    e.printStackTrace();
                }

            });


        }
        else{
            handler.post(()->Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show());
        }
    }
}