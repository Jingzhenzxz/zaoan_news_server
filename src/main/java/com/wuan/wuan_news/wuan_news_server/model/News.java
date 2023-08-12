package com.wuan.wuan_news.wuan_news_server.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Model representing a news item")
public class News {

    @Schema(description = "Unique identifier for the news", example = "123")
    private Long id;

    @Schema(description = "Title of the news", example = "Breaking News!")
    private String title;

    @Schema(description = "Description of the news", example = "Details about the breaking news...")
    private String description;

    @Schema(description = "URL to the preview image for the news", example = "http://example.com/image.jpg")
    private String previewImage;

    @Schema(description = "Publication date of the news", example = "2023-07-28T10:15:30")
    private LocalDateTime pubDate;

    @Schema(description = "Link to the full news article", example = "http://example.com/news/breaking-news")
    private String link;

    @Schema(description = "Author of the news", example = "John Doe")
    private String author;

    @Schema(description = "Name of the media publishing the news", example = "ABC News")
    private String mediaName;

    @Schema(description = "Creation date of the news record", example = "2023-07-27T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date of the news record", example = "2023-07-28T10:15:30")
    private LocalDateTime updatedAt;
}