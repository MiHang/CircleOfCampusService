package team.coc.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class CampusNoticeCrawler implements PageProcessor {

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数
    private Site site = Site.me().setCharset("utf-8").setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    public void process(Page page) {

        System.out.println("------- 校公告详情数据爬取 ------");

        // 使用css选择器和正则表达式爬取页面公告详情链接
        List<String> list = page.getHtml().css("ul.pageList").links().all();

    }

    public Site getSite() {
        return site;
    }
}
