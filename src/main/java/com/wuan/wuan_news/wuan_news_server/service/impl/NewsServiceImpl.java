package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import com.wuan.wuan_news.wuan_news_server.exception.NewsNotFoundException;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsMapper;
import com.wuan.wuan_news.wuan_news_server.model.News;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import com.wuan.wuan_news.wuan_news_server.util.NewsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private final NewsUtil newsUtil;

    @Autowired
    public NewsServiceImpl(NewsMapper newsMapper, NewsUtil newsUtil) {
        this.newsMapper = newsMapper;
        this.newsUtil = newsUtil;
    }

    @Override
    public List<NewsDTO> getAllNews() {
        List<News> allNews = newsMapper.getAllNews();
        if (allNews == null) {
            throw new NewsNotFoundException("获取到的资讯为空");
        }
        return allNews.stream().map(newsUtil::convertNewsModelToNewsDTO).collect(Collectors.toList());
    }

    @Override
    public NewsDTO getNewsByMediaNameAndNewsTitle(String mediaName, String newsTitle) {
        News news = newsMapper.getNewsByMediaNameAndNewsTitle(mediaName, newsTitle);
        if (news == null) {
            throw new NewsNotFoundException("获取到的资讯为空");
        }
        return newsUtil.convertNewsModelToNewsDTO(news);
    }

    @Override
    public List<NewsDTO> getNewsByMediaName(String mediaName) {
        List<News> newsList = newsMapper.getNewsByMediaName(mediaName);
        if (newsList == null) {
            throw new NewsNotFoundException("获取到的资讯为空");
        }
        return newsList.stream().map(newsUtil::convertNewsModelToNewsDTO).collect(Collectors.toList());
    }
}
