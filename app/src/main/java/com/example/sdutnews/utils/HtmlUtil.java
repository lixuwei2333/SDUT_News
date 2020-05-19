package com.example.sdutnews.utils;

import com.example.sdutnews.bean.ListEleBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlUtil {
    public static String h1 = "https://lgwindow.sdut.edu.cn/1058/list.htm";
    public static String h2 = "https://lgwindow.sdut.edu.cn/zhxw/list.htm";
    public static String h3 = "https://lgwindow.sdut.edu.cn/1073/list.htm";
    public static String h4 = "https://lgwindow.sdut.edu.cn/jxky/list.htm";
    public static List<ListEleBean> get_list(int flag) throws IOException {
        List<ListEleBean> list = new ArrayList<ListEleBean>();
        Document document = Jsoup.connect(h1).get();
        if(flag == 2) document = Jsoup.connect(h2).get();
        if(flag == 3) document = Jsoup.connect(h3).get();
        if(flag == 4) document = Jsoup.connect(h4).get();

        Element postList = document.select("tbody").get(2);
        Elements postItems = postList.select("tr");
        for (Element postItem : postItems) {
            //像jquery选择器一样，获取文章标题元素
            Elements titleEle = postItem.select(".list_tit [target='_blank']");
            Elements contentEle = postItem.select(".list_content [target='_blank']");
            Elements timeEle = postItem.select(".list_time [class='lt_b']");
            ListEleBean data = new ListEleBean();
            data.setContent(contentEle.text());
            data.setTitle(titleEle.text());
            data.setTime(timeEle.text());
            list.add(data);
        }
        return list;
    }
}
