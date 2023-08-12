package com.wuan.wuan_news.wuan_news_server.service;

import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:35
 * @description
 */
public interface NewsService {
    List<NewsDTO> getAllNews();

    NewsDTO getNewsByMediaNameAndNewsTitle(String mediaName, String newsTitle);

    List<NewsDTO> getNewsByMediaName(String mediaName);
}
