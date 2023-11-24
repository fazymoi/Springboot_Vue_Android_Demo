package com.example.androiddemo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lw on 2017/4/14.
 */

public class CollectionAdapter extends ArrayAdapter{
    private final int resourceId;

    public CollectionAdapter(Context context, int textViewResourceId, List<Collection> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Collection collection = (Collection) getItem(position); // 获取当前项的Collection实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView storename = (TextView) view.findViewById(R.id.storename);//获取该布局内的图片视图
        TextView foodname=(TextView) view.findViewById(R.id.foodname);
        TextView submittime=(TextView) view.findViewById(R.id.submittime);
        storename.setText(collection.getStorename());//为图片视图设置图片资源
        foodname.setText(collection.getFoodname());
        submittime.setText(collection.getSubmittime());
        return view;
    }
}     