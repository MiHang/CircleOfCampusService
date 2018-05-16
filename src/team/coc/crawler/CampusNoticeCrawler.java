package team.coc.crawler;

import team.coc.callback.CrawlerListener;
import team.coc.pojo.CampusCircle;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampusNoticeCrawler implements PageProcessor {

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数
    private Site site = Site.me().setCharset("utf-8").setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);

    private CrawlerListener<CampusCircle> crawlerListener;

    public void process(Page page) {

        System.out.println("------- 校公告详情数据爬取 ------");

        // 获取所有标题
        List<String> titles = page.getHtml().css("h2.title").regex("<h2[^>]*>([^<]*)</h2>").all();

        // 获取发布时间, 发布单位
        List<String> publishTimes = page.getHtml().css("div.property").regex("<span[^>]*>([^<]*)</span>").all();

        // 获取公告内容(带html标签)
        List<String> contents_temp = page.getHtml().css("div.conTxt").regex("<p.*>.*</p>").all();

        List<String> imgUrls = page.getHtml().css("div.conTxt").regex("<img.*src=(.*?)[^>]*?>").all();

        System.out.println("-------- imgUrl");
        for (String s : imgUrls) {
            System.out.println(s);
        }

        /*
        // 保存公告内容(无html标签)
        List<String> contents = new ArrayList<String>();

        // 过滤公告内容的html标签
        for (String s : contents_temp) {

            // 将<br>和<p>标签替换为换行符, 将&nbsp;替换为空格
            s = s.replace("<br>","\n");
            s = s.replace("</br>","\n");
            s = s.replace("<p>","\n");
            s = s.replaceAll("&nbsp;"," ");

            // 将其他标签替换为空字符串
            Pattern pattern = Pattern.compile("<[^>]+>");
            Matcher matcher = pattern.matcher(s);
            s = matcher.replaceAll("");

            // 去除前后空字符
            s = s.trim();

            // 将过滤完的内容存入contents集合
            contents.add(s);
        }

        // 数据清洗, 清洗掉内容为空和链接的公告
        if (contents.size() > 0) {
            for (String s : contents) {
                if ("".equals(s.trim()) || "http://".equals(s.trim().substring(0, 7))) {
                    contents.clear();
                    break;
                }
            }
        }

        // 存储爬取的数据到校园圈对象中
        List<CampusCircle> campusCircles = new ArrayList<CampusCircle>();

        // 爬取存在内容
        if ((titles != null && titles.size() > 0)
                && (publishTimes != null && publishTimes.size() > 0)
                && (contents.size() > 0)) {

            CampusCircle campusCircle = new CampusCircle();
            campusCircle.setTitle(titles.get(0));

            // 解析发布日期
            Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(publishTimes.get(0));
            } catch (ParseException e) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    date = sdf.parse(publishTimes.get(0));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
            campusCircle.setPublishTime(date);

            // 获取公告内容
            String msg = "";
            for (String s : contents) {
                msg += s + "\n";
            }
            campusCircle.setContent(msg);

            campusCircles.add(campusCircle);

            if (crawlerListener != null) {
                crawlerListener.done(campusCircles);
            }
        }*/

    }

    public Site getSite() {
        return site;
    }

    public void setCrawlerListener(CrawlerListener<CampusCircle> crawlerListener) {
        this.crawlerListener = crawlerListener;
    }
}
