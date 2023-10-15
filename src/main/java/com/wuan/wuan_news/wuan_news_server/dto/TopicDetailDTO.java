package com.wuan.wuan_news.wuan_news_server.dto;

import com.wuan.wuan_news.wuan_news_server.model.News;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Collections;
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
    @Getter(AccessLevel.NONE) // 禁用 Lombok 自动生成的 getter
    private List<News> newsForTopic;

    public TopicDetailDTO(String topicName, List<News> newsForTopic) {
        this.topicName = topicName;
        this.newsForTopic = newsForTopic;
    }

    // 手动提供一个返回不可修改视图的 getter
    public List<News> getNewsForTopic() {
        return Collections.unmodifiableList(newsForTopic);
    }
}
