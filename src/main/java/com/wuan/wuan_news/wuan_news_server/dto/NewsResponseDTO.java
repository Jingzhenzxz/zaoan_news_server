package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

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
    private List<NewsDTO> newsDTO;
}
