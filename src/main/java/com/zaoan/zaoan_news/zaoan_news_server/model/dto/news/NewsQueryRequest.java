package com.zaoan.zaoan_news.zaoan_news_server.model.dto.news;

import com.zaoan.zaoan_news.zaoan_news_server.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: zaoan_news_server
 * @description:
 * @author: Jingzhen
 * @create: 2024-06-21 11:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class NewsQueryRequest extends PageRequest implements Serializable {
    /**
     * 新闻编码
     */
    private Long id;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 发表时间
     */
    private LocalDateTime pubDate;

    /**
     * 新闻地址
     */
    private String link;

    /**
     * 新闻作者
     */
    private String author;

    /**
     * 媒体名称
     */
    private String mediaName;

    /**
     * 媒体编码
     */
    private Long mediaId;

    /**
     * 话题编码
     */
    private Long topicId;

    private static final long serialVersionUID = 1L;
}