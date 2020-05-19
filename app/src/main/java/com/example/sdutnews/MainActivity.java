package com.example.sdutnews;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        }
        try {
            showData(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
