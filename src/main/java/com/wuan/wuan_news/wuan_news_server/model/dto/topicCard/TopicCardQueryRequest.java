package com.wuan.wuan_news.wuan_news_server.model.dto.topicCard;

import com.wuan.wuan_news.wuan_news_server.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @program: wuan_news_server
 * @description:
 * @author: Jingzhen
 * @create: 2024-06-21 11:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TopicCardQueryRequest extends PageRequest implements Serializable {
    /**
     * 新闻编码
     */
    private Long id;
    private String name;
    private Long userId;
    private static final long serialVersionUID = 1L;
}