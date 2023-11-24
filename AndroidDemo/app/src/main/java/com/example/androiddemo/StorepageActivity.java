package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.androiddemo.databinding.StorepageBinding;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
public class StorepageActivity extends AppCompatActivity {

    private StorepageBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client2 = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();

    public static String session;
    private List<Food> foods;

    public void setstorefood(List<Food> storefoods) {
        this.foods = storefoods;
    }
    public List<Food> getstorefood() {
        return foods;
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
        binding = StorepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FormBody.Builder params = new FormBody.Builder();
        params.add("storeid", "111");
        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).
                url(NetworkSettings.Show_Store).post(
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

                        Store store = mapper.readValue(body.string(), Store.class);
                        ImageView imageView = (ImageView) findViewById(R.id.storeimage);
                        TextView namev=(TextView) findViewById(R.id.storename);
                        TextView ohv=(TextView)findViewById(R.id.openinghours);
                        TextView saddrv=(TextView)findViewById(R.id.storeaddr);
                        TextView contactv=(TextView)findViewById(R.id.contact);
                        TextView instrov=(TextView)findViewById(R.id.introduction);

                        String realurl=store.getCover().replace("localhost","172.20.10.2");
                        System.out.println(store.getCover());
                        Bitmap bmp = getURLimage(realurl);

                        handler.post(()->{
                            namev.setText(store.getStorename());
                            ohv.setText("营业时间："+ store.getOpeninghours());
                            saddrv.setText("商店地址："+store.getStoreaddr());
                            contactv.setText("联系电话："+store.getContact());
                            instrov.setText("商店简介："+store.getIntroduction());

                            imageView.setImageBitmap(bmp);
                        });
                    } else {
                        message.what = 401;
                        Log.e("RESPONSE_BODY_EMPTY", response.message());
                    }

                }else {
                    message.what = 400;
                    Log.e("SERVER_ERROR", response.message());
                }
            }
        });

        FormBody.Builder params2 = new FormBody.Builder();
        params2.add("storeid","111");
        Request request2 = new Request.Builder().addHeader("cookie",MainActivity.session).url(NetworkSettings.Store_Foods).post(
                params2.build()
        ).build();

        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                message.what = 200;
                e.printStackTrace();
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        List<Food> foods_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Food.class);
                        foods_ = mapper.readValue(body.string(), listType);
                        setstorefood(foods_);

                        handler.post(() -> {
                            StoreFoodAdapter adapter = new StoreFoodAdapter(StorepageActivity.this, R.layout.fooditem, getstorefood());
                            ListView listView = (ListView) findViewById(R.id.store_foods);
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
                                intent.setClass(StorepageActivity.this, Foodpage.class);
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