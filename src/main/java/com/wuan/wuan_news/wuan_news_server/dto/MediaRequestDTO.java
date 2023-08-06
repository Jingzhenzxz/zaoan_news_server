package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 15:24
 * @description
 */
@Data
public class MediaRequestDTO {
    @NotEmpty(message = "媒体名称不能为空")
    private String name;
    @NotEmpty(message = "RSS 地址不能为空")
    private String rssLink;
}
