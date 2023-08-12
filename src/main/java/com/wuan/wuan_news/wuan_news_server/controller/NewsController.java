package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import com.wuan.wuan_news.wuan_news_server.dto.NewsResponseDTO;
import com.wuan.wuan_news.wuan_news_server.exception.UnauthorizedException;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:27
 * @description
 */
@Api(tags = "News Endpoints", value = "Endpoints for managing news")
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // 获取全站资讯
    @ApiOperation(value = "Get all news")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved news list"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated")
    })
    @GetMapping
    public ResponseEntity<NewsResponseDTO> getAllNews(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        List<NewsDTO> newsDTOList = newsService.getAllNews();
        return ResponseEntity.ok(new NewsResponseDTO(newsDTOList));
    }

    // 获取单个媒体的所有资讯
    @ApiOperation(value = "Get news by media name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved news list by media name"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated")
    })
    @GetMapping("/{mediaName}")
    public ResponseEntity<NewsResponseDTO> getNewsByMediaName(
            @ApiParam(value = "Media name to retrieve news for", required = true)
            @PathVariable String mediaName, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        List<NewsDTO> newsDTOList = newsService.getNewsByMediaName(mediaName);
        return ResponseEntity.ok(new NewsResponseDTO(newsDTOList));
    }

    // 获取单个资讯详情
    @ApiOperation(value = "Get specific news detail by media name and news title")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved specific news detail"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated")
    })
    @GetMapping("/{mediaName}/{newsTitle}")
    public ResponseEntity<NewsResponseDTO> getNewsByMediaNameAndNewsTitle(
            @ApiParam(value = "Media name of the news", required = true)
            @PathVariable String mediaName,
            @ApiParam(value = "Title of the news", required = true)
            @PathVariable String newsTitle,
            Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        NewsDTO newsDTO = newsService.getNewsByMediaNameAndNewsTitle(mediaName, newsTitle);
        return ResponseEntity.ok(new NewsResponseDTO(Collections.singletonList(newsDTO)));
    }
}
