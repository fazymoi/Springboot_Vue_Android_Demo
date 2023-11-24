package com.example.androiddemo;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.androiddemo.databinding.PersonalBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bouncycastle.util.Store;
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

public class PersonalActivity extends AppCompatActivity {

    private PersonalBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private static String session;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("initIIIIIIIIIIIIIIIIIIII");
        if (resultCode == 1) {
            //GET_CODE是自定义的页面跳转识别码
            System.out.println("initIIIIIIIIIIIIIIIIIIII");
            initview();
        }
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
        binding =PersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initview();
    }

    private void initview(){
        FormBody.Builder params = new FormBody.Builder();
        Request request = new Request.Builder().addHeader("cookie",MainActivity.session).url(NetworkSettings.Personal).post(
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
                        User user = mapper.readValue(body.string(), User.class);
                        System.out.println(user.getUsername());
                        String username=user.getUsername();

                        TextView name=(TextView) findViewById(R.id.username);
                        TextView nickname=(TextView) findViewById(R.id.nickname);
                        ImageView imageView = (ImageView) findViewById(R.id.userimg);
                        String realurl="";
                        Bitmap bmp;
                        if(user.getCover()!=null)
                        {
                            realurl=user.getCover().replace("localhost","172.20.10.2");
                            System.out.println(user.getCover());
                            bmp= getURLimage(realurl);
                        }
                        else{
                            bmp = null;
                        }
                        handler.post(()->{
                            name.setText("账号："+String.valueOf(user.getUsername()));
                            nickname.setText("昵称："+String.valueOf(user.getNickname()));
                            imageView.setImageBitmap(bmp);
                        });
                    }else {
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


    public void searchbutton(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(PersonalActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void personalbutton(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(PersonalActivity.this, PersonalActivity.class);
        startActivity(intent);
    }

    public void toinfo(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(PersonalActivity.this, PersonalInfo.class);
        startActivityForResult(intent,1);
    }

    public void tokeys(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(PersonalActivity.this, KeysActivity.class);
        startActivity(intent);
    }
    public void tocomment(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(PersonalActivity.this, CommentManage.class);
        startActivity(intent);
    }
    public void tomystore(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(PersonalActivity.this, MystoreActivity.class);
        startActivity(intent);
    }
    public void tocollection(View view){
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(PersonalActivity.this, CollectionManage.class);
        startActivity(intent);
    }

}