package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 10:05
 * @description
 */
@Data
public class TopicResponseDTO {
    public TopicResponseDTO(TopicDTO topicDTO) {
        this.name = topicDTO.getName();
        this.createdAt = topicDTO.getCreatedAt();
        this.updatedAt = topicDTO.getUpdatedAt();
    }

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
