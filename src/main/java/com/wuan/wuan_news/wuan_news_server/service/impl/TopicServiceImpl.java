package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.TopicCardDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicDetailDTO;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsMapper;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsTopicMapper;
import com.wuan.wuan_news.wuan_news_server.mapper.TopicMapper;
import com.wuan.wuan_news.wuan_news_server.model.News;
import com.wuan.wuan_news.wuan_news_server.model.NewsTopic;
import com.wuan.wuan_news.wuan_news_server.model.Topic;
import com.wuan.wuan_news.wuan_news_server.service.TopicService;
import com.wuan.wuan_news.wuan_news_server.util.TopicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:41
 * @description
 */
public class TopicServiceImpl implements TopicService {
    private final TopicMapper topicMapper;
    private final NewsMapper newsMapper;
    private final NewsTopicMapper newsTopicMapper;
    private final TopicUtil topicUtil;

    @Autowired
    public TopicServiceImpl(TopicMapper topicMapper, NewsMapper newsMapper, NewsTopicMapper newsTopicMapper, TopicUtil topicUtil) {
        this.topicMapper = topicMapper;
        this.newsMapper = newsMapper;
        this.newsTopicMapper = newsTopicMapper;
        this.topicUtil = topicUtil;
    }

    @Override
    @Transactional
    public TopicDTO createTopicAndAssociateNews(String topicName) {
        Topic topic = new Topic();
        topic.setName(topicName);
        topic = topicMapper.createTopic(topic);

        List<News> matchingNews = newsMapper.getNewsByTitleContaining(topicName);

        for (News news : matchingNews) {
            NewsTopic newsTopic = new NewsTopic();
            newsTopic.setNewsId(news.getId());
            newsTopic.setTopicId(topic.getId());
            newsTopicMapper.create(newsTopic);
        }

        return topicUtil.convertTopicModelToTopicDTO(topic);  // Assuming a constructor in TopicDTO that accepts a Topic entity.
    }

    @Override
    public List<TopicCardDTO> getAllTopicCards() {
        List<Topic> allTopics = topicMapper.getAllTopics();

        return allTopics.stream().map(topic -> {
            List<News> newsForTopic = newsTopicMapper.getNewsByTopicId(topic.getId())
                    .stream()
                    .sorted((n1, n2) -> n2.getPubDate().compareTo(n1.getPubDate()))
                    .limit(3)
                    .collect(Collectors.toList());

            long newContentTodayCount = newsForTopic.stream()
                    .filter(news -> news.getCreatedAt().toLocalDate().equals(LocalDate.now()))
                    .count();

            return new TopicCardDTO(topic.getName(), newsForTopic, newContentTodayCount);
        }).collect(Collectors.toList());
    }

    @Override
    public TopicCardDTO getTopicCardByTopicName(String topicName) {
        Topic topic = topicMapper.getTopicByName(topicName);

        if (topic == null) {
            return null;
        }

        List<News> newsForTopic = newsTopicMapper.getNewsByTopicId(topic.getId())
                .stream()
                .sorted((n1, n2) -> n2.getPubDate().compareTo(n1.getPubDate()))
                .limit(3)
                .collect(Collectors.toList());

        long newContentTodayCount = newsForTopic.stream()
                .filter(news -> news.getCreatedAt().toLocalDate().equals(LocalDate.now()))
                .count();

        return new TopicCardDTO(topic.getName(), newsForTopic, newContentTodayCount);
    }

    @Override
    public TopicDetailDTO getTopicDetailByName(String topicName) {
        Topic topic = topicMapper.getTopicByName(topicName);

        if (topic == null) {
            return null;
        }

        List<News> newsForTopic = newsTopicMapper.getNewsByTopicId(topic.getId());

        return new TopicDetailDTO(topic.getName(), newsForTopic);
    }
}
