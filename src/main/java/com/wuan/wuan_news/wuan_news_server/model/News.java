package com.wuan.wuan_news.wuan_news_server.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 14:01
 * @description
 */
@Data
public class News {
    private Long id;

    private String title;

    private String description;

    private String previewImage;

    private LocalDateTime pubDate;

    private String link;

    private String author;

    private String mediaName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
