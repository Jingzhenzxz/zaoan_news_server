package com.wuan.wuan_news.wuan_news_server.model.vo;

import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 11:11
 * @description
 */
@Data
public class TopicCardVO {
    private String topicName;
    @Getter(AccessLevel.NONE) // 禁用 Lombok 自动生成的 getter
    private List<News> listOfNews;
    private long newContentTodayCount;

    public TopicCardVO(String topicName, List<News> listOfNews, long newContentTodayCount) {
        this.topicName = topicName;
        this.listOfNews = listOfNews;
        this.newContentTodayCount = newContentTodayCount;
    }

    // 手动提供一个返回不可修改视图的 getter
    public List<News> getListOfNews() {
        return Collections.unmodifiableList(listOfNews);
    }
}
