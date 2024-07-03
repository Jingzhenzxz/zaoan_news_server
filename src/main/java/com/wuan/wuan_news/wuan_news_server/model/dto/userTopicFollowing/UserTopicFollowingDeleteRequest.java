package com.wuan.wuan_news.wuan_news_server.model.dto.userTopicFollowing;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * @author Jingzhen
 * @date 2024/6/30 20:11
 * @description 
 */
@Data
public class UserTopicFollowingDeleteRequest implements Serializable {
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
