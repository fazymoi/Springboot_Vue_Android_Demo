package com.example.androiddemo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.androiddemo.databinding.PersonalinfoBinding;
import com.example.androiddemo.databinding.RegisterBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.jetbrains.annotations.NotNull;

import java.io.File;
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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
public class PersonalInfo extends AppCompatActivity{
    private PersonalinfoBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private List<Comment> mycomment;
    private ListView listView;
    private CommentAdapter adapter;
    public static final int STORAGE_PERMISSION = 1;
    private File file=null;
    private String imgurl;
    private String username;

    public static final int CHOOSE_PHOTO = 1;//标识选择图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PersonalinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FormBody.Builder params = new FormBody.Builder();
        Request request = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.INFORMATION).post(
                params.build()
        ).build();
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
                        User user = mapper.readValue(body.string(), User.class);
                        String realurl="";
                        Bitmap bmp;
                        if(user.getCover()!=null)
                        {
                            realurl=user.getCover().replace("localhost","172.20.10.2");
                            System.out.println(user.getCover());
                            bmp= getURLimage(realurl);
                            imgurl=user.getCover();
                        }
                        else{
                            bmp = null;
                            imgurl="";
                        }
                        username=user.getUsername();
                            handler.post(() -> {
                                binding.nickname.setText(user.getNickname());
                                binding.mail.setText(user.getEaddress());
                                binding.ivImage.setImageBitmap(bmp);
                            });
                        }
                } else {
                message.what = 400;
                Log.e("SERVER_ERROR", response.message());
            }
        }
    });
    }

    public void chooseimg(View view) {
        if (ContextCompat.checkSelfPermission(PersonalInfo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalInfo.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        } else {
            xzImage();
        }
    }
    
    private void xzImage() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO); // 打开本地存储
    }

    public void uploadimg(View view)
    {
        if(file==null)
        {
            handler.post(() -> Toast.makeText(getApplicationContext(), "请选择图片", Toast.LENGTH_SHORT).show());
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//请求类型
                .addFormDataPart("a", file.getName(), RequestBody.create(MediaType.parse("*/*"), file)) // 第一个参数传到服务器的字段名，第二个你自己的文件名，第三个MediaType.parse("*/*")数据类型，这个是所有类型的意思,file就是我们之前创建的全局file，里面是创建的图片
                .build();
        //2、调用工具类上传图片以及参数
        HttpUtil.uploadFile(NetworkSettings.COVER, requestBody, new Callback() {
            //请求失败回调函数
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("=============");
                System.out.println("异常：：");
                e.printStackTrace();
            }
            //请求成功响应函数
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        myURL url= mapper.readValue(body.string(), myURL.class);
                        imgurl=url.getUrl();
                        System.out.println(imgurl);
                        handler.post(()-> {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("username", username);
                            params.add("cover", imgurl);
                            Request request = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.UPDATEIMG).post(
                                    params.build()
                            ).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    message.what = 200;
                                    // handler.post(() -> handler.post(() -> Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show()));
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    handler.post(() ->
                                            {
                                                Toast.makeText(getApplicationContext(), "上传头像成功", Toast.LENGTH_SHORT).show();
                                                setResult(1);
                                            }
                                    );
                                }
                            });
//
                        });
                    }
                } else {
                    message.what = 400;
                    Log.e("上传图片不得大于1mb", response.message());
                    handler.post(() -> Toast.makeText(getApplicationContext(), "上传图片不得大于1mb", Toast.LENGTH_SHORT).show());
                }

            }
        });

    }

    public void update(View view) {
        //api/updateinfo
        FormBody.Builder params2 = new FormBody.Builder();
            params2.add("nickname", String.valueOf(binding.nickname.getText()));
            params2.add("eaddress",String.valueOf(binding.mail.getText()));
            params2.add("cover",imgurl);
            Request request2 = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.UPDATEINFO).post(
                    params2.build()
            ).build();
            client.newCall(request2).enqueue(new Callback() {
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
                            String c=body.string();
                            if(c=="")
                                handler.post(() -> Toast.makeText(getApplicationContext(), "昵称和邮箱不能为空", Toast.LENGTH_SHORT).show());
                            else{
                                User user = mapper.readValue(c, User.class);
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
                                handler.post(() -> {
                                    binding.nickname.setText(user.getNickname());
                                    binding.mail.setText(user.getEaddress());
                                    binding.ivImage.setImageBitmap(bmp);
                                    setResult(1);
                                    handler.post(() -> Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show());
                                });
                            }
                        }
                    } else {
                        message.what = 400;
                        Log.e("SERVER_ERROR", response.message());
                    }
                }
            });

    }

    public void deleteuser(View view) {
        //api/updateinfo
        new AlertDialog.Builder(this).setTitle("确定注销用户？")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FormBody.Builder params2 = new FormBody.Builder();
                    Request request2 = new Request.Builder().addHeader("cookie", MainActivity.session).url(NetworkSettings.DELETEINFO).post(
                            params2.build()
                    ).build();
                    client.newCall(request2).enqueue(new Callback() {
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
                                Intent intent = new Intent();
                                intent.setClass(PersonalInfo.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                message.what = 400;
                                Log.e("SERVER_ERROR", response.message());
                            }
                            handler.post(() -> Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

    }

    //选择权限后的回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION:
                //检查是否有读取存储卡的权限，如果有则选择图片，如果没有则提示
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    xzImage();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CHOOSE_PHOTO://判断是不是选择照片后的操作
                //显示图片
                binding.ivImage.setImageURI(data.getData());
                System.out.println("图片在手机上的虚拟路径为："+data.getData());
                String realPath = Utils.getRealPath(this, data);
                System.out.println("图片在手机上的真实路径为："+realPath);
                file = new File(realPath);
                break;
            default:
                break;
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
}
