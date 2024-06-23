package com.wuan.wuan_news.wuan_news_server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.model.entity.Media;
import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import com.wuan.wuan_news.wuan_news_server.util.RssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/05/ 11:06
 * @description
 */
@Component
@Slf4j
public class NewsFetchTask {
    private final MediaService mediaService;
    private final NewsService newsService;
    private final RssUtil rssUtil;

    @Autowired
    public NewsFetchTask(MediaService mediaService, NewsService newsService, RssUtil rssUtil) {
        this.mediaService = mediaService;
        this.newsService = newsService;
        this.rssUtil = rssUtil;
    }

    @Scheduled(fixedRate = 3600000)
    public void fetchNewsFromRss() {
        /**
         * 获取所有media
         * Stream.of(T... values) 方法接收的是一组分散的值而不是一个集合，它将传入的值转换为一个 Stream 对象。
         * mediaMapper.getAllMedias() 方法返回的是一个集合。
         * 所以应该直接将集合转换为 Stream，而不是使用 Stream.of。我们可以直接调用集合的 stream() 方法来创建一个流。
         */
        List<Media> medias = mediaService.list();
        // 遍历所有的media，从RSS源获取信息并保存
        for (Media media : medias) {
            List<News> newsList = rssUtil.fetchFromRssUrl(media.getRssLink());
            if (newsList != null) {
                for (News news : newsList) {
                    // 查询该新闻是否已存在
                    QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
                    newsQueryWrapper.eq("media_name", media.getName());
                    newsQueryWrapper.eq("media_id", media.getId());
                    newsQueryWrapper.eq("title", news.getTitle());
                    newsQueryWrapper.eq("link", news.getLink());
                    newsQueryWrapper.eq("pub_date", news.getPubDate());
                    newsQueryWrapper.eq("author", news.getAuthor());
                    News existingNews = newsService.getOne(newsQueryWrapper);
                    // 如果不存在，则保存
                    if (existingNews == null) {
                        news.setMediaName(media.getName());
                        news.setMediaId(media.getId());

                        boolean saved = newsService.save(news);
                        if (!saved) {
                            throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存资讯失败");
                        }
                    } else if (news.getPubDate().isAfter(existingNews.getPubDate())) {
                        boolean newsUpdated = newsService.update(news, newsQueryWrapper);
                        if (!newsUpdated) {
                            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新资讯失败");
                        }
                    }
                }
            }
        }
    }
}
