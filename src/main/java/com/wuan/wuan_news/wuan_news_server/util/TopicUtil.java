package com.wuan.wuan_news.wuan_news_server.util;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicDTO;
import com.wuan.wuan_news.wuan_news_server.model.Media;
import com.wuan.wuan_news.wuan_news_server.model.Topic;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 16:00
 * @description
 */
@Service
public class TopicUtil {
    public TopicDTO convertTopicModelToTopicDTO(Topic topic) {
        if (topic == null) {
            return null;
        }
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setName(topic.getName());
        topicDTO.setCreatedAt(topic.getCreatedAt());
        topicDTO.setUpdatedAt(topic.getUpdatedAt());

        return topicDTO;
    }

    public Topic convertTopicDTOToTopicModel(TopicDTO topicDTO) {
        if (topicDTO == null) {
            return null;
        }

        Topic topic = new Topic();
        topic.setName(topicDTO.getName());
        topic.setCreatedAt(topicDTO.getCreatedAt());
        topic.setUpdatedAt(topicDTO.getUpdatedAt());

        return topic;
    }
}
