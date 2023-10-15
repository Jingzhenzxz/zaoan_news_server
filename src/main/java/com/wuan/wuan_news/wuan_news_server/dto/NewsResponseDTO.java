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
 * @date 2023/08/12/ 16:48
 * @description
 */
@Data
@AllArgsConstructor
public class NewsResponseDTO {
    @Getter(AccessLevel.NONE) // 禁用 Lombok 自动生成的 getter
    private List<NewsDTO> newsDTO;

    public List<NewsDTO> getNewsDTO() {
        return Collections.unmodifiableList(newsDTO);
    }
}
