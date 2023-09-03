package com.wuan.wuan_news.wuan_news_server.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:57
 * @description
 */
@Data
@Schema(description = "NewsTopic representation")
public class NewsTopic {
    @Schema(description = "Unique identifier for the news", example = "123")
    private Long newsId;
    @Schema(description = "Unique identifier for the topic", example = "123")
    private Long topicId;
}
