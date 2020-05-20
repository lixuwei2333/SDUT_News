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
    public static String h1 = "https://lgwindow.sdut.edu.cn/1058/list.htm";  //理工要闻
    public static String h2 = "https://lgwindow.sdut.edu.cn/zhxw/list.htm";  //综合新闻
    public static String h3 = "https://lgwindow.sdut.edu.cn/1073/list.htm";  //院部传真
    public static String h4 = "https://lgwindow.sdut.edu.cn/jxky/list.htm";  //科学教研
    public static List<ListEleBean> get_list(int flag) throws IOException {    //获得新闻列表
        String html = new String();
        if(flag == 1) html = h1;
        if(flag == 2) html = h2;
        if(flag == 3) html = h3;
        if(flag == 4) html = h4;
        List<ListEleBean> list = new ArrayList<ListEleBean>();
        Document document = Jsoup.connect(html).get();

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
            data.setUrl(titleEle.attr("href"));
            System.out.println(titleEle.attr("href"));
            list.add(data);
        }
        return list;
    }
    static public void get_news(String html) throws IOException {
        Document document = Jsoup.connect(html).get();
        Element postList = document.getElementsByClass("wp_articlecontent").get(0);
        Elements postItems = postList.select("[style='font-size:16px;line-height:1.75em;text-indent:2em;text-align:justify;']");
        for (Element postItem : postItems) {
            System.out.println(postItem.text());
        }
    }
    public static void main(String[] args) throws IOException {
        get_news("https://lgwindow.sdut.edu.cn/2020/0520/c1058a382722/page.htm");
    }
}