package team.coc.callback;

import java.util.List;

/**
 * 爬虫相关接口
 * @param <E>
 */
public interface CrawlerListener<E> {

    /**
     * 完成爬取
     * @param list - 爬取结果
     */
    void done(List<E> list);
}
