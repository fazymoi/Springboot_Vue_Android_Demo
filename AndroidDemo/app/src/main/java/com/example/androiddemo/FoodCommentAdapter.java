package com.example.androiddemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lw on 2017/4/14.
 */

public class FoodCommentAdapter extends ArrayAdapter{
    private final int resourceId;

    public FoodCommentAdapter(Context context, int textViewResourceId, List<Comment> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = (Comment) getItem(position); // 获取当前项的Comment实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView content = (TextView) view.findViewById(R.id.content);//获取该布局内的图片视图
        TextView score = (TextView) view.findViewById(R.id.score);//获取该布局内的文本视图
        TextView nickname=(TextView) view.findViewById(R.id.nickname);
        TextView submittime=(TextView) view.findViewById(R.id.submittime);
        content.setText(comment.getContent());
        score.setText(comment.getScore()+"分");//为文本视图设置文本内容
        submittime.setText(comment.getSubmittime());
        if(comment.getNickname()==null)
            nickname.setText("匿名");
        else
            nickname.setText(comment.getNickname());
        return view;
    }
}