package com.example.sdutnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sdutnews.adapter.ListAdapter;
import com.example.sdutnews.bean.ListEleBean;
import com.example.sdutnews.utils.HtmlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import static com.example.sdutnews.R.id.list1;
import static com.example.sdutnews.R.id.list4;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<ListEleBean> list;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        findViewById(R.id.list1).setOnClickListener(this);
        findViewById(R.id.list2).setOnClickListener(this);
        findViewById(R.id.list3).setOnClickListener(this);
        findViewById(R.id.list4).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListEleBean listEleBean = list.get(position);
                Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                intent.putExtra("title",listEleBean.getTitle());
                intent.putExtra("url",listEleBean.getUrl());
                intent.putExtra("content",listEleBean.getContent());
                startActivity(intent);
            }
        });

        try {
            showData(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showData(final int flag) throws IOException {
        new Thread(){
            public void run() {
                try {
                    list = HtmlUtil.get_list(flag);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListAdapter adapter = new ListAdapter(MainActivity.this,list);
                        listView.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }
    public void onClick(View v) {
        int flag = 0;
        switch (v.getId()) {
            case R.id.list1:
                flag = 1;
                break;
            case R.id.list2:
                flag = 2;
                break;
            case R.id.list3:
                flag = 3;
                break;
            case R.id.list4:
                flag = 4;
                break;
            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "欢迎访问山东理工大学新闻网：\nhttps://lgwindow.sdut.edu.cn/1058/list.htm");
                shareIntent = Intent.createChooser(shareIntent, "分享给你的朋友吧");
                startActivity(shareIntent);
                break;
        }
        try {
            if(flag != 0) showData(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}