package com.zaoan.zaoan_news.zaoan_news_server.model.dto.userTopicFollowing;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: zaoan_news_server
 * @description:
 * @author: Jingzhen
 * @create: 2024-06-30 17:35
 **/
@Data
public class UserTopicFollowingAddRequest implements Serializable {
    /**
     * 话题编码
     */
    private Long topicId;

    /**
     * 用户编码
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
