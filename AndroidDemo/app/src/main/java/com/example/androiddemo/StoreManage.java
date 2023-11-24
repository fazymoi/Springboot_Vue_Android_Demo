package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.androiddemo.databinding.StoremanageBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
public class StoreManage extends AppCompatActivity {
    private StoremanageBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("33315");
        super.onCreate(savedInstanceState);
        binding = StoremanageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String name = "123";
        String password = "123";
        FormBody.Builder params = new FormBody.Builder();

        params.add("password",password);
        params.add("identity","普通用户");
        Request request = null;
        try {
            request = new Request.Builder().url(NetworkSettings.Mystore).addHeader("cookie",MainActivity.session).post(
                    RequestBody.create(mapper.writeValueAsString(new User(name, password)), mediaType)
            ).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                message.what = 200;
                handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        List<Store> mystore_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Store.class);
                        List<Store> mystore=mapper.readValue(body.string(), listType);
                        System.out.println(mystore.get(0).getContact());
                    } else {
                        message.what =401;
                        Log.e("RESPONSE_BODY_EMPTY", response.message());
                    }
                } else {
                    message.what = 400;
                    Log.e("SERVER_ERROR", response.message());
                }
                handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
            }
        });

    }

    public void signInUp(View view) throws JsonProcessingException {

    }
}
