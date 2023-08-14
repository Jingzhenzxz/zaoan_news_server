package com.wuan.wuan_news.wuan_news_server.task;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import com.wuan.wuan_news.wuan_news_server.exception.NewsCreationException;
import com.wuan.wuan_news.wuan_news_server.exception.NewsUpdateException;
import com.wuan.wuan_news.wuan_news_server.mapper.MediaMapper;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsMapper;
import com.wuan.wuan_news.wuan_news_server.util.MediaUtil;
import com.wuan.wuan_news.wuan_news_server.util.NewsUtil;
import com.wuan.wuan_news.wuan_news_server.util.RssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
    private final MediaMapper mediaMapper;
    private final NewsMapper newsMapper;
    private final RssUtil rssUtil;
    private final MediaUtil mediaUtil;
    private final NewsUtil newsUtil;

    @Autowired
    public NewsFetchTask(MediaMapper mediaMapper, NewsMapper newsMapper, RssUtil rssUtil, MediaUtil mediaUtil, NewsUtil newsUtil) {
        this.mediaMapper = mediaMapper;
        this.newsMapper = newsMapper;
        this.rssUtil = rssUtil;
        this.mediaUtil = mediaUtil;
        this.newsUtil = newsUtil;
    }

    @Scheduled(fixedRate = 3600000)
    public void fetchNewsFromRss() {
        /*
         * 获取所有media
         * Stream.of(T... values) 方法接收的是一组分散的值而不是一个集合，它将传入的值转换为一个 Stream 对象。
         * mediaMapper.getAllMedias() 方法返回的是一个集合。
         * 所以应该直接将集合转换为 Stream，而不是使用 Stream.of。我们可以直接调用集合的 stream() 方法来创建一个流。
         */
        List<MediaDTO> mediaDTOs = mediaMapper.getAllMedias().stream().map(mediaUtil::convertMediaModelToMediaDTO).collect(Collectors.toList());
        // 遍历所有的media，从RSS源获取信息并保存
        for (MediaDTO mediaDTO : mediaDTOs) {
            List<NewsDTO> newsDTOsList = rssUtil.fetchFromRssUrl(mediaDTO.getRssLink());

            for (NewsDTO newsDTO : newsDTOsList) {
                NewsDTO existingNews = newsUtil.convertNewsModelToNewsDTO(newsMapper.getNewsByMediaNameAndNewsTitle(mediaDTO.getName(), newsDTO.getTitle()));

                if (existingNews == null) {
                    newsDTO.setMediaName(mediaDTO.getName());

                    int result = newsMapper.insert(newsUtil.convertNewsDTOToNewsModel(newsDTO));
                    if (result == 0) {
                        throw new NewsCreationException("保存资讯失败");
                    }
                } else if (newsDTO.getPubDate().isAfter(existingNews.getPubDate())) {
                    newsDTO.setMediaName(mediaDTO.getName());

                    int result = newsMapper.update(newsUtil.convertNewsDTOToNewsModel(newsDTO));
                    if (result == 0) {
                        throw new NewsUpdateException("更新资讯失败");
                    }
                }
            }
        }
    }
}
