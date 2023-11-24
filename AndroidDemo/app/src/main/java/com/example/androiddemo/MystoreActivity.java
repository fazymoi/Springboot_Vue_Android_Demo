package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View;

import com.example.androiddemo.databinding.FoodpageBinding;
import com.example.androiddemo.databinding.MystoreBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class MystoreActivity extends AppCompatActivity {

    private MystoreBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client2 = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();

    public static String session;

    private List<Store> stores;
    public void setstore(List<Store> stores) {
        this.stores = stores;
    }
    public List<Store> getstore() {
        return stores;
    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MystoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String username="11";
        String password="11";


        try {
            Request request = new Request.Builder().url(NetworkSettings.Mystore).addHeader("cookie",MainActivity.session).post(
                    RequestBody.create(mapper.writeValueAsString(new User(username, password)), mediaType)
            ).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    message.what = 200;
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            List<Store> stores_ = null;
                            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Store.class);
                            stores_ = mapper.readValue(body.string(), listType);
                            setstore(stores_);

                            handler.post(() -> {
                                StoreAdapter adapter = new StoreAdapter(MystoreActivity.this, R.layout.storeitem, getstore());
                                ListView listView = (ListView) findViewById(R.id.list_ms);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                        System.out.println(getstore().get(arg2).getStoreid());
                                        int storeid = getstore().get(arg2).getStoreid();
                                        setstoreid(storeid, arg2);
                                    }
                                });

                            });
                        }else {
                            message.what =401;
                            Log.e("RESPONSE_BODY_EMPTY", response.message());
                        }
                    }else {
                        message.what = 400;
                        Log.e("SERVER_ERROR", response.message());
                    }
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void setstoreid(int storeid, int i) {
        FormBody.Builder params = new FormBody.Builder();
        params.add("storeid", String.valueOf(storeid));
        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.Set_Storeid).post(
                params.build()
        ).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Result result = mapper.readValue(body.string(), Result.class);
                        message.what = result.getcode();
                        if (message.what == 200) {
                            handler.post(() -> {
                                Intent intent = new Intent();
                                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                intent.setClass(MystoreActivity.this, StorepageActivity.class);
                                startActivity(intent);
                                // finish();
                            });
                        }
                    } else {
                        message.what = 401;
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


