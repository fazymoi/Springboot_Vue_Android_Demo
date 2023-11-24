package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;


import com.example.androiddemo.databinding.PersonalBinding;
import com.example.androiddemo.databinding.SearchBinding;
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

public class SearchActivity extends AppCompatActivity {

    private SearchBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();

    private static String session;

    private RatingBar ratingBar;
    private List<Food> foods;
    private List<Store> stores;


    public void setstorefood(List<Food> storefoods) {
        this.foods = storefoods;
    }
    public List<Food> getstorefood() {
        return foods;
    }

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
        binding = SearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        FormBody.Builder params = new FormBody.Builder();
        params.add("searchfood", "111");
        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).
                url(NetworkSettings.All_Foods).post(
                        params.build()
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
                        List<Food> foods_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Food.class);
                        foods_ = mapper.readValue(body.string(), listType);
                        setstorefood(foods_);

                        handler.post(() -> {
                            StoreFoodAdapter adapter = new StoreFoodAdapter(SearchActivity.this, R.layout.fooditem, getstorefood());
                            ListView listView = (ListView) findViewById(R.id.list_sf);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    System.out.println(getstorefood().get(arg2).getFoodid());
                                    int foodid = getstorefood().get(arg2).getFoodid();
                                    setfoodid(foodid, arg2);
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
    }

    private void setfoodid(int foodid, int i) {
        FormBody.Builder params = new FormBody.Builder();
        params.add("foodid", String.valueOf(foodid));
        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.SETFOODID).post(
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
                                intent.setClass(SearchActivity.this, Foodpage.class);
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
                                intent.setClass(SearchActivity.this, StorepageActivity.class);
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

    public void searchfood(View view){
        String searchword = binding.searchword.getText().toString();

        FormBody.Builder params = new FormBody.Builder();
        params.add("searchfood",searchword);
        Request request = new Request.Builder().url(NetworkSettings.Search_Foods).post(
                params.build()
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
                        List<Food> foods_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Food.class);
                        foods_ = mapper.readValue(body.string(), listType);
                        setstorefood(foods_);

                        handler.post(() -> {
                            StoreFoodAdapter adapter = new StoreFoodAdapter(SearchActivity.this, R.layout.fooditem, getstorefood());
                            ListView listView = (ListView) findViewById(R.id.list_sf);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    System.out.println(getstorefood().get(arg2).getFoodid());
                                    int foodid = getstorefood().get(arg2).getFoodid();
                                    setfoodid(foodid, arg2);
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

    }


    public void searchstore(View view){
        String searchword = binding.searchword.getText().toString();

        FormBody.Builder params = new FormBody.Builder();
        params.add("searchstore",searchword);
        Request request = new Request.Builder().url(NetworkSettings.Search_Stores).post(
                params.build()
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
                            StoreAdapter adapter = new StoreAdapter(SearchActivity.this, R.layout.storeitem, getstore());
                            ListView listView = (ListView) findViewById(R.id.list_sf);
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
    }

    public void searchbutton(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(SearchActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void personalbutton(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(SearchActivity.this, PersonalActivity.class);
        startActivity(intent);
    }
}