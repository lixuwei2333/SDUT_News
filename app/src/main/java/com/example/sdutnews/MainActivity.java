package com.example.sdutnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

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
import static com.example.sdutnews.R.id.list2;
import static com.example.sdutnews.R.id.list3;
import static com.example.sdutnews.R.id.list4;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<ListEleBean> list;
    ListView listView;
    private RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int flag = 0;
                switch (checkedId) {
                    case list1:
                        flag = 1;
                        break;
                    case list2:
                        flag = 2;
                        break;
                    case list3:
                        flag = 3;
                        break;
                    case list4:
                        flag = 4;
                        break;
                }
                try {
                    showData(flag);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        init();
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
                list = HtmlUtil.get_list(flag);
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
        switch (v.getId()) {
            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "欢迎访问山东理工大学新闻网：\nhttps://lgwindow.sdut.edu.cn/1058/list.htm");
                shareIntent = Intent.createChooser(shareIntent, "分享给你的朋友吧");
                startActivity(shareIntent);
                break;
        }
    }
    public void init(){
        new Thread(){
            public void run() {
                try {
                    HtmlUtil.init(1);
                    showData(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            public void run() {
                try {
                    HtmlUtil.init(2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            public void run() {
                try {
                    HtmlUtil.init(3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            public void run() {
                try {
                    HtmlUtil.init(4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}