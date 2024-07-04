package com.zaoan.zaoan_news.zaoan_news_server.model.dto.topic;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @description
 */
@Data
public class TopicAddRequest implements Serializable {
    /**
     * 话题名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}