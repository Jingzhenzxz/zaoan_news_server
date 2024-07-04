package com.zaoan.zaoan_news.zaoan_news_server.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.news.NewsQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.News;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:35
 * @description
 */
public interface NewsService extends IService<News> {
    QueryWrapper<News> getQueryWrapper(NewsQueryRequest newsQueryRequest);
}
