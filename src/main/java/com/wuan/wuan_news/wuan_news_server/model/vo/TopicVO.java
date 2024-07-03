package com.wuan.wuan_news.wuan_news_server.model.vo;

import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Jingzhen
 */
@Data
public class TopicVO implements Serializable {
    private Long id;
    private String topicName;
    @Getter(AccessLevel.NONE) // 禁用 Lombok 自动生成的 getter
    private List<News> listOfNews;
    private long newContentTodayCount;

    private static final long serialVersionUID = 1L;

    // 手动提供一个返回不可修改视图的 getter
    public List<News> getListOfNews() {
        return Collections.unmodifiableList(listOfNews);
    }
}
