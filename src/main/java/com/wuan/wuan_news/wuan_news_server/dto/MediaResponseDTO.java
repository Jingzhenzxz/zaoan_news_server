package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:51
 * @description
 */
@Data
@AllArgsConstructor
public class MediaResponseDTO {
    private String message;
    private MediaDTO mediaDTO;
}
