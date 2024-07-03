package com.wuan.wuan_news.wuan_news_server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuan.wuan_news.wuan_news_server.common.BaseResponse;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.common.ResultUtils;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.exception.ThrowUtils;
import com.wuan.wuan_news.wuan_news_server.model.dto.news.NewsQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import com.wuan.wuan_news.wuan_news_server.model.vo.NewsVO;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping("/list/page")
    public BaseResponse listNewsByPage(@RequestBody NewsQueryRequest newsQueryRequest, HttpServletRequest httpServletRequest) {
        long current = newsQueryRequest.getCurrent();
        long size = newsQueryRequest.getPageSize();
        Page<News> userPage = newsService.page(new Page<>(current, size),
                newsService.getQueryWrapper(newsQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取全站资讯
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<NewsVO>> listNewsVOByPage(@RequestBody NewsQueryRequest newsQueryRequest, HttpServletRequest httpServletRequest) {
        long current = newsQueryRequest.getCurrent();
        long size = newsQueryRequest.getPageSize();
        Page<News> newsPage = newsService.page(new Page<>(current, size),
                newsService.getQueryWrapper(newsQueryRequest));

        Page<NewsVO> newsVOPage = new Page<>(current, size, newsPage.getTotal());
        List<News> newsList = newsPage.getRecords();
        List<NewsVO> newsVOList = newsList.stream()
                .sorted(Comparator.comparing(News::getUpdatedAt))
                .map(news -> {
                    NewsVO newsVO = new NewsVO();
                    BeanUtils.copyProperties(news, newsVO);
                    return newsVO;
                }).collect(Collectors.toList());
        newsVOPage.setRecords(newsVOList);
        return ResultUtils.success(newsVOPage);
    }

    /**
     * 获取单个资讯详情
     */
    @GetMapping("/get")
    public BaseResponse<News> getNewsById(@RequestParam Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        News news = newsService.getById(id);
        ThrowUtils.throwIf(news == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(news);
    }

    /**
     * 获取单个资讯详情
     */
    @GetMapping("/get/vo")
    public BaseResponse<NewsVO> getNewsVOById(@RequestParam Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        News news = newsService.getById(id);
        ThrowUtils.throwIf(news == null, ErrorCode.NOT_FOUND_ERROR);

        NewsVO newsVO = new NewsVO();
        BeanUtils.copyProperties(news, newsVO);
        return ResultUtils.success(newsVO);
    }
}
