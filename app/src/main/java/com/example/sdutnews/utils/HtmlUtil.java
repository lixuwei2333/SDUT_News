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
    private static String Sdut ="https://lgwindow.sdut.edu.cn";
    private static String h1 = Sdut+"/1058/list";  //理工要闻
    private static String h2 = Sdut+"/zhxw/list";  //综合新闻
    private static String h3 = Sdut+"/1073/list";  //院部传真
    private static String h4 = Sdut+"/jxky/list";  //科学教研
    private static List<ListEleBean> list1 = new ArrayList<ListEleBean>();  //缓存4个新闻列表
    private static List<ListEleBean> list2 = new ArrayList<ListEleBean>();
    private static List<ListEleBean> list3 = new ArrayList<ListEleBean>();
    private static List<ListEleBean> list4 = new ArrayList<ListEleBean>();
    public static void init(int flag) throws IOException {    //初始化，加上flag方便并行
        if(flag == 1) list1 = get_list_form_html(1);
        if(flag == 2) list2 = get_list_form_html(2);
        if(flag == 3) list3 = get_list_form_html(3);
        if(flag == 4) list4 = get_list_form_html(4);
    }
    public static List<ListEleBean> get_list(int flag) {            //接口获得新闻列表
        if(flag==1) return list1;
        if(flag==2) return list2;
        if(flag==3) return list3;
        if(flag==4) return list4;
        return null;
    }
    private static List<ListEleBean> get_list_form_html(int flag) throws IOException {    //从html中解析新闻列表
        List<ListEleBean> list = new ArrayList<ListEleBean>();
        String html_pre = new String();
        String html = new String();
        if(flag == 1) html_pre = h1;
        if(flag == 2) html_pre = h2;
        if(flag == 3) html_pre = h3;
        if(flag == 4) html_pre = h4;
        for(int i = 1; i <= 5; i++) {
            html = html_pre + i + ".htm";
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
                data.setUrl(Sdut + titleEle.attr("href"));
                list.add(data);
            }
        }
        return list;
    }
    static public String get_news(String html) throws IOException {  //获得新闻详情
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
        return postList1.toString();
    }
}