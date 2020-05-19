package com.example.sdutnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlUtil {
    public static String h1 = "https://lgwindow.sdut.edu.cn/1058/list.htm";
    public static String h2 = "https://lgwindow.sdut.edu.cn/zhxw/list.htm";
    public static void test(String html) throws IOException {
        Document document = Jsoup.connect(html).get();
        Element postList = document.select("tbody").get(2);
        Elements postItems = postList.select("tr");
        for (Element postItem : postItems) {
            //像jquery选择器一样，获取文章标题元素
            Elements titleEle = postItem.select(".list_tit [target='_blank']");
            Elements contentEle = postItem.select(".list_content [target='_blank']");
            Elements timeEle = postItem.select(".list_time [class='lt_b']");
            String x = titleEle.text();
            String y = contentEle.text();
            String z = timeEle.text();
            System.out.println(x);
            System.out.println(z);
            System.out.println(y);
        }
    }
    public static void main(String[] args) throws IOException {
        test(h1);
    }
}
