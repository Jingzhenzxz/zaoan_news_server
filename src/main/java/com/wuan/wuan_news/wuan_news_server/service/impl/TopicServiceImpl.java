package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.TopicCardDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicDetailDTO;
import com.wuan.wuan_news.wuan_news_server.exception.TopicException;
import com.wuan.wuan_news.wuan_news_server.exception.UserCreationFailedException;
import com.wuan.wuan_news.wuan_news_server.exception.UserTopicDeleteFailedException;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsMapper;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsTopicMapper;
import com.wuan.wuan_news.wuan_news_server.mapper.TopicMapper;
import com.wuan.wuan_news.wuan_news_server.mapper.UserTopicMapper;
import com.wuan.wuan_news.wuan_news_server.model.News;
import com.wuan.wuan_news.wuan_news_server.model.NewsTopic;
import com.wuan.wuan_news.wuan_news_server.model.Topic;
import com.wuan.wuan_news.wuan_news_server.service.TopicService;
import com.wuan.wuan_news.wuan_news_server.util.TopicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:41
 * @description
 */
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicMapper topicMapper;
    private final NewsMapper newsMapper;
    private final NewsTopicMapper newsTopicMapper;
    private final TopicUtil topicUtil;
    private final UserTopicMapper userTopicMapper;

    @Autowired
    public TopicServiceImpl(TopicMapper topicMapper, NewsMapper newsMapper, NewsTopicMapper newsTopicMapper,
                            TopicUtil topicUtil, UserTopicMapper userTopicMapper) {
        this.topicMapper = topicMapper;
        this.newsMapper = newsMapper;
        this.newsTopicMapper = newsTopicMapper;
        this.topicUtil = topicUtil;
        this.userTopicMapper = userTopicMapper;
    }

    @Override
    @Transactional
    public TopicDTO createTopicAndAssociateNews(String topicName) {
        Topic topic = new Topic();
        topic.setName(topicName);
        int createdCount = topicMapper.createTopic(topic);
        if (createdCount != 1) {
            throw new TopicException("创建主题失败");
        } else {
            topic = topicMapper.getTopicByName(topicName);
        }

        List<News> matchingNews = newsMapper.getNewsByTitleContaining(topicName);

        for (News news : matchingNews) {
            NewsTopic newsTopic = new NewsTopic();
            newsTopic.setNewsId(news.getId());
            newsTopic.setTopicId(topic.getId());
            newsTopicMapper.create(newsTopic);
        }

        return topicUtil.convertTopicModelToTopicDTO(topic);
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

    // 获取指定主题的主题卡片
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
    public void followTopic(Long userId, Long topicId) {
        int number = userTopicMapper.insertUserTopic(userId, topicId);
        if (number == 0) {
            throw new UserCreationFailedException("创建 user_topic 数据失败");
        }
    }

    @Override
    public void unfollowTopic(Long userId, Long topicId) {
        int number = userTopicMapper.deleteUserTopic(userId, topicId);
        if (number == 0) {
            throw new UserTopicDeleteFailedException("删除 user_topic 数据失败");
        }
    }

    @Override
    public boolean isFollowing(Long userId, Long topicId) {
        List<Long> topicIds = userTopicMapper.getTopicIdsByUserId(userId);
        return topicIds.contains(topicId);
    }

    @Override
    public List<TopicCardDTO> getFollowedTopicsByUserId(Long userId) {
        List<Long> topicIds = userTopicMapper.getTopicIdsByUserId(userId);
        List<Topic> topics = new LinkedList<>();
        for (Long topicId : topicIds) {
            Topic topic = topicMapper.getTopicByTopicId(topicId);
            topics.add(topic);
        }
        return topics.stream().map(topic -> {
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
    public TopicDetailDTO getTopicDetailByName(String topicName) {
        Topic topic = topicMapper.getTopicByName(topicName);

        if (topic == null) {
            return null;
        }

        List<News> newsForTopic = newsTopicMapper.getNewsByTopicId(topic.getId());

        return new TopicDetailDTO(topic.getName(), newsForTopic);
    }
}
