package com.wuan.wuan_news.wuan_news_server.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 15:37
 * @description
 */

@Data
@Schema(description = "Media representation")
public class Media {

    @Schema(description = "Unique identifier of the Media.", example = "1", required = true)
    private Long id;

    @Schema(description = "Name of the Media.", example = "Sample Media", required = true)
    private String name;

    @Schema(description = "RSS Link of the Media.", example = "http://sample.com/rss", required = true)
    private String rssLink;

    @Schema(description = "Creation timestamp of the Media.", example = "2023-07-28T15:52:36", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "Update timestamp of the Media.", example = "2023-07-29T16:45:30")
    private LocalDateTime updatedAt;
}