package com.wuan.wuan_news.wuan_news_server.service;

import com.wuan.wuan_news.wuan_news_server.dto.TopicCardDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicDetailDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 10:04
 * @description
 */
public interface TopicService {
    TopicDTO createTopicAndAssociateNews(String topicName);
    TopicDetailDTO getTopicDetailByName(String topicName);
    List<TopicCardDTO> getAllTopicCards();
    TopicCardDTO getTopicCardByTopicName(String topicName);
    void followTopic(Long userId, Long topicId);
    void unfollowTopic(Long userId, Long topicId);
    boolean isFollowing(Long userId, Long topicId);
}
