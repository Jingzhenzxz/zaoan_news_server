package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.exception.UnauthorizedException;
import com.wuan.wuan_news.wuan_news_server.model.News;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:27
 * @description
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // 获取全站资讯
    @GetMapping
    public ResponseEntity<List<News>> getAllNews(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        List<News> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    // 获取单个媒体的所有资讯
    @GetMapping("/{mediaName}")
    public ResponseEntity<List<News>> getNewsByMediaName(@PathVariable String mediaName, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        List<News> newsList = newsService.getNewsByMediaName(mediaName);
        return ResponseEntity.ok(newsList);
    }

    // 获取单个资讯详情
    @GetMapping("/{mediaName}/{newsTitle}")
    public ResponseEntity<News> getNewsByMediaNameAndNewsTitle(@PathVariable String mediaName,
                                                               @PathVariable String newsTitle,
                                                               Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        News news = newsService.getNewsByMediaNameAndNewsTitle(mediaName, newsTitle);
        return ResponseEntity.ok(news);
    }
}
