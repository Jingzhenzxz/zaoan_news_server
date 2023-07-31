package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.model.News;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

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

    // 获取全站资讯API
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    // 获取单个资讯详情API
    @GetMapping("/{mediaName}/{newsTitle}")
    public ResponseEntity<Object> getNewsByMediaNameAndNewsTitle(@PathVariable String mediaName, @PathVariable String newsTitle) {
        News news = newsService.getNewsByMediaNameAndNewsTitle(mediaName, newsTitle);
        if (news != null) {
            return ResponseEntity.ok(news);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("请求的资讯不存在"); // 在这里你可以添加更具体的错误信息
        }
    }
}
