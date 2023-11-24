package com.example.androiddemo;

import androidx.annotation.Nullable;
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
import android.widget.Toast;

import android.view.View;
import com.example.androiddemo.databinding.CommentmanageBinding;
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
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
public class CommentManage extends AppCompatActivity {
    private CommentmanageBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private List<Comment> mycomment;
    private ListView listView;
    private CommentAdapter adapter;

    public void setmycomment(List<Comment> mycomment) {
        this.mycomment = mycomment;
    }

    public List<Comment> getmycomment() {
        return mycomment;
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
        binding = CommentmanageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initview();

    }
    private void initview()
    {
        String name = "123";
        String password = "123";
        Request request = null;
        try {
            request = new Request.Builder().url(NetworkSettings.MYCOMMENT).addHeader("cookie", MainActivity.session).post(
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
                        List<Comment> mycomment_ = null;
                        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Comment.class);
                        mycomment_ = mapper.readValue(body.string(), listType);
                        // System.out.println(mycomment_.get(0).getNickname());
                        setmycomment(mycomment_);
                        handler.post(() -> {
                            adapter = new CommentAdapter(CommentManage.this, R.layout.commentitem, getmycomment());
                            listView = (ListView) findViewById(R.id.list_view);
                            listView.setAdapter(adapter);
                            ItemOnLongClick1();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    System.out.println(getmycomment().get(arg2).getFoodid());
                                    int foodid = getmycomment().get(arg2).getFoodid();
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
                                intent.setClass(CommentManage.this, Foodpage.class);
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

    private void ItemOnLongClick1() {
//注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
                menu.add(0, 1, 0, "更新");
            }
        });
    }

    // 长按菜单响应函数
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int position = (int) info.position;// 这里的info.id对应的就是数据库中_id的值
        Comment selected = (Comment) listView.getItemAtPosition(position);
        switch (item.getItemId()) {
            case 0:
                // 删除操作
                System.out.println(selected.getCommentid());
                FormBody.Builder params = new FormBody.Builder();
                params.add("commentid", String.valueOf(selected.getCommentid()));
                Request request = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.DELETECOMMENT).post(
                        params.build()
                ).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "!!!", Toast.LENGTH_SHORT).show()));
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
                                        handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show()));
                                        adapter.remove(selected);
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
                break;

            case 1:
                // 更新操作
                handler.post(() -> {
                    Intent intent = new Intent();
                    //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                    intent.setClass(CommentManage.this, UpdateComment.class);
                    Bundle b=new Bundle();
                    //数据对象
                    Comment temp=selected;
                    b.putSerializable("comment", (Serializable) temp);
                    //将打包好的bundle发送出去
                    intent.putExtras(b);
                    startActivityForResult(intent,1);
                    //finish();
                });
                break;

            default:
                break;
        }
    return false;
    }
}
