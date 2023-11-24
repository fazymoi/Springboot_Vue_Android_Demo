package com.example.androiddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class StoreAdapter extends ArrayAdapter{
    private final int resourceId;

    public StoreAdapter(Context context, int textViewResourceId, List<Store> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        Store store = (Store) getItem(position); // 获取当前项的Food实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView storename = (TextView) view.findViewById(R.id.istorename);//获取该布局内的图片视图
        ImageView storeimg = (ImageView) view.findViewById(R.id.istoreimage);
        TextView storeaddr = (TextView) view.findViewById(R.id.istoreaddr);

        String realurl=store.getCover().replace("localhost","172.20.10.2");
        System.out.println(realurl);
        Bitmap bmp = getURLimage(realurl);

        storeimg.setImageBitmap(bmp);
        storename.setText(store.getStorename());
        storeaddr.setText(store.getStoreaddr());
        return view;
    }
}
