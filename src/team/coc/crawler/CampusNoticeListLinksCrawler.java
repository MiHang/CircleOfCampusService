package team.coc.crawler;

import team.coc.callback.CrawlerListener;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * 学校通知公告列表页 - 公告详情链接 爬虫
 */
public class CampusNoticeListLinksCrawler implements PageProcessor {

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数
    private Site site = Site.me().setCharset("utf-8").setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    private CrawlerListener<String> crawlerListener;

    public void process(Page page) {

        System.out.println("------- 校通知公告列表公告详情页链接爬取 ------");

        // 使用css选择器和正则表达式爬取页面公告详情链接
        List<String> list = page.getHtml().css("ul.pageList").links().all();

        if (crawlerListener != null) {
            crawlerListener.done(list);
        }
    }

    public void setCrawlerListener(CrawlerListener<String> crawlerListener) {
        this.crawlerListener = crawlerListener;
    }

    public Site getSite() {
        return site;
    }
}