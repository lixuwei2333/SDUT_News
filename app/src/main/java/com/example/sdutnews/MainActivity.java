package com.example.sdutnews;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread(){
            public void run(){
                    Document document = Jsoup.parse("https://lgwindow.sdut.edu.cn/1058/list.htm");
                    Element postList = document.getElementById("wp_paging_w3");
                    System.out.println("**********************");
                    System.out.println(postList.toString());
                    System.out.println("**********************");
            }
        };
        thread.start();
    }
}
