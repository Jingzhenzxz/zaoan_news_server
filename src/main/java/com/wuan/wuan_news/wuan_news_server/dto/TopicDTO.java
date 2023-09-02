package com.wuan.wuan_news.wuan_news_server.dto;

import com.wuan.wuan_news.wuan_news_server.model.Topic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 10:04
 * @description
 */
@Data
public class TopicDTO {
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
