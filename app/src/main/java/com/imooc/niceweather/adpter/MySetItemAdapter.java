package com.imooc.niceweather.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.niceweather.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by xhx12366 on 2017-09-29.
 */

public class MySetItemAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;

    public MySetItemAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Map<String, Object> item;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_set, null);
            holder.itemIcon = (ImageView) convertView.findViewById(R.id.image_item_icon);
            holder.itemName = (TextView) convertView.findViewById(R.id.text_item_name);
            holder.itemChoosed = (TextView) convertView.findViewById(R.id.text_item_choosed);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        item = data.get(position);
        holder.itemIcon.setImageResource((Integer) item.get("itemIcon"));
        holder.itemName.setText((String) item.get("itemName"));
        holder.itemChoosed.setText((String) item.get("itemChoosed"));

        return convertView;
    }

    private class ViewHolder{
        ImageView itemIcon;
        TextView itemName;
        TextView itemChoosed;
    }

}
