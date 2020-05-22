package com.example.sdutnews.utils;

import android.view.WindowManager;

import com.example.sdutnews.bean.ListEleBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlUtil {
    public static String Sdut ="https://lgwindow.sdut.edu.cn";
    public static String h1 = Sdut+"/1058/list.htm";  //理工要闻
    public static String h2 = Sdut+"/zhxw/list.htm";  //综合新闻
    public static String h3 = Sdut+"/1073/list.htm";  //院部传真
    public static String h4 = Sdut+"/jxky/list.htm";  //科学教研
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
            data.setUrl(Sdut+titleEle.attr("href"));
            list.add(data);
        }
        return list;
    }
    static public String get_news(String html) throws IOException {
        Document document = Jsoup.connect(html).get();
        Element postList1 = document.getElementsByClass("con_left clearfix").get(0);
        Element postList = document.getElementsByClass("wp_articlecontent").get(0);
        Elements postItems = postList.select("[style='font-size:16px;line-height:1.75em;text-indent:2em;text-align:justify;']");
        Elements imgItems = postList.select("[data-layer='photo']");
        String imgUrl = null;
        for (Element imgItem: imgItems) {
            imgUrl = Sdut + imgItem.attr("src").toString();
            imgItem.attr("src", imgUrl);
            imgItem.attr("original-src", imgUrl);
            imgItem.attr("max-width", "100%")
                    .attr("height", "auto");
            imgItem.attr("style", "max-width:100%;height:auto");
        }
        for (Element postItem : postItems) {

            //System.out.println(postItem.text());
        }
        return postList1.toString();
    }
}