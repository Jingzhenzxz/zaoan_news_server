package com.wuan.wuan_news.wuan_news_server.mapper;

import com.wuan.wuan_news.wuan_news_server.model.News;
import com.wuan.wuan_news.wuan_news_server.model.NewsTopic;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:58
 * @description
 */
public interface NewsTopicMapper {
    List<News> getNewsByTopicId(Long id);

    void create(NewsTopic newsTopic);
}
