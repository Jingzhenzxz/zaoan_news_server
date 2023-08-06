package com.wuan.wuan_news.wuan_news_server.mapper;

import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import com.wuan.wuan_news.wuan_news_server.model.News;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:00
 * @description
 */
@Mapper
public interface NewsMapper {
    List<News> getAllNews();

    News getNewsByMediaNameAndNewsTitle(String mediaName, String newsTitle);

    int insert(News news);

    int update(News news);

    List<News> getNewsByMediaName(String mediaName);
}
