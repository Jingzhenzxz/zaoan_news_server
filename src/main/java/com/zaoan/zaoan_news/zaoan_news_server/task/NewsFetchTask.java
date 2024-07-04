package com.zaoan.zaoan_news.zaoan_news_server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zaoan.zaoan_news.zaoan_news_server.common.ErrorCode;
import com.zaoan.zaoan_news.zaoan_news_server.exception.BusinessException;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Media;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.News;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.NewsTopic;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Topic;
import com.zaoan.zaoan_news.zaoan_news_server.service.MediaService;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsService;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsTopicService;
import com.zaoan.zaoan_news.zaoan_news_server.service.TopicService;
import com.zaoan.zaoan_news.zaoan_news_server.util.RssUtils;
import com.zaoan.zaoan_news.zaoan_news_server.util.UpdateLatestNewsOfTopicUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

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
    private final TopicService topicService;
    private final NewsTopicService newsTopicService;
    private final RssUtils rssUtils;
    private final UpdateLatestNewsOfTopicUtils updateLatestNewsOfTopicUtils;

    @Autowired
    public NewsFetchTask(MediaService mediaService, NewsService newsService, TopicService topicService, NewsTopicService newsTopicService, RssUtils rssUtils, UpdateLatestNewsOfTopicUtils updateLatestNewsOfTopicUtils) {
        this.mediaService = mediaService;
        this.newsService = newsService;
        this.topicService = topicService;
        this.newsTopicService = newsTopicService;
        this.rssUtils = rssUtils;
        this.updateLatestNewsOfTopicUtils = updateLatestNewsOfTopicUtils;
    }

    @Scheduled(fixedRate = 3600000)
    public void fetchNewsFromRss() {
        List<Media> mediaList = mediaService.list();
        for (Media media : mediaList) {
            fetchNewsFromRss(media);
        }
    }

    public void fetchNewsFromRss(Media media) {
        /**
         * 获取所有media
         * Stream.of(T... values) 方法接收的是一组分散的值而不是一个集合，它将传入的值转换为一个 Stream 对象。
         * mediaMapper.getAllMedias() 方法返回的是一个集合。
         * 所以应该直接将集合转换为 Stream，而不是使用 Stream.of。我们可以直接调用集合的 stream() 方法来创建一个流。
         */

        Set<Topic> topicSet = new HashSet<>();
        List<News> newsList = rssUtils.fetchFromRssUrl(media.getRssLink());
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
                    // 如果存在且当前新闻的发布日期大于已有新闻的发布日期，则更新内容
                    boolean newsUpdated = newsService.update(news, newsQueryWrapper);
                    if (!newsUpdated) {
                        throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新资讯失败");
                    }
                }
                // 关联news和topic
                // 查询newsId
                News savedNews = newsService.getOne(newsQueryWrapper);
                Long newsId = savedNews.getId();
                // 查询topic
                List<Topic> allTopicList = topicService.list();
                // like查询是看右边的字符串是否在左边的字符串中，不是反过来。
                List<Topic> topicList = allTopicList.stream()
                        .filter(topic -> containsIgnoreCase(savedNews.getTitle(), topic.getName()))
                        .collect(Collectors.toList());
                // 新增news_topic信息
                boolean newsTopicSaved = true;
                for (Topic topic : topicList) {
                    Long topicId = topic.getId();
                    QueryWrapper<NewsTopic> newsTopicQueryWrapper = new QueryWrapper<>();
                    newsTopicQueryWrapper.eq("news_id", savedNews.getId());
                    newsQueryWrapper.eq("topic_id", topicId);
                    NewsTopic oldNewsTopic = newsTopicService.getOne(newsTopicQueryWrapper);

                    if (oldNewsTopic != null) {
                        continue;
                    }

                    NewsTopic newsTopic = new NewsTopic();
                    newsTopic.setNewsId(newsId);
                    newsTopic.setTopicId(topic.getId());
                    newsTopicSaved &= newsTopicService.save(newsTopic);
                }
                if (!newsTopicSaved) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR);
                }

                topicSet.addAll(topicList);
            }
        }
        updateLatestNewsOfTopicUtils.updateLatestThreeNewsOfTopic(topicSet);
    }
}
