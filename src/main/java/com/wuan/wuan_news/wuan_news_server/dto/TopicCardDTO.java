package com.wuan.wuan_news.wuan_news_server.dto;

import com.wuan.wuan_news.wuan_news_server.model.News;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 11:11
 * @description
 */
@Data
public class TopicCardDTO {
    private String topicName;
    private List<News> listOfNews;
    private long newContentTodayCount;

    public TopicCardDTO(String topicName, List<News> listOfNews, long newContentTodayCount) {
        this.topicName = topicName;
        this.listOfNews = listOfNews;
        this.newContentTodayCount = newContentTodayCount;
    }
}
