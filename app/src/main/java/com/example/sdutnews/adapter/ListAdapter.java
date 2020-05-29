package com.example.sdutnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sdutnews.R;
import com.example.sdutnews.bean.ListEleBean;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<ListEleBean> list;
    public ListAdapter(Context context, List<ListEleBean> list) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ListEleBean listEleBean = (ListEleBean) getItem(position);
        viewHolder.Title.setText(listEleBean.getTitle());
        viewHolder.Content.setText(listEleBean.getContent());
        viewHolder.Time.setText(listEleBean.getTime());
        return convertView;
    }
    class ViewHolder {
        TextView Title,Content,Time;
        public ViewHolder(View view) {
            Title = view.findViewById(R.id.item_title);
            Time = view.findViewById(R.id.item_time);
            Content = view.findViewById(R.id.item_content);
        }
    }
}
