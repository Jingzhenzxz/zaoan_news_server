package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:52
 * @description
 */
@Data
public class MediaDTO {
    @NotEmpty(message = "媒体名称不能为空")
    private String name;

    @NotEmpty(message = "RSS地址不能为空")
    private String rssLink;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
