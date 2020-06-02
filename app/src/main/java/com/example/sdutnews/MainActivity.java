package com.example.sdutnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdutnews.adapter.ListAdapter;
import com.example.sdutnews.bean.ListEleBean;
import com.example.sdutnews.utils.HtmlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import static com.example.sdutnews.R.id.list1;
import static com.example.sdutnews.R.id.list2;
import static com.example.sdutnews.R.id.list3;
import static com.example.sdutnews.R.id.list4;
import static com.example.sdutnews.R.id.list5;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<ListEleBean> list;
    ListView listView;
    TextView textView;
    private RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        findViewById(R.id.share).setOnClickListener(this);
        rg = findViewById(R.id.rg);
        textView = findViewById(R.id.hint);
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
                    case list5:
                        flag = 5;
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
                intent.putExtra("time",listEleBean.getTime());
                startActivityForResult(intent,1);
            }
        });
    }

    public void showData(final int flag) throws IOException {   //展示新闻列表
        if(flag==5) {
            list = LitePal.findAll(ListEleBean.class);
            ListAdapter adapter = new ListAdapter(MainActivity.this,list);
            listView.setAdapter(adapter);
            return;
        }
        new Thread(){
            public void run() {
                list = HtmlUtil.get_list(flag);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("");
                        ListAdapter adapter = new ListAdapter(MainActivity.this,list);
                        listView.setAdapter(adapter);

                    }
                });
            }
        }.start();
    }
    public void onClick(View v) {           //分享操作
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1&&resultCode == 2) {
           if(rg.getCheckedRadioButtonId()== list5) {
                try {
                    showData(5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void init(){         //并行加载4列表新闻
        for(int i = 1; i <= 4; i++) {
            final int finalI = i;
            new Thread() {
                public void run() {
                    try {
                        HtmlUtil.init(finalI);
                        if(finalI==1) showData(finalI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}