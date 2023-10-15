package com.wuan.wuan_news.wuan_news_server.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/10/14/ 21:05
 * @description
 */
@Data
@Schema(description = "UserTopic representation")
public class UserTopic {
    @Schema(description = "userId")
    private Long userId;
    @Schema(description = "topicId")
    private Long topicId;
}
