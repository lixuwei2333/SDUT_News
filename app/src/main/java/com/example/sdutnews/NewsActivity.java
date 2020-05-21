package com.example.sdutnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.sdutnews.utils.HtmlUtil;

import java.io.IOException;

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        final WebView test = findViewById(R.id.content);
        final String[] imgItems = new String[1];


        new Thread(){
            public void run() {
                try {
                    imgItems[0] = HtmlUtil.get_news("https://lgwindow.sdut.edu.cn/2020/0520/c1058a382722/page.htm");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String img = "<p><img src=\"https://lgwindow.sdut.edu.cn/_upload/article/images/61/55/3b431b7e42739ad53b7d9e25b652/1dfaf7d0-0395-44af-8502-5b0502a8d160.jpg\"></p>";
                        //Spanned result = Html.fromHtml();
                        test.loadData(imgItems[0],"text/html; charset=UTF-8;",null);
                    }
                });
            }
        }.start();
    }
}
