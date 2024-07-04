package com.zaoan.zaoan_news.zaoan_news_server.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.topic.TopicQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Topic;
import com.zaoan.zaoan_news.zaoan_news_server.model.vo.TopicVO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 10:04
 * @description
 */
public interface TopicService extends IService<Topic> {
    Wrapper<Topic> getQueryWrapper(TopicQueryRequest topicQueryRequest);
    TopicVO topicToTopicVO(Topic topic);
}
