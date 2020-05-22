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
import android.widget.TextView;

import com.example.sdutnews.utils.HtmlUtil;

import java.io.IOException;

public class NewsActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        final WebView test = findViewById(R.id.content);
        final String[] imgItems = new String[1];
        intent = getIntent();
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
