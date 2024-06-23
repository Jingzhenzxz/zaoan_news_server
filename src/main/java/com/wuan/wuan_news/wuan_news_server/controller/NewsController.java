package com.wuan.wuan_news.wuan_news_server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuan.wuan_news.wuan_news_server.common.BaseResponse;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.common.ResultUtils;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.exception.ThrowUtils;
import com.wuan.wuan_news.wuan_news_server.model.dto.news.NewsQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:27
 * @description
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * 分页获取全站资讯
     */
    @GetMapping("/list/page")
    public BaseResponse listNewsByPage(@RequestBody NewsQueryRequest newsQueryRequest, HttpServletRequest httpServletRequest) {
        long current = newsQueryRequest.getCurrent();
        long size = newsQueryRequest.getPageSize();
        Page<News> userPage = newsService.page(new Page<>(current, size),
                newsService.getQueryWrapper(newsQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 获取单个资讯详情
     */
    @GetMapping("/get")
    public BaseResponse<News> getNewsById(
            @RequestBody Long id,
            HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        News news = newsService.getById(id);
        ThrowUtils.throwIf(news == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(news);
    }
}
