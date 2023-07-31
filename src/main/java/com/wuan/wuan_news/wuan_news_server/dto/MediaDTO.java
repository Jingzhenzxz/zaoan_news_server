package com.wuan.wuan_news.wuan_news_server.dto;

import javax.validation.constraints.NotEmpty;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:52
 * @description
 */
public class MediaDTO {
    @NotEmpty(message = "媒体名称不能为空")
    private String name;

    @NotEmpty(message = "RSS地址不能为空")
    private String rssLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }
}
