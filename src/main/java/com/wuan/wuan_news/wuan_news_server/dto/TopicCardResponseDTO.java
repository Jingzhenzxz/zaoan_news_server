package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 11:12
 * @description
 */
@Data
@AllArgsConstructor
public class TopicCardResponseDTO {
    private List<TopicCardDTO> listOfTopicCardDTO;
}
