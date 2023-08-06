package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/05/ 11:17
 * @description
 */
@Data
public class NewsDTO {
    private String title;
    private String description;
    private String previewImage;
    private LocalDateTime pubDate;
    private String link;
    private String author;
    private Long mediaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
