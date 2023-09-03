package com.wuan.wuan_news.wuan_news_server.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:40
 * @description
 */
@Data
@Schema(description = "Topic representation")
public class Topic {
    @Schema(description = "Unique identifier for the topic", example = "123")
    private Long id;
    @Schema(description = "Name of the topic", example = "Computer")
    private String name;
    @Schema(description = "Creation date of the topic record", example = "2023-07-27T10:15:30")
    private LocalDateTime createdAt;
    @Schema(description = "Last update date of the topic record", example = "2023-07-28T10:15:30")
    private LocalDateTime updatedAt;
}
