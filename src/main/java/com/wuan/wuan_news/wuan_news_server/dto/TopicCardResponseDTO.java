package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Collections;
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
    @Getter(AccessLevel.NONE) // 禁用 Lombok 自动生成的 getter
    private List<TopicCardDTO> listOfTopicCardDTO;

    // 手动提供一个返回不可修改视图的 getter
    public List<TopicCardDTO> getListOfCardDTO() {
        return Collections.unmodifiableList(listOfTopicCardDTO);
    }
}
