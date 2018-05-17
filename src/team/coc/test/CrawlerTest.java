package team.coc.test;

import team.coc.callback.CrawlerListener;
import team.coc.crawler.CampusNoticeCrawler;
import team.coc.crawler.CampusNoticeListLinksCrawler;
import team.coc.pojo.CampusCircle;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫测试
 */
public class CrawlerTest {

    // 存放所有公告的详情页链接
    private static List<String> allNoticeLinks = new ArrayList<String>();

    /**
     * 公告详情页爬取部分
     */
    private static void getNoticeLinks() {

        /* **************   公告详情页爬取部分   *************** */
        CampusNoticeListLinksCrawler noticeListLinksCrawler = new CampusNoticeListLinksCrawler();
        noticeListLinksCrawler.setCrawlerListener(new CrawlerListener<String>() {
            public void done(List<String> list) {
                for (String s : list) {
                    // 将爬取的结果加入allNoticeLinks
                    allNoticeLinks.add(s);
                }
            }
        });

        // 创建Spider
        Spider spider = Spider.create(noticeListLinksCrawler);

        // 添加初始的URL
        spider.addUrl("http://www.cdp.edu.cn/Category_1648/Index.aspx");
        spider.addUrl("http://www.cdp.edu.cn/Category_1648/Index_2.aspx");
        spider.addUrl("http://www.cdp.edu.cn/Category_1648/Index_3.aspx");
        spider.addUrl("http://www.cdp.edu.cn/Category_1648/Index_4.aspx");
        spider.addUrl("http://www.cdp.edu.cn/Category_1648/Index_5.aspx");

        // 开启10个线程
        spider.thread(20);

        // 异步启动，不会阻塞当前线程的运行
        //spider.start();

        // 启动，会阻塞当前线程执行
        spider.run();
    }

    /**
     * 获取所有公告
     */
    private static void getNotices() {

        CampusNoticeCrawler campusNoticeCrawler = new CampusNoticeCrawler();
        campusNoticeCrawler.setCrawlerListener(new CrawlerListener<CampusCircle>() {
            public void done(List<CampusCircle> list) {
                System.out.println("------ allNotices.size = " + list.size() + " ------");
                for (CampusCircle cc : list) {
                    System.out.println("Title = " + cc.getTitle() +
                            "\nPublishTime = " + cc.getPublishTime() +
                            "\nContent = " + cc.getContent() +
                            "\nimageUrl = " + cc.getImagesUrl());
                }
            }
        });

        // 创建Spider
        Spider spider = Spider.create(campusNoticeCrawler);

        // 添加初始的URL
        for (String url : allNoticeLinks) {
            spider.addUrl(url);
        }

        // 开启20个线程
        spider.thread(20);

        // 异步启动，不会阻塞当前线程的运行
        //spider.start();

        // 启动，会阻塞当前线程执行
        spider.run();
    }

    public static void main(String[]args) {

        getNoticeLinks();
        getNotices();
    }
}
