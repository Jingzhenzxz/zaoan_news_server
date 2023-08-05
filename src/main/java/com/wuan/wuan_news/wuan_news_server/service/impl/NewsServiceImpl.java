package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.exception.NewsException;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsMapper;
import com.wuan.wuan_news.wuan_news_server.model.News;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:35
 * @description
 */
@Service
public class NewsServiceImpl implements NewsService {
    private final NewsMapper newsMapper;

    @Autowired
    public NewsServiceImpl(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    @Override
    public List<News> getAllNews() {
        List<News> allNews = newsMapper.getAllNews();
        if (allNews == null) {
            throw new NewsException("获取到的资讯为空");
        }
        return allNews;
    }

    @Override
    public News getNewsByMediaNameAndNewsTitle(String mediaName, String newsTitle) {
        News news = newsMapper.getNewsByMediaNameAndNewsTitle(mediaName, newsTitle);
        if (news == null) {
            throw new NewsException("获取到的资讯为空");
        }
        return news;
    }
}
