package com.example.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.databinding.CollectionmanageBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
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

public class CollectionManage extends AppCompatActivity {
    private CollectionmanageBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private List<Collection> mycollection;
    private ListView listView;
    private CollectionAdapter adapter;

    public void setmycollection(List<Collection> mycollection) {
        this.mycollection = mycollection;
    }

    public List<Collection> getmycollection() {
        return mycollection;
    }
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("33315");
        super.onCreate(savedInstanceState);
        binding = CollectionmanageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initview();

    }
    private void initview()
    {
        String name = "123";
        String password = "123";
        Request request = null;
        try {
            request = new Request.Builder().url(NetworkSettings.MYCOLLECTION).addHeader("cookie", MainActivity.session).post(
                    RequestBody.create(mapper.writeValueAsString(new User(name, password)), mediaType)
            ).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                message.what = 200;
                // handler.post(() -> handler.post(() -> Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        List<Collection> mycollection_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Collection.class);
                        mycollection_ = mapper.readValue(body.string(), listType);
                        // System.out.println(mycollection_.get(0).getNickname());
                        setmycollection(mycollection_);
                        handler.post(() -> {
                            adapter = new CollectionAdapter(CollectionManage.this, R.layout.collectionitem, getmycollection());
                            listView = (ListView) findViewById(R.id.list_view);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    System.out.println(getmycollection().get(arg2).getFoodid());
                                    int foodid = getmycollection().get(arg2).getFoodid();
                                    setfoodid(foodid, arg2);
                                }
                            });
                        });

                    } else {
                        message.what = 401;
                        Log.e("RESPONSE_BODY_EMPTY", response.message());
                    }
                } else {
                    message.what = 400;
                    Log.e("SERVER_ERROR", response.message());
                }
                // handler.post(() -> Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
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
                                intent.setClass(CollectionManage.this, Foodpage.class);
                                startActivityForResult(intent,1);
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



}
