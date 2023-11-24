package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.androiddemo.databinding.ActivityMainBinding;
import com.example.androiddemo.databinding.CommentmanageBinding;
import com.example.androiddemo.databinding.FoodcommentBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

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

public class FoodComment extends AppCompatActivity{
    private FoodcommentBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private List<Comment> foodcomment;
    private RatingBar ratingBar;
    private int score;
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
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("**********foodcomment*******");
        super.onCreate(savedInstanceState);
        binding = FoodcommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FormBody.Builder params = new FormBody.Builder();

        params.add("storeid","111");
        Request request = new Request.Builder().addHeader("cookie",MainActivity.session).url(NetworkSettings.FOODCOMMENT).post(
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
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        List<Comment> foodcomment_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Comment.class);
                        foodcomment_=mapper.readValue(body.string(), listType);
                        // System.out.println(mycomment_.get(0).getNickname());
                        setfoodcomment(foodcomment_);
                        handler.post(()->{
                            FoodCommentAdapter adapter = new FoodCommentAdapter(FoodComment.this, R.layout.foodcommentitem,getfoodcomment());
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
                handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
            }
        });

        ratingBar=findViewById(R.id.score);
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
                               ratingBar.setRating(1);
                                binding.content.setText("");
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
                handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
            }
        });
    }

}

