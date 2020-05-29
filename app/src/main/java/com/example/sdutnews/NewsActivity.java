package com.example.sdutnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdutnews.bean.ListEleBean;
import com.example.sdutnews.utils.HtmlUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private Intent intent;
    int flag=0;
    ImageView star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        final WebView test = findViewById(R.id.content);
        star = findViewById(R.id.starNews);
        final String[] imgItems = new String[1];
        intent = getIntent();

        List<ListEleBean> listEleBeans = LitePal.where("Url = ?",intent.getStringExtra("url")).find(ListEleBean.class);
        if(listEleBeans.size()!=0) {
            flag = 1;
            star.setImageResource(R.drawable.star_on);
        }
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0) {
                    ListEleBean listEleBean = new ListEleBean();
                    listEleBean.setContent(intent.getStringExtra("content"));
                    listEleBean.setTime(intent.getStringExtra("time"));
                    listEleBean.setTitle(intent.getStringExtra("title"));
                    listEleBean.setUrl(intent.getStringExtra("url"));
                    listEleBean.save();
                    if (listEleBean.save()) {
                        Toast.makeText(NewsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewsActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                    star.setImageResource(R.drawable.star_on);
                }
                else {
                    LitePal.deleteAll(ListEleBean.class,"url = ?",intent.getStringExtra("url"));
                    star.setImageResource(R.drawable.star_off);
                }
                flag^=1;
            }
        });
        findViewById(R.id.shareNews).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                                "山东理工大学新闻："+intent.getStringExtra("title")+"\n\n"
                                 +intent.getStringExtra("content")+"\n\n"
                                 +"详情见："+intent.getStringExtra("url"));
                shareIntent = Intent.createChooser(shareIntent, "分享给你的朋友吧");
                startActivity(shareIntent);
            }
        });

        new Thread(){
            public void run() {
                try {
                    imgItems[0] = HtmlUtil.get_news(intent.getStringExtra("url"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        test.loadData(imgItems[0],"text/html; charset=UTF-8;",null);
                    }
                });
            }
        }.start();

    }
}
