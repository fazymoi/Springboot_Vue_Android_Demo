package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import android.view.View;
import com.example.androiddemo.databinding.UpdatecommentBinding;
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
public class UpdateComment extends AppCompatActivity {
    private UpdatecommentBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private RatingBar ratingBar;
    private final Message message = new Message();
    private int score;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    private Comment comment;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("33315");
        super.onCreate(savedInstanceState);
        binding = UpdatecommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        Comment temp=(Comment) bundle.getSerializable("comment");
        setComment(temp);
        System.out.println(temp.getContent());
        binding.content.setText(temp.getContent());
        binding.score.setRating((float) temp.getScore());
        setScore(temp.getScore());
        setContent(temp.getContent());

        ratingBar=findViewById(R.id.score);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                score=(int)v;
                System.out.println(score);
                setScore(score);
                ratingBar.setRating(score);
            }
        });

    }
    public void updatecomment(View view) throws JsonProcessingException {
        System.out.println("********updatecomment******");
        String content = binding.content.getText().toString();
        int s=getScore();
        System.out.println(score);
        String score=String.valueOf(s);
        FormBody.Builder params = new FormBody.Builder();
        params.add("score", score);
        params.add("content", content);
        params.add("commentid", String.valueOf(getComment().getCommentid()));

        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.UPDATECOMMENT).post(
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
                                Intent intent = new Intent();
                                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                intent.setClass(UpdateComment.this, CommentManage.class);
                                setResult(1,intent);
                                //startActivity(intent);
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
                handler.post(()->Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show());
            }
        });
    }

}



