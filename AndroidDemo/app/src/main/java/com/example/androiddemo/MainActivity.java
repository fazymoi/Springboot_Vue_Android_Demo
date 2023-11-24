package com.example.androiddemo;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androiddemo.databinding.ActivityMainBinding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int signInId = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    private final Message message = new Message();
    private String MycreatePhoneCode;

    public static String session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


    public static boolean isValid(String phoneNumber) {
        String pattern = "^1[3456789]\\d{9}$";
        return phoneNumber.matches(pattern);
    }


    public void signInUp(View view) {
        String username = binding.name.getText().toString();
        String password = binding.password.getText().toString();

        if(isValid(username))
        {
            if(password.equals("")){
                handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "请填写验证码", Toast.LENGTH_SHORT).show()));
            }
            else{
                if(MycreatePhoneCode.equals(password))
                {
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("username",username);
                    Request request = new Request.Builder().url(NetworkSettings.SIGN_IN_UP2).post(
                            params.build()
                    ).build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Headers headers = response.headers();
                            List<String> cookies = headers.values("Set-Cookie");
                            String s = cookies.get(0);
                            session = s.substring(0, s.indexOf(";"));

                            if (response.isSuccessful()) {
                                ResponseBody body = response.body();
                                if (body != null) {
                                    Result result = mapper.readValue(body.string(), Result.class);
                                    message.what = result.getcode();
                                    if(message.what == 200){
                                        handler.post(()->{
                                            Intent intent = new Intent();
                                            //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                                            intent.setClass(MainActivity.this, SearchActivity.class);
                                            startActivity(intent);
//                                        finish();
                                        });
                                        handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
                                    }
                                }
                                else {
                                    message.what =401;
                                    Log.e("RESPONSE_BODY_EMPTY", response.message());
                                }
                            }
                            else {
                                message.what = 400;
                                Log.e("SERVER_ERROR", response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            message.what = 200;
                            handler.post(() -> handler.post(() -> Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show()));
                            e.printStackTrace();
                        }
                    });
                }
                else{
                    handler.post(()->Toast.makeText(getApplicationContext(), "验证码不正确", Toast.LENGTH_SHORT).show());
                }
            }

        }
        else{
            handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "账号格式错误", Toast.LENGTH_SHORT).show()));
        }

    }

//    public void signInUp0(View view) {
//        String username = binding.name.getText().toString();
//        String password = binding.password.getText().toString();
//        System.out.println(username);
//        FormBody.Builder params = new FormBody.Builder();
//        params.add("username",username);
//        params.add("password",password);
//        params.add("identity","普通用户");
//        Request request = new Request.Builder().url(NetworkSettings.SIGN_IN_UP).post(
//                params.build()
//        ).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                message.what = 200;
//                handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show()));
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                Headers headers = response.headers();
//
//                List<String> cookies = headers.values("Set-Cookie");
//                String s = cookies.get(0);
//                session = s.substring(0, s.indexOf(";"));
//
//                if (response.isSuccessful()) {
//                    ResponseBody body = response.body();
//                    if (body != null) {
//                        Result result = mapper.readValue(body.string(), Result.class);
//                        message.what = result.getcode();
//                        if(message.what == 200){
//                            handler.post(()->{
////                                binding.signInUp.setText("退出");
////                                Button button = (Button) findViewById(R.id.signInUp);
////                                System.out.println("111");
////                                //按钮进行监听
////                                button.setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View v) {
////                                        //监听按钮，如果点击，就跳转
////                                        System.out.println("!!!");
//                                        Intent intent = new Intent();
//                                        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
//                                        intent.setClass(MainActivity.this, StoreManage.class);
//                                        startActivity(intent);
//                                        finish();
////                                    }
////                                });
////                                oldName = binding.name.getText().toString();
////                                oldPassword = binding.password.getText().toString();
//                            });
//                        }
//                    } else {
//                        message.what =401;
//                        Log.e("RESPONSE_BODY_EMPTY", response.message());
//                    }
//                } else {
//                    message.what = 400;
//                    Log.e("SERVER_ERROR", response.message());
//                }
//                handler.post(()->Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show());
//            }
//        });
//    }

//    public void update(View view) {
//        try {
//            String name = binding.name.getText().toString();
//            String password = binding.password.getText().toString();
//            if(name.equals(oldName) && password.equals(oldPassword)){
//                message.what = ResponseCode.UNCHANGED_INFORMATION;
//                handler.post(()->Utils.showMessage(getApplicationContext(),message));
//                return;
//            }
//
//            Request request = new Request.Builder().url(NetworkSettings.UPDATE).put(
//                RequestBody.create(
//                    mapper.writeValueAsString(new User( name, password)),
//                    mediaType
//                )
//            ).build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    message.what = ResponseCode.REQUEST_FAILED;
//                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        ResponseBody body = response.body();
//                        if (body != null) {
//                            RestResponse restResponse = mapper.readValue(body.string(), RestResponse.class);
//                            message.what = restResponse.getCode();
//                            if(message.what == ResponseCode.UPDATE_SUCCESS){
//                                oldName = binding.name.getText().toString();
//                                oldPassword = binding.password.getText().toString();
//                            }
//                        } else {
//                            message.what = ResponseCode.EMPTY_RESPONSE;
//                            Log.e("RESPONSE_BODY_EMPTY", response.message());
//                        }
//                    } else {
//                        message.what = ResponseCode.SERVER_ERROR;
//                        Log.e("SERVER_ERROR", response.message());
//                    }
//                    handler.post(()->Utils.showMessage(getApplicationContext(),message));
//                }
//            });
//        } catch (Exception e) {
//            message.what = ResponseCode.JSON_SERIALIZATION;
//            Utils.showMessage(getApplicationContext(),message);
//            e.printStackTrace();
//        }
//    }
//

    public void toregister(View view) {

        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void tologin2(View view) {

        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public void toget(View view) {
        String username = binding.name.getText().toString();
        if(isValid(username))
        {
            int codeLength = 4;
            String createPhoneCode="";
            for (int i = 0; i < codeLength; i++) {
                int index = (int) Math.floor(Math.random() * (9));
                String iindex = valueOf(index);
                createPhoneCode += iindex;
            }
            MycreatePhoneCode=createPhoneCode;
            System.out.println("!!!!!!!!!"+MycreatePhoneCode);

            FormBody.Builder params = new FormBody.Builder();
            params.add("username",username);
            params.add("createPhoneCode",createPhoneCode);
            Request request = new Request.Builder().url(NetworkSettings.Get_Code).post(
                    params.build()
            ).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            Result result = mapper.readValue(body.string(), Result.class);
                            message.what = result.getcode();
                            if (message.what == 200) {
                                Log.e("RESPONSE_SUCCESS", response.message());
                            } else{
                                message.what =401;
                                Log.e("RESPONSE_BODY_EMPTY", response.message());
                            }
                        }
                    }
                    else {
                        message.what = 400;
                        Log.e("SERVER_ERROR", response.message());
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    message.what = 200;
                    handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show()));
                    e.printStackTrace();
                }
            });
        }
        else{
            handler.post(()->handler.post(()->Toast.makeText(getApplicationContext(), "账号格式错误", Toast.LENGTH_SHORT).show()));
        }

    }

}