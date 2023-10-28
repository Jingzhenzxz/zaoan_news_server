package com.wuan.wuan_news.wuan_news_server.mapper;

import com.wuan.wuan_news.wuan_news_server.model.Topic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:42
 * @description
 */
@Mapper
public interface TopicMapper {
    int createTopic(Topic topic);

    Topic getTopicByName(String topicName);

    List<Topic> getAllTopics();

    Topic getTopicByTopicId(Long topicId);

    Long getTopicIdByName(String topicName);

    String getTopicNameByTopicId(Long topicId);
}
