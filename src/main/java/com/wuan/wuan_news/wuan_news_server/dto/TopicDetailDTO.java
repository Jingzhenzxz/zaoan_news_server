package com.wuan.wuan_news.wuan_news_server.dto;

import com.wuan.wuan_news.wuan_news_server.model.News;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 20:21
 * @description
 */
@Data
public class TopicDetailDTO {
    private String topicName;
    private List<News> newsForTopic;

    public TopicDetailDTO(String topicName, List<News> newsForTopic) {
        this.topicName = topicName;
        this.newsForTopic = newsForTopic;
    }
}
