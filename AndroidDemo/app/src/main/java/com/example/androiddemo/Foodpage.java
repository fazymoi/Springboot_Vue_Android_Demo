package com.example.androiddemo;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View;

import com.example.androiddemo.databinding.FoodpageBinding;
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
public class Foodpage extends AppCompatActivity {

    private FoodpageBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client2 = new OkHttpClient();
    private final OkHttpClient client3 = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private List<Comment> foodcomment;
    private RatingBar ratingBar;
    private int score;
    private int storeid;
    public void setscore(int score) {
        this.score=score;
    }
    public int getscore() {
        return score;
    }
    public void setfoodcomment(List<Comment> mycomment) {
        this.foodcomment = mycomment;
    }
    public List<Comment> getfoodcomment() {
        return foodcomment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FoodpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //TextView titletext=(TextView) findViewById(R.id.title_bar_name);
        //handler.post(()->{titletext.setText("美食主页");});
        FormBody.Builder params = new FormBody.Builder();
        params.add("storeid", "111");
        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).
                url(NetworkSettings.SHOWFOOD).post(
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
                        Food food = mapper.readValue(body.string(), Food.class);
                        storeid=food.getStoreid();
                        ImageView imageView = (ImageView) findViewById(R.id.foodimage);
                        TextView scorev=(TextView) findViewById(R.id.score);
                        TextView tagv=(TextView)findViewById(R.id.tag);
                        TextView ingredientv=(TextView)findViewById(R.id.ingredient);
                        TextView pricev=(TextView)findViewById(R.id.price);
                        TextView namev=(TextView)findViewById(R.id.foodname);
                        String realurl=food.getCover().replace("localhost","172.20.10.2");
                        System.out.println(food.getCover());
                        Bitmap bmp = getURLimage(realurl);

                        handler.post(()->{
                            scorev.setText("评分："+String.valueOf(food.getScore()));
                            pricev.setText("价格："+String.valueOf(food.getPrice()));
                            ingredientv.setText("原料："+food.getIngredient());
                            tagv.setText("标签："+food.getTag());
                            namev.setText(food.getFoodname());
                            System.out.println(tagv.getText());
                            imageView.setImageBitmap(bmp);
                        });

                    } else {
                        message.what = 401;
                        Log.e("RESPONSE_BODY_EMPTY", response.message());
                    }
                } else {
                    message.what = 400;
                    Log.e("SERVER_ERROR", response.message());
                }
                //handler.post(() -> Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
            }
        });

        //******************************

        FormBody.Builder params2 = new FormBody.Builder();
        params2.add("storeid","111");
        Request request2 = new Request.Builder().addHeader("cookie",MainActivity.session).url(NetworkSettings.FOODCOMMENT).post(
                params2.build()
        ).build();
        client2.newCall(request2).enqueue(new Callback() {
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
                        List<Comment> foodcomment_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Comment.class);
                        foodcomment_=mapper.readValue(body.string(), listType);

                        // System.out.println(mycomment_.get(0).getNickname());
                        setfoodcomment(foodcomment_);
                        handler.post(()->{
                            FoodCommentAdapter adapter = new FoodCommentAdapter(Foodpage.this, R.layout.foodcommentitem,getfoodcomment());
                            ListView listView = (ListView) findViewById(R.id.list_view);
                            listView.setAdapter(adapter);
//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//                                    System.out.println(getmycomment().get(arg2).getFoodid());
//                                    int foodid=getmycomment().get(arg2).getFoodid();
//                                    setfoodid(foodid,arg2);
//                                }
//                            });
                        });

                    } else {
                        message.what =401;
                        Log.e("RESPONSE_BODY_EMPTY", response.message());
                    }
                } else {
                    message.what = 400;
                    Log.e("SERVER_ERROR", response.message());
                }
                // handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
            }
        });

//***************************

        FormBody.Builder params3 = new FormBody.Builder();
        Request request3 = new Request.Builder().addHeader("cookie",MainActivity.session).url(NetworkSettings.ISCOLLEEXIST).post(
                params3.build()
        ).build();
        client3.newCall(request3).enqueue(new Callback() {
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
                        String c=body.string();
                        handler.post(()->{
                            if(c=="")
                                binding.collection.setText("收藏美食");

                            else
                                binding.collection.setText("取消收藏");
                        });

                    } else {
                        message.what =401;
                        Log.e("RESPONSE_BODY_EMPTY", response.message());
                    }
                } else {
                    message.what = 400;
                    Log.e("SERVER_ERROR", response.message());
                }
                // handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
            }
        });

        ratingBar=findViewById(R.id.myscore);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                score=(int)v;
                System.out.println(score);
                setscore(score);
                ratingBar.setRating(score);
            }
        });

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
    public void foodcomment(View view){
        System.out.println("foodcomment");
        handler.post(()->{
            Intent intent = new Intent();
            //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
            intent.setClass(Foodpage.this, FoodComment.class);
            startActivity(intent);
            // finish();
        });
    }

    public void addcomment(View view) {
        System.out.println("********addcomment******");
        String content = binding.content.getText().toString();
        int score=getscore();
        System.out.println(score);
        FormBody.Builder params = new FormBody.Builder();
        params.add("content",content);
        params.add("score", String.valueOf(score));
        Request request = new Request.Builder().addHeader("cookie",MainActivity.session).url(NetworkSettings.ADDCOMMENT).post(
                params.build()
        ).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                message.what = 200;
                //handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "评论失败", Toast.LENGTH_SHORT).show()));
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Result result = mapper.readValue(body.string(), Result.class);
                        message.what = result.getcode();
                        if(message.what == 200){
                            handler.post(()->{
                                ratingBar.setRating(0);
                                binding.content.setText("");
                                Intent intent = new Intent();
                                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                intent.setClass(Foodpage.this, Foodpage.class);
                                startActivity(intent);
                                finish();
                            });
                        }
                        else{

                            handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show()));
                        }
                    }else {
                        message.what =401;
                        Log.e("RESPONSE_BODY_EMPTY", response.message());
                    }
                } else {
                    message.what = 400;
                    Log.e("SERVER_ERROR", response.message());
                }
               // handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void tostore(View view) {
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
                                intent.setClass(Foodpage.this, StorepageActivity.class);
                                startActivity(intent);
                                //finish();
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


    public void changecollection(View view) {
        String former= (String) binding.collection.getText();
        FormBody.Builder params = new FormBody.Builder();
        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.CHANGECOLLECTION).post(
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
                                if(former=="取消收藏")
                                {
                                    binding.collection.setText("收藏美食");
                                    Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                    intent.setClass(Foodpage.this, CollectionManage.class);
                                    setResult(1,intent);
                                }
                                else
                                {
                                    binding.collection.setText("取消收藏");
                                    Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                    intent.setClass(Foodpage.this, CollectionManage.class);
                                    setResult(1,intent);
                                }
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