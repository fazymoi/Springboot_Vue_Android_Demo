//package com.example.androiddemo;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class TitleLayout extends LinearLayout {
//    public TitleLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        //动态加载布局文件
//        LayoutInflater.from(context).inflate(R.layout.title, this);
//        Button titleBack = (Button) findViewById(R.id.titleButton);
//        TextView titletext=(TextView) findViewById(R.id.title_bar_name);
//        titleBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //结束当前Activity
//                Intent intent = new Intent();
//                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
//                intent.setClass(((Activity) getContext()), CommentManage.class);
//                ((Activity) getContext()).startActivity(intent);
//            }
//        });
//
//    }
//
//}