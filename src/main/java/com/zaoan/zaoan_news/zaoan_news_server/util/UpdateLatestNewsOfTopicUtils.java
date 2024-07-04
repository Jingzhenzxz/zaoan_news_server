package com.zaoan.zaoan_news.zaoan_news_server.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zaoan.zaoan_news.zaoan_news_server.common.ErrorCode;
import com.zaoan.zaoan_news.zaoan_news_server.exception.ThrowUtils;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.News;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.NewsTopic;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Topic;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsService;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsTopicService;
import com.zaoan.zaoan_news.zaoan_news_server.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: zaoan_news_server
 * @description:
 * @author: Jingzhen
 * @create: 2024-07-01 14:19
 **/
@Component
public class UpdateLatestNewsOfTopicUtils {
    // 一定要注入，不然用到的时候newsTopicService为null，会报空指针异常
    private final NewsTopicService newsTopicService;
    private final TopicService topicService;
    private final NewsService newsService;

    @Autowired
    public UpdateLatestNewsOfTopicUtils(NewsTopicService newsTopicService, TopicService topicService, NewsService newsService) {
        this.newsTopicService = newsTopicService;
        this.topicService = topicService;
        this.newsService = newsService;
    }

    public void updateLatestThreeNewsOfTopic(Set<Topic> topicSet) {
        // 确定与当前新闻相关的topic的前三条最新新闻
        for (Topic topic : topicSet) {
            Long topicId = topic.getId();
            QueryWrapper<NewsTopic> newsTopicQueryWrapper = new QueryWrapper<>();
            newsTopicQueryWrapper.eq("topic_id", topicId);
            List<NewsTopic> newsTopicList = newsTopicService.list(newsTopicQueryWrapper);

            List<Long> latestNewsIdList = newsTopicList.stream()
                    .map(n -> newsService.getById(n.getNewsId()))
                    .sorted(Comparator
                            .comparing(News::getUpdatedAt)
                            .thenComparing(News::getId).reversed())
                    .limit(3)
                    .map(News::getId)
                    .collect(Collectors.toList());

            long newContentTodayCount = newsTopicList.stream()
                    .map(n -> newsService.getById(n.getNewsId()))
                    .filter(n -> n.getUpdatedAt().toLocalDate().equals(LocalDate.now(ZoneId.of("UTC"))))
                    .count();

            UpdateWrapper<Topic> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", topicId);

            // 设置最新新闻 ID
            if (latestNewsIdList.size() == 0) {
                updateWrapper.set("latest_news_1_id", null);
                updateWrapper.set("latest_news_2_id", null);
                updateWrapper.set("latest_news_3_id", null);
            }
            if (latestNewsIdList.size() == 1) {
                updateWrapper.set("latest_news_1_id", latestNewsIdList.get(0));
                updateWrapper.set("latest_news_2_id", null);
                updateWrapper.set("latest_news_3_id", null);
            }
            if (latestNewsIdList.size() == 2) {
                updateWrapper.set("latest_news_1_id", latestNewsIdList.get(0));
                updateWrapper.set("latest_news_2_id", latestNewsIdList.get(1));
                updateWrapper.set("latest_news_3_id", null);
            }
            if (latestNewsIdList.size() > 2) {
                updateWrapper.set("latest_news_1_id", latestNewsIdList.get(0));
                updateWrapper.set("latest_news_2_id", latestNewsIdList.get(1));
                updateWrapper.set("latest_news_3_id", latestNewsIdList.get(2));
            }

            // 更新 new_content_today_count
            updateWrapper.set("new_content_today_count", newContentTodayCount);

            boolean topicUpdated = topicService.update(new Topic(), updateWrapper);
            ThrowUtils.throwIf(!topicUpdated, ErrorCode.OPERATION_ERROR);
        }
    }
}
