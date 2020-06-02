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
    private static ArrayList<String> h = new ArrayList<String>(){{
        add(Sdut+"/1058/list");         //理工要闻
        add(Sdut+"/zhxw/list");         //综合新闻
        add(Sdut+"/1073/list");         //院部传真
        add(Sdut+"/jxky/list");         //科学教研
    }};
    private static ArrayList<List<ListEleBean>>list = new ArrayList<List<ListEleBean>>(){{
        for(int i = 1; i <= 4; i++) add(new ArrayList<ListEleBean>());
    }};

    public static void init(int flag) throws IOException {        //初始化，加上flag方便并行
        list.set(flag-1,get_list_form_html(flag));
    }
    public static List<ListEleBean> get_list(int flag) {            //接口获得新闻列表
        return list.get(flag-1);
    }
    private static List<ListEleBean> get_list_form_html(int flag) throws IOException {    //从html中解析新闻列表
        List<ListEleBean> list = new ArrayList<ListEleBean>();
        String html_pre = new String();
        String html = new String();
        html_pre = h.get(flag-1);
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
                if(contentEle.text().indexOf(0)!=' ') {
                    data.setContent("     "+contentEle.text());
                }
                else data.setContent(contentEle.text());
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
        document.getElementsByClass("article_inf2").select(".article_inf2 div").get(1).remove(); //删除“阅读次数”
        Element postList = document.getElementsByClass("wp_articlecontent").get(0);
        Elements imgItems = postList.select("[data-layer='photo']");
        String imgUrl = null;
        for (Element imgItem: imgItems) {
            imgUrl = Sdut + imgItem.attr("src");
            imgItem.attr("src", imgUrl);
            imgItem.attr("original-src", imgUrl);
            imgItem.attr("max-width", "100%")
                    .attr("height", "auto");
            imgItem.attr("style", "max-width:100%;height:auto");
        }
        return postList1.toString();
    }
}